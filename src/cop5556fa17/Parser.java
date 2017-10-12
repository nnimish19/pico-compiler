package cop5556fa17;



import java.util.ArrayList;
import java.util.Arrays;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.Parser.SyntaxException;
import static cop5556fa17.Scanner.Kind.*;
import cop5556fa17.AST.*;

public class Parser {

	@SuppressWarnings("serial")
	public class SyntaxException extends Exception {
		Token t;

		public SyntaxException(Token t, String message) {
			super(message);
			this.t = t;
		}

	}

	Scanner scanner;
	Token t;
	static Kind[] dec_keys = {KW_int,KW_boolean,KW_image,KW_url,KW_file};
	static Kind[] fun_names = {KW_sin, KW_cos , KW_atan , KW_abs , KW_cart_x , KW_cart_y , KW_polar_a , KW_polar_r};
	static Kind[] unary_kw_keys = {KW_x , KW_y , KW_r , KW_a , KW_X , KW_Y , KW_Z , KW_A , KW_R , KW_DEF_X , KW_DEF_Y};

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}
	
	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public Program parse() throws SyntaxException {
		Program p = program();
		matchEOF();
		return p;
	}
	
	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	
//	Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*
//	Program(Token firstToken, Token name, ArrayList<ASTNode> decsAndStatements)
	public Program program() throws SyntaxException {
		Program p=null;
		Token first = t;
		ArrayList<ASTNode> list=new ArrayList<ASTNode>(); 
		
		match(IDENTIFIER);
		while(isAnyKind(dec_keys) || t.isKind(IDENTIFIER)){
			if(isAnyKind(dec_keys)){
				list.add(declaration());
			}
			else if(t.isKind(IDENTIFIER)){
				list.add(statement());
			}
			match(SEMI);
		}
		p = new Program(first, first, list);
		return p;
	}
	
//	Declaration :: =  VariableDeclaration     |    ImageDeclaration   |   SourceSinkDeclaration  
//	VariableDeclaration  ::=  VarType IDENTIFIER  (  OP_ASSIGN  Expression  | NULL )
//	VarType ::= KW_int | KW_boolean

//	ImageDeclaration ::=  KW_image  (LSQUARE Expression COMMA Expression RSQUARE | NULL) IDENTIFIER ( OP_LARROW Source | NULL )   

//	SourceSinkDeclaration ::= SourceSinkType IDENTIFIER  OP_ASSIGN  Source
//	SourceSinkType := KW_url | KW_file
	public Declaration declaration() throws SyntaxException{
		Token first =t;
		Declaration d=null;
//		VariableDeclaration: Declaration_Variable(Token firstToken,  Token type, Token name, Expression e)
		if(t.isKind(KW_int)||t.isKind(KW_boolean)){
			Token type=t;
			consume();
			Token name=t;
			match(IDENTIFIER);
			Expression e=null;
			if(t.isKind(OP_ASSIGN)){
				consume();
				e=expression();
			}
			d=new Declaration_Variable(first, type, name, e);
			return d;
		}
//		ImageDeclaration: Declaration_Image(Token firstToken, Expression xSize, Expression ySize, Token name, Source source)
		else if(t.isKind(KW_image)){
			consume();
			Expression xsize=null,ysize=null;
			if(t.isKind(LSQUARE)){
				consume();
				xsize=expression();
				match(COMMA);
				ysize=expression();
				match(RSQUARE);
			}
			
			Token name = t;
			match(IDENTIFIER);
			
			Source s=null;
			if(t.isKind(OP_LARROW)){
				consume();
				s = source();
			}
			d= new Declaration_Image(first, xsize, ysize, name, s);
			return d;
		}
//		SourceSinkDeclaration: Declaration_SourceSink(Token firstToken, Token type, Token name, Source source)
		else{	//KW_url | KW_file
			Token type = t;
			consume();
			Token name = t;
			match(IDENTIFIER);
			match(OP_ASSIGN);
			Source s = source();
			d = new Declaration_SourceSink(first, type, name, s);
			return d;
		}
	}

//	Statement  ::= AssignmentStatement 
//		  	| ImageOutStatement    
//		| ImageInStatement    

//	AssignmentStatement ::= Lhs OP_ASSIGN Expression	
//					 Lhs::=  IDENTIFIER ( LSQUARE LhsSelector RSQUARE   | NULL )
//	ImageOutStatement ::= IDENTIFIER OP_RARROW Sink 
//	ImageInStatement ::= IDENTIFIER OP_LARROW Source
	public Statement statement() throws SyntaxException{
		Token first = t;
		Statement stm = null;
		
		match(IDENTIFIER);
//		AssignmentStatement: Statement_Assign(Token firstToken, LHS lhs, Expression e)
		if(t.isKind(OP_ASSIGN)||t.isKind(LSQUARE)){
			LHS lhs=null;
			
			if (t.isKind(LSQUARE)){	//Note this is optional. 
				consume();
				lhs = lhsSelector(first);	//PASS the first token = identifier. 
				match(RSQUARE);
			}
			match(OP_ASSIGN);
			
			Expression e = expression();
			stm = new Statement_Assign(first, lhs, e);
		}
//		ImageOutStatement: Statement_Out(Token firstToken, Token name, Sink sink) {
		else if(t.isKind(OP_RARROW)){
			consume();
			Sink s = sink();
			stm = new Statement_Out(first, first, s);
		}
//		ImageInStatement: Statement_In(Token firstToken, Token name, Source source) {
		else if(t.isKind(OP_LARROW)){
			consume();
			Source s = source();
			stm = new Statement_In(first, first, s);
		}
		else{
			String message = "Unexpected token at line:position " + t.line + ":" + t.pos_in_line;
			message+=". Expected = or [ or -> or <-";
			throw new SyntaxException(t, message);
		}
		return stm;
	}

	
//	Source ::= STRING_LITERAL  
//	Source ::= OP_AT Expression 
//	Source ::= IDENTIFIER 
	public Source source() throws SyntaxException{	//First token must be one of the above or throw exception
		Token first=t;
		Source s=null;
		if(t.isKind(STRING_LITERAL)){//Source_StringLiteral(Token firstToken, String fileOrUrl)
			s=new Source_StringLiteral(first, t.getText());	//check here getTEXT would return "cefva" including the double quotes
			consume();
		}
		else if(t.isKind(IDENTIFIER)){//Source_Ident(Token firstToken, Token name)
			s=new Source_Ident(first, t);
			consume();
		}
		else if(t.isKind(OP_AT)){ //Source_CommandLineParam(Token firstToken, Expression paramNum)
			consume();
			Expression e = expression();
			s = new Source_CommandLineParam(first, e);
		}
		else{
			String message = "Unexpected token at line:position " + t.line + ":" + t.pos_in_line;
			message+=". Expected STRING_LITERAL, @ or IDENTIFIER.";
			throw new SyntaxException(t, message);
		}
		return s;
	}	
	
//	Sink ::= IDENTIFIER | KW_SCREEN  //ident must be file
	public Sink sink() throws SyntaxException{
		Token first = t;
		Sink s =null;
		if(t.isKind(IDENTIFIER)||t.isKind(KW_SCREEN)){
			if(t.isKind(IDENTIFIER)){	//Sink_Ident(Token firstToken, Token name)
				s = new Sink_Ident(first, t);
			}else{						// Sink_SCREEN(Token firstToken)
				 s = new Sink_SCREEN(first);
			}
			consume();
			return s;
		}
		else{
			String message = "Unexpected token at line:position " + t.line + ":" + t.pos_in_line;
			message+=". Expected IDENTIFIER or KW_SCREEN.";
			throw new SyntaxException(t, message);
		}
	}
	
//	Lhs::=  IDENTIFIER ( LSQUARE LhsSelector RSQUARE   | NULL )
//	LhsSelector ::= LSQUARE  ( XySelector  | RaSelector  )   RSQUARE
//	XySelector ::= KW_x COMMA KW_y
//	RaSelector ::= KW_r COMMA KW_A
	public LHS lhsSelector(Token name) throws SyntaxException{	//LHS(Token firstToken, Token name, Index index)
		Token first = name;		//first token(place where u throw error) would be name of the variable itself. and not LSQUARE [
		Expression e1=null,e2=null;
		Index ind =null;
		LHS lhs=null;
		
		match(LSQUARE);
		if(t.isKind(KW_x)){
			Token tmp=t;
			e1= new Expression_PredefinedName(t, t.kind); 	//CHECK predefined name 
			consume();
			match(COMMA);
			e2 = new Expression_PredefinedName(t, t.kind);
			match(KW_y);
			ind= new Index(tmp, e1,e2);
		}
		else if(t.isKind(KW_r)){
			Token tmp=t;
			e1= new Expression_PredefinedName(t, t.kind);
			consume();
			match(COMMA);
			e2= new Expression_PredefinedName(t, t.kind);
			match(KW_A);
			ind= new Index(tmp, e1,e2);
		}
		else{
			String message = "Expected KW_x, or KW_r at line:position " + t.line + ":" + t.pos_in_line;
			throw new SyntaxException(t, message);
		}
		match(RSQUARE);
		lhs = new LHS(first, name, ind); 
		return lhs;
	}
	

	/**
	 * Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression    | OrExpression
	 * 
	 * Our test cases may invoke this routine directly to support incremental development.
	 * 
	 * @throws SyntaxException
	 */
//	Expression ::=  OrExpression  OP_Q  Expression OP_COLON Expression
//            |   OrExpression
//OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
//AndExpression ::= EqExpression ( OP_AND  EqExpression )*
//EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*
//RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
//AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*
//MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*

	public Expression expression() throws SyntaxException {
		Token first=t;
		Expression e0;
		Expression e1;
		Expression e2;
		e0 = orExp();
		if(t.isKind(OP_Q)){		//for 0 or more times; manually check using if-statement. and don't use match() function. 
			consume();
			e1 = expression();
			match(OP_COLON);	//Must match. if does not match throw error; 
			e2 = expression();
			return new Expression_Conditional(first, e0,e1,e2);
		}
		return e0;
	}
	
//	OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
	public Expression orExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=andExp();
		while(t.isKind(OP_OR)){ // 0 or more times. don't use match().
			Token op=t;
			consume();			//already matched! just consume
			e1=andExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}

//		AndExpression ::= EqExpression ( OP_AND  EqExpression )*
	public Expression andExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=eqExp();
		while(t.isKind(OP_AND)){
			Token op=t;
			consume();
			e1=eqExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}

//		EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*	
	public Expression eqExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=relExp();
		while(t.isKind(OP_EQ)||t.isKind(OP_NEQ)){
			Token op=t;
			consume();
			e1=relExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}
	
//	RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
	public Expression relExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=addExp();
		while(t.isKind(OP_LT) || t.isKind(OP_GT) || t.isKind(OP_LE) || t.isKind(OP_GE) ){
			Token op = t;
			consume();
			e1=addExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}
	
//	AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*
	public Expression addExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=mulExp();
		while(t.isKind(OP_PLUS)||t.isKind(OP_MINUS)){
			Token op =t;
			consume();
			e1=mulExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}
	
//	MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*
	public Expression mulExp()throws SyntaxException{
		Token first = t;
		Expression e0;
		Expression e1;
		e0=uniExp();
		while(t.isKind(OP_TIMES)||t.isKind(OP_DIV) || t.isKind(OP_MOD)){
			Token op = t;
			consume();
			e1=uniExp();
			e0=new Expression_Binary(first, e0,op,e1);
		}
		return e0;
	}

//FIRST(UnaryExpression) = {OP_PLUS,OP_MINUS,OP_EXCL} U FIRST(PRIMARY) U FIRST(IdentOrPixelSelectorExpression)
//FIRST(PRIMARY) = INTEGER_LITERAL, LPAREN, FunctionName(KW_sin, KW_cos , KW_atan , KW_abs , KW_cart_x , KW_cart_y , KW_polar_a , KW_polar_r)
//FIRST(IdentOrPixelSelectorExpression) = {IDENTIFIER} U	
//{KW_x | KW_y | KW_r | KW_a | KW_X | KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y}
//	
//UnaryExpression ::= OP_PLUS UnaryExpression 
//            | OP_MINUS UnaryExpression 
//            | UnaryExpressionNotPlusMinus
//UnaryExpressionNotPlusMinus ::=  OP_EXCL  UnaryExpression
//				| Primary 
//				| IdentOrPixelSelectorExpression 
//				| KW_x | KW_y | KW_r | KW_a | KW_X | KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
	
//	IdentOrPixelSelectorExpression::=  IDENTIFIER LSQUARE Selector RSQUARE   | IDENTIFIER
//	Selector ::=  Expression COMMA Expression
	public Expression uniExp()throws SyntaxException{
		Token first=t;
		Expression e=null;
		
		if(t.isKind(OP_PLUS) || t.isKind(OP_MINUS) ||t.isKind(OP_EXCL)){
			Token op=t;
			consume();
			e=uniExp();
			e = new Expression_Unary(first,op,e);
		}
//		else if(primary()){}			  //PRIMARY: return true if used else check down the path
		else if(t.isKind(INTEGER_LITERAL)){
			e=new Expression_IntLit(first, t.intVal());
			consume();
		}
		else if(t.isKind(BOOLEAN_LITERAL)){
			boolean b=t.getText()=="true"?true:false;
			e=new Expression_BooleanLit(first, b);
			consume();
		}
		else if(t.isKind(LPAREN)){
			consume();
			e = expression();
			match(RPAREN);
		}
		else if(isAnyKind(fun_names)){	//function application
			Token function=t; 
			consume();
			if(t.isKind(LPAREN)){
				consume();
				e = expression();
				e = new Expression_FunctionAppWithExprArg(first, function.kind, e);
				match(RPAREN);
			}
			else if(t.isKind(LSQUARE)){
				consume();
//				selector();
				Token tmp = t;
				Expression e1=expression();
				match(COMMA);
				Expression e2=expression();
				Index ind = new Index(tmp, e1, e2);
				e = new Expression_FunctionAppWithIndexArg(first, function.kind, ind);
				match(RSQUARE);
			}
			else{
				String message = "Expected '(' or '[' following the FunctionName at line:position " + t.line + ":" + t.pos_in_line;
				throw new SyntaxException(t, message);
			}
		}

//				IdentOrPixelSelectorExpression
		else if (t.isKind(IDENTIFIER)){   
			Token name = t;
			e = new Expression_Ident(first, t); 
			consume();
			if(t.isKind(LSQUARE)){
				consume();
				Token tmp = t;
				Expression e1=expression();
				match(COMMA);
				Expression e2=expression();
				match(RSQUARE);
				Index ind = new Index(tmp, e1,e2);
				e = new Expression_PixelSelector(first,name, ind);
			}
		}
//		KW_x | KW_y | KW_r | KW_a | KW_X | KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
		else if(isAnyKind(unary_kw_keys)){	
			e = new Expression_PredefinedName(first, t.kind);
			consume();
		}
		else {
			String message = "Expected unary Expression at line:position " + t.line + ":" + t.pos_in_line;
			throw new SyntaxException(t, message);
		}
		
		return e;
	}
	
//	Primary ::= INTEGER_LITERAL | LPAREN Expression RPAREN | FunctionApplication | BOOLEAN_LITERAL
//	FunctionApplication ::= FunctionName LPAREN Expression RPAREN  
//						 | FunctionName  LSQUARE Selector RSQUARE 
//	FunctionName ::= KW_sin | KW_cos | KW_atan | KW_abs 
//					| KW_cart_x | KW_cart_y | KW_polar_a | KW_polar_r
//	Selector ::=  Expression COMMA Expression   
	boolean primary() throws SyntaxException{	//RETURN TRUE if primary begins. else false
		Token first = t;	//CHECK should be outside
		Expression e=null;
		if(t.isKind(INTEGER_LITERAL)){
			e=new Expression_IntLit(first, t.intVal());
			consume();
			return true;
		}
		else if(t.isKind(BOOLEAN_LITERAL)){
			boolean b=t.getText()=="true"?true:false;
			e=new Expression_BooleanLit(first, b);
			consume();
			return true;
		}
		else if(t.isKind(LPAREN)){
			consume();
			e = expression();
			match(RPAREN);
			return true;
		}
		else if(isAnyKind(fun_names)){	//function application
			Token function=t; 
			consume();
			if(t.isKind(LPAREN)){
				consume();
				e = expression();
				e = new Expression_FunctionAppWithExprArg(first, function.kind, e);
				match(RPAREN);
			}
			else if(t.isKind(LSQUARE)){
				consume();
//				selector();
				Token tmp =t;
				Expression e1=expression();
				match(COMMA);
				Expression e2=expression();
				Index ind = new Index(tmp, e1, e2);
				e = new Expression_FunctionAppWithIndexArg(first, function.kind, ind);
				match(RSQUARE);
			}
			else{
				String message = "Expected '(' or '[' following the FunctionName at line:position " + t.line + ":" + t.pos_in_line;
				throw new SyntaxException(t, message);
			}
			return true;
		}
		return false;
	}


	/**
	 * Only for check at end of program. Does not "consume" EOF so no attempt to get
	 * nonexistent next Token.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind == EOF) {
			return t;
		}
		String message =  "Expected EOF at line:position " + t.line + ":" + t.pos_in_line;
		throw new SyntaxException(t, message);
	}
	
	
	
	private void consume() //we call consume only when token kinds match. So EOF should be handled.
	{
		if(t.kind!=EOF)
			t =  scanner.nextToken();
	}
	
	private void match(Kind k) throws SyntaxException{
		if(t.isKind(k)){
			consume();
		}
		else{
			String message =  "Match Error. Expected " + k + " at line:position " + t.line + ":" + t.pos_in_line;
			throw new SyntaxException(t, message);
		}
	}
	
	boolean isAnyKind(Kind[] kinds){
		for(int i=0;i<kinds.length;i++){
			if(t.isKind(kinds[i]))
				return true;
		}
		return false;
	}
}
