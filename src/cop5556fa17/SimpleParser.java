package cop5556fa17;



import java.util.Arrays;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.SimpleParser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class SimpleParser {

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

	SimpleParser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * Main method called by compiler to parser input.
	 * Checks for EOF
	 * 
	 * @throws SyntaxException
	 */
	public void parse() throws SyntaxException {
		program();
		matchEOF();
	}
	

	
	static Kind[] dec_keys = {KW_int,KW_boolean,KW_image,KW_url,KW_file};
	static Kind[] fun_names = {KW_sin, KW_cos , KW_atan , KW_abs , KW_cart_x , KW_cart_y , KW_polar_a , KW_polar_r};
	static Kind[] unary_kw_keys = {KW_x , KW_y , KW_r , KW_a , KW_X , KW_Y , KW_Z , KW_A , KW_R , KW_DEF_X , KW_DEF_Y};

	boolean isAnyKind(Kind[] kinds){
		for(int i=0;i<kinds.length;i++){
			if(t.isKind(kinds[i]))
				return true;
		}
		return false;
	}

	/**
	 * Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	 * 
	 * Program is start symbol of our grammar.
	 * 
	 * @throws SyntaxException
	 */
	
//	Program ::=  IDENTIFIER   ( Declaration SEMI | Statement SEMI )*   
	void program() throws SyntaxException {
		//TODO  implement this
//		throw new UnsupportedOperationException();
		
		match(IDENTIFIER);
		while(isAnyKind(dec_keys) || t.isKind(IDENTIFIER)){
			if(isAnyKind(dec_keys)){
				declaration();
			}
			else if(t.isKind(IDENTIFIER)){
				statement();
			}
			match(SEMI);
		}
	}
	
//	Declaration :: =  VariableDeclaration     |    ImageDeclaration   |   SourceSinkDeclaration  
//	VariableDeclaration  ::=  VarType IDENTIFIER  (  OP_ASSIGN  Expression  | NULL )
//	VarType ::= KW_int | KW_boolean

//	ImageDeclaration ::=  KW_image  (LSQUARE Expression COMMA Expression RSQUARE | NULL) IDENTIFIER ( OP_LARROW Source | NULL )   

//	SourceSinkDeclaration ::= SourceSinkType IDENTIFIER  OP_ASSIGN  Source
//	SourceSinkType := KW_url | KW_file
	void declaration() throws SyntaxException{
//		VariableDeclaration
		if(t.isKind(KW_int)||t.isKind(KW_boolean)){
			consume();
			match(IDENTIFIER);
			if(t.isKind(OP_ASSIGN)){
				consume();
				expression();
			}
		}
//		ImageDeclaration
		else if(t.isKind(KW_image)){
			consume();
			if(t.isKind(LSQUARE)){
				consume();
				expression();
				match(COMMA);
				expression();
				match(RSQUARE);
			}
			
			match(IDENTIFIER);
			
			if(t.isKind(OP_LARROW)){
				consume();
				source();
			}
		}
//		SourceSinkDeclaration
		else{	//KW_url | KW_file
			consume();
			match(IDENTIFIER);
			match(OP_ASSIGN);
			source();
		}
	}

//	Statement  ::= AssignmentStatement 
//		  	| ImageOutStatement    
//		| ImageInStatement    

//	AssignmentStatement ::= Lhs OP_ASSIGN Expression	
//					 Lhs::=  IDENTIFIER ( LSQUARE LhsSelector RSQUARE   | NULL )
//	ImageOutStatement ::= IDENTIFIER OP_RARROW Sink 
//	ImageInStatement ::= IDENTIFIER OP_LARROW Source
	void statement() throws SyntaxException{
		match(IDENTIFIER);
//		AssignmentStatement
		if(t.isKind(OP_ASSIGN)||t.isKind(LSQUARE)){
			if (t.isKind(LSQUARE)){	//Note this is optional. 
				consume();
				lhsSelector();
				match(RSQUARE);
			}
			match(OP_ASSIGN);
			expression();
		}
//		ImageOutStatement
		else if(t.isKind(OP_RARROW)){
			consume();
			sink();
		}
//		ImageInStatement
		else if(t.isKind(OP_LARROW)){
			consume();
			source();
		}
		else{
			String message = "Unexpected token at line:position " + t.line + ":" + t.pos_in_line;
			message+=". Expected = or [ or -> or <-";
			throw new SyntaxException(t, message);
		}
	}

	
//	Source ::= STRING_LITERAL  
//	Source ::= OP_AT Expression 
//	Source ::= IDENTIFIER 
	void source() throws SyntaxException{	//First token must be one of the above or throw exception
		if(t.isKind(STRING_LITERAL)) consume();
		else if(t.isKind(IDENTIFIER)) consume();
		else if(t.isKind(OP_AT)){
			consume();
			expression();
		}
		else{
			String message = "Unexpected token at line:position " + t.line + ":" + t.pos_in_line;
			message+=". Expected STRING_LITERAL, @ or IDENTIFIER.";
			throw new SyntaxException(t, message);
		}
	}	
	
//	Sink ::= IDENTIFIER | KW_SCREEN  //ident must be file
	void sink() throws SyntaxException{
		if(t.isKind(IDENTIFIER)||t.isKind(KW_SCREEN))	
			consume();
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
	void lhsSelector() throws SyntaxException{
		match(LSQUARE);
		if(t.isKind(KW_x)){
			consume();
			match(COMMA);
			match(KW_y);
		}
		else if(t.isKind(KW_r)){
			consume();
			match(COMMA);
			match(KW_A);
		}
		else{
			String message = "Expected KW_x, or KW_r at line:position " + t.line + ":" + t.pos_in_line;
			throw new SyntaxException(t, message);
		}
		match(RSQUARE);
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

	void expression() throws SyntaxException {
		//TODO implement this.
//		throw new UnsupportedOperationException();
		orExp();
		while(t.isKind(OP_Q)){		//for 0 or more times; manually check using if-statement. and don't use match() function. 
			consume();
			expression();
			match(OP_COLON);	//Must match. if does not match throw error; 
			expression();
		}
	}
	
//	OrExpression ::= AndExpression   (  OP_OR  AndExpression)*
	void orExp()throws SyntaxException{
		andExp();
		while(t.isKind(OP_OR)){ // 0 or more times. don't use match().  
			consume();			//already matched! just consume
			andExp();
		}
	}

//		AndExpression ::= EqExpression ( OP_AND  EqExpression )*
	void andExp()throws SyntaxException{
		eqExp();
		while(t.isKind(OP_AND)){
			consume();
			eqExp();
		}
	}

//		EqExpression ::= RelExpression  (  (OP_EQ | OP_NEQ )  RelExpression )*	
	void eqExp()throws SyntaxException{
		relExp();
		while(t.isKind(OP_EQ)||t.isKind(OP_NEQ)){
			consume();
			relExp();
		}
	}
	
//	RelExpression ::= AddExpression (  ( OP_LT  | OP_GT |  OP_LE  | OP_GE )   AddExpression)*
	void relExp()throws SyntaxException{
		addExp();
		while(t.isKind(OP_LT) || t.isKind(OP_GT) || t.isKind(OP_LE) || t.isKind(OP_GE) ){
			consume();
			addExp();
		}
	}
	
//	AddExpression ::= MultExpression   (  (OP_PLUS | OP_MINUS ) MultExpression )*
	void addExp()throws SyntaxException{
		mulExp();
		while(t.isKind(OP_PLUS)||t.isKind(OP_MINUS)){
			consume();
			mulExp();
		}
	}
	
//	MultExpression := UnaryExpression ( ( OP_TIMES | OP_DIV  | OP_MOD ) UnaryExpression )*
	void mulExp()throws SyntaxException{
		uniExp();
		while(t.isKind(OP_TIMES)||t.isKind(OP_DIV) || t.isKind(OP_MOD)){
			consume();
			uniExp();
		}
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
	void uniExp()throws SyntaxException{
		if(t.isKind(OP_PLUS) || t.isKind(OP_MINUS) ||t.isKind(OP_EXCL)){
			consume();
			uniExp();
		}
		else if(primary()){}			  //PRIMARY: return true if used else check down the path
		else if (t.isKind(IDENTIFIER)){   //IdentOrPixelSelectorExpression
			consume();
			if(t.isKind(LSQUARE)){
				consume();
				expression();
				match(COMMA);
				expression();
				match(RSQUARE);
			}
		}
		else if(isAnyKind(unary_kw_keys)){	//KW_x | KW_y | KW_r | KW_a | KW_X | KW_Y | KW_Z | KW_A | KW_R | KW_DEF_X | KW_DEF_Y
			consume();
		}
		else {
			String message = "Expected unary Expression at line:position " + t.line + ":" + t.pos_in_line;
			throw new SyntaxException(t, message);
		}
		
	}
	
//	Primary ::= INTEGER_LITERAL | LPAREN Expression RPAREN | FunctionApplication
//	FunctionApplication ::= FunctionName LPAREN Expression RPAREN  
//						 | FunctionName  LSQUARE Selector RSQUARE 
//	FunctionName ::= KW_sin | KW_cos | KW_atan | KW_abs 
//					| KW_cart_x | KW_cart_y | KW_polar_a | KW_polar_r
//	Selector ::=  Expression COMMA Expression   
	boolean primary() throws SyntaxException{	//RETURN TRUE if primary begins. else false
		if(t.isKind(INTEGER_LITERAL)){
			consume();
			return true;
		}
		else if(t.isKind(LPAREN)){
			consume();
			expression();
			match(RPAREN);
			return true;
		}
		else if(isAnyKind(fun_names)){	//function application
			consume();
			if(t.isKind(LPAREN)){
				consume();
				expression();
				match(RPAREN);
			}
			else if(t.isKind(LSQUARE)){
				consume();
//				selector();
				expression();
				match(COMMA);
				expression();
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
}
