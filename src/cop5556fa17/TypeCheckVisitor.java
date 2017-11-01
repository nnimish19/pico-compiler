package cop5556fa17;

import java.net.*;
import static cop5556fa17.Scanner.Kind.KW_boolean;
import static cop5556fa17.Scanner.Kind.KW_int;
import cop5556fa17.AST.*;

import java.util.HashMap;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression_Binary;
import cop5556fa17.AST.Expression_BooleanLit;
import cop5556fa17.AST.Expression_Conditional;
import cop5556fa17.AST.Expression_FunctionAppWithExprArg;
import cop5556fa17.AST.Expression_FunctionAppWithIndexArg;
import cop5556fa17.AST.Expression_Ident;
import cop5556fa17.AST.Expression_IntLit;
import cop5556fa17.AST.Expression_PixelSelector;
import cop5556fa17.AST.Expression_PredefinedName;
import cop5556fa17.AST.Expression_Unary;
import cop5556fa17.AST.Index;
import cop5556fa17.AST.LHS;
import cop5556fa17.AST.Program;
import cop5556fa17.AST.Sink;
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_Assign;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;

public class TypeCheckVisitor implements ASTVisitor {
	

		@SuppressWarnings("serial")
		public static class SemanticException extends Exception {
			Token t;

			public SemanticException(Token t, String message) {
				super("line " + t.line + " pos " + t.pos_in_line + ": "+  message);
				this.t = t;
			}

		}		
		

	HashMap<String, Declaration> symTab = new HashMap<String, Declaration>();
	
	
	/**
	 * The program name is only used for naming the class.  It does not rule out
	 * variables with the same name.  It is returned for convenience.
	 * 
	 * @throws Exception 
	 */
	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		for (ASTNode node: program.decsAndStatements) {
			node.visit(this, arg);
		}
		return program.name;
	}

//	Declaration_Variable ::=  Type name (Expression | null )
//			REQUIRE:  symbolTable.lookupType(name) = $
//		              symbolTable.insert(name, Declaration_Variable)
//			Declaration_Variable.Type <= Type
//		               REQUIRE if (Expression !=  null) Declaration_Variable.Type == Expression.Type
	@Override
	public Object visitDeclaration_Variable(
			Declaration_Variable dv, Object arg)	//declaration_Variable
			throws Exception {
		
//		if(dv==null)System.out.println("---------------------");
//		System.out.println(dv);
		if(symTab.containsKey(dv.name)){
			throw new SemanticException(dv.firstToken, "Error: Variable re-declared");
		}
		
		symTab.put(dv.name, dv);
		
		Type t= TypeUtils.getType(dv.type);		//		throw error if type not found
		if(t == null){
			throw new SemanticException(dv.firstToken, "Error: visitDeclaration_Variable.Type not found in TypeUtils");
		}
		dv.setType(t);
		
													//if(t.isKind(KW_int)||t.isKind(KW_boolean)){
		if(dv.e!=null){	
			dv.e.visit(this, arg);			//CHECK
			if(dv.getType() != dv.e.getType()){
				throw new SemanticException(dv.firstToken, "Error: visitDeclaration_Variable: Type mismatch");
			}
		}
		
		return null;
	}

	
//	Declaration_Image  ::= name (  xSize ySize | null) Source
//			REQUIRE:  symbolTable.lookupType(name) = $
//		    symbolTable.insert(name, Declaration_Image)
//			Declaration_Image.Type <= IMAGE   
//		               REQUIRE if xSize != null then ySize != null && xSize.Type == INTEGER && ySize.type == INTEGER
	@Override
	public Object visitDeclaration_Image(Declaration_Image di,	//declaration_Image
			Object arg) throws Exception {
		
		if(symTab.containsKey(di.name)){
			throw new SemanticException(di.firstToken, "Error: Variable re-declared");
		}
		
		symTab.put(di.name, di);
		di.setType(TypeUtils.Type.IMAGE);
		
		if(di.xSize!=null && di.ySize!=null){
			di.xSize.visit(this, arg);
			di.ySize.visit(this, arg);
			if(di.xSize.getType()!=Type.INTEGER || di.ySize.getType()!=Type.INTEGER){
				throw new SemanticException(di.firstToken, "Error: Declaration_Image: xSize or ySize type not integer");
			}
		}
		else if(di.xSize!=null || di.ySize!=null){
			throw new SemanticException(di.firstToken, "Error: Declaration_Image: xSize or ySize null");
		}
		return null;
	}
	
	
//	Declaration_SourceSink  ::= Type name  Source
//          	REQUIRE:  symbolTable.lookupType(name) = $
//           symbolTable.insert(name, Declaration_SourceSink)
//Declaration_SourceSink.Type <= Type
//           REQUIRE Source.Type == Declaration_SourceSink.Type
	@Override
	public Object visitDeclaration_SourceSink(
			Declaration_SourceSink ds, Object arg)	//declaration_SourceSink
			throws Exception {
		
		if(symTab.containsKey(ds.name)){
			throw new SemanticException(ds.firstToken, "Error: Variable re-declared");
		}
		
		symTab.put(ds.name, ds);		//KW_url | KW_file
		
		Type t= TypeUtils.getType(ds.type);		//		throw error if type not found
		if(t == null){
			throw new SemanticException(ds.firstToken, "Error: Declaration_SourceSink.Type not found in TypeUtils");
		}
		ds.setType(t);
		
		if(ds.source==null)
			throw new SemanticException(ds.firstToken, "Error: Declaration_SourceSink.source should not be null");
		
		ds.source.visit(this, arg);
		if(ds.source.getType()!=ds.getType())
			throw new SemanticException(ds.firstToken, "Error: Declaration_SourceSink.Type != Source.Type");
		
		return null;
	}
	
//	---------------------------------------------------------------
//	Statement_Out 
//		public final String name;
//		public final Sink sink;
//	Statement_Out ::= name Sink		//variables
//	Statement_Out.Declaration <= name.Declaration
//		              REQUIRE:  (name.Declaration != null)
//		              REQUIRE:   ((name.Type == INTEGER || name.Type == BOOLEAN) && Sink.Type == SCREEN)
//			                  ||  (name.Type == IMAGE && (Sink.Type ==FILE || Sink.Type == SCREEN))
	@Override
	public Object visitStatement_Out(Statement_Out so, Object arg)	//statement_Out
			throws Exception {
		
		Declaration dec = symTab.get(so.name);
		
		if(so.sink==null)
			throw new SemanticException(so.firstToken, "Error: visitStatement_Out: Sink not defined");
		
		so.sink.visit(this, arg);	//CHECK: which method should be visited. there are 2 types of sinks
		if(	dec!=null && (
//			((dec.getType()==TypeUtils.Type.INTEGER || dec.getType()==TypeUtils.Type.BOOLEAN) && TypeUtils.getType(so.sink.firstToken)==TypeUtils.Type.SCREEN)
			((dec.getType()==Type.INTEGER || dec.getType()==Type.BOOLEAN) && so.sink.getType()==Type.SCREEN)
			|| (dec.getType()==Type.IMAGE && (so.sink.getType() ==Type.FILE  || so.sink.getType() == Type.SCREEN))
		)){
			so.setDec(dec);
		}
		else {
			throw new SemanticException(so.firstToken, "Error: Either Variable is not declared or incompatible assignment");
		}
		return null;
	}

//	Statement_In ::= name Source		//Variable stored:
//		Statement_In.Declaration <= name.Declaration
//		REQUIRE:  (name.Declaration != null) & (name.type == Source.type)
	@Override
	public Object visitStatement_In(Statement_In si, Object arg)	//statement_In
			throws Exception {
		
		Declaration dec = symTab.get(si.name);
		
		if(si.source==null)
			throw new SemanticException(si.firstToken, "Error: visitStatement_In: Source not defined");
		
		si.source.visit(this, arg);
		if(dec!=null && dec.getType()==si.source.getType()){
			si.setDec(dec);
		}
		else{
			throw new SemanticException(si.firstToken, "Error: Either Variable is not declared or incompatible assignment");
		}
		
		return null;
	}

//	Statement_Assign ::=  LHS  Expression
//			REQUIRE:  LHS.Type == Expression.Type
//			StatementAssign.isCartesian <= LHS.isCartesian
	@Override
	public Object visitStatement_Assign(Statement_Assign sa,	//statement_Assign
			Object arg) throws Exception {
		
		if(sa.lhs==null || sa.e==null){
			throw new SemanticException(sa.firstToken, "Error: visitStatement_Assign: LHS or exp is null");
		}
		
		sa.lhs.visit(this, arg);
		sa.e.visit(this, arg);
		if(sa.lhs.getType() == sa.e.getType()){
			sa.setCartesian(sa.lhs.isCartesian());
		}
		else{
			throw new SemanticException(sa.firstToken, "Error: visitStatement_Assign: LHS.type != exp.type");
		}
		return null;
	}


//	Index ::= Expression0 Expression1
//	REQUIRE: Expression0.Type == INTEGER &&  Expression1.Type == INTEGER
//	Index.isCartesian <= !(Expression0 == KW_r && Expression1 == KW_a)
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		
		if(index.e0==null || index.e1==null)
			throw new SemanticException(index.firstToken, "Error:visitIndex: null expression");
//			return null;
//			
		index.e0.visit(this, arg);	//CHECK null
		index.e1.visit(this, arg);
		if(index.e0.getType()==Type.INTEGER && index.e1.getType()==Type.INTEGER){
			try{
				Expression_PredefinedName exp0= (Expression_PredefinedName)index.e0;
				Expression_PredefinedName exp1= (Expression_PredefinedName)index.e1;
				index.setCartesian(!(exp0.kind==Kind.KW_r&& exp1.kind==Kind.KW_a));	
			}catch(Exception e){
				throw new SemanticException(index.firstToken, e.getMessage());//"Error: Expected index.expression to be Expression_PredefinedName");
			}
		}
		else{
			throw new SemanticException(index.firstToken, "Error: Expected index expression type as Integer");
		}
		
		return null;
	}
	
	
//	LHS ::= name Index
//	LHS.Declaration <= symbolTable.lookupDec(name)
//              LHS.Type <= LHS.Declaration.Type
//              LHS.isCarteisan <= Index.isCartesian
	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
	
		Declaration dec = symTab.get(lhs.name);
		if(dec==null){
			throw new SemanticException(lhs.firstToken, "Error: visitLHS: no declaration found for the identifier");
		}
		
		lhs.setDec(dec);
		lhs.setType(lhs.getDec().getType());
		
		if(lhs.index!=null){							//do we need to throw exception or handle if statement. I guess just handle if. bcz index can be null
			lhs.index.visit(this, arg);
			lhs.setCartesian(lhs.index.isCartesian());
		}
		return null;
	}

	
//	Sink ::= Sink_Ident | Sink_SCREEN
//	Sink.Type <= Sink_X.Type
	
//	Sink_SCREEN ::= SCREEN
//	Sink_SCREEN.Type <= SCREEN
	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sink_SCREEN, Object arg)
			throws Exception {
		
		sink_SCREEN.setType(Type.SCREEN);
		return null;
	}

//	Sink_Ident ::= name
//		Sink_Ident.Type <= symbolTable.lookupType(name) 
//  	REQUIRE:  Sink_Ident.Type  == FILE
	@Override
	public Object visitSink_Ident(Sink_Ident si, Object arg)	//sink_Ident
			throws Exception {
		
		Declaration dec = symTab.get(si.name);
		if(dec==null||dec.getType()!=Type.FILE)	{
			throw new SemanticException(si.firstToken, "Error: visitSink_Ident: un-expected declaration");
		}
		si.setType(dec.getType());
		return null;
	}
	
//	Source ::= Source_CommandLineParam  | Source_Ident | Source_StringLiteral
	
//	Source_StringLiteral ::=  fileOrURL
//	Source_StringLIteral.Type <= if isValidURL(fileOrURL) then URL else FILE	
	@Override
	public Object visitSource_StringLiteral(
			Source_StringLiteral ssl, Object arg)	//source_StringLiteral
			throws Exception {

		try{
			URL url = new URL(ssl.fileOrUrl);
			ssl.setType(Type.URL);
		}catch(Exception e){
			ssl.setType(Type.FILE);
		}
		return null;
	}

//	Source_CommandLineParam  ::= ExpressionparamNum
//			Source_CommandLineParam .Type <= ExpressionparamNum.Type
//			REQUIRE:  Source_CommandLineParam .Type == INTEGER
	@Override
	public Object visitSource_CommandLineParam(
			Source_CommandLineParam clp, Object arg)	//source_CommandLineParam
			throws Exception {
		
		if(clp.paramNum==null){
			throw new SemanticException(clp.firstToken, "Error: visitSource_CommandLineParam: paramNum is null");
		}
		clp.paramNum.visit(this, arg);
		if(clp.paramNum.getType()!=Type.INTEGER){
			throw new SemanticException(clp.firstToken, "Error: Expected integer parameter");
		}
		clp.setType(clp.paramNum.getType());	
//		}
		return null;
	}

//	Source_Ident ::= name
//	Source_Ident.Type <= symbolTable.lookupType(name)
//	REQUIRE:  Source_Ident.Type == FILE || Source_Ident.Type == URL
	@Override
	public Object visitSource_Ident(Source_Ident si, Object arg)	//source_Ident
			throws Exception {
		
		Declaration dec= symTab.get(si.name);
		if(dec!=null && (dec.getType()==Type.FILE||dec.getType()==Type.URL)){
			si.setType(dec.getType());
		}
		else{
			throw new SemanticException(si.firstToken, "Error: Expected declaration to be FILE or URL");
		}
		
		return null;
	}

	
//---------------------------------------------------------------
//	Expression_Binary ::= Expression0 op Expression1
//	REQUIRE:  Expression0.Type == Expression1.Type  && Expression_Binary.Type ≠ $
//	Expression_Binary.type <=   
//		 if op ∈ {EQ, NEQ} then BOOLEAN
//		 else if (op ∈ {GE, GT, LT, LE} && Expression0.Type == INTEGER) then BOOLEAN
//		 else if (op ∈ {AND, OR}) && (Expression0.Type == INTEGER || Expression0.Type ==BOOLEAN) 
//			then Expression0.Type
//		 else if op ∈ {DIV, MINUS, MOD, PLUS, POWER, TIMES} && Expression0.Type == INTEGER
//			then INTEGER
//		else $
	@Override
	public Object visitExpression_Binary(Expression_Binary eb,	//expression_Binary
			Object arg) throws Exception {

		if(eb.e0==null || eb.e1==null){
			throw new SemanticException(eb.firstToken, "Error: visitExpression_Binary: 1st or 2nd expression is null");
		}
		eb.e0.visit(this, arg);	//CHECK null
		eb.e1.visit(this, arg);
		
		if(eb.e0.getType() != eb.e1.getType()) 
			throw new SemanticException(eb.firstToken, "Error: Expected same type for binary exp");
		
		if(eb.op == Kind.OP_EQ ||eb.op == Kind.OP_NEQ){
			eb.setType(Type.BOOLEAN);
		}
		else if((eb.op == Kind.OP_GE || eb.op == Kind.OP_GT || eb.op == Kind.OP_LT||eb.op == Kind.OP_LE ) && eb.e0.getType() == Type.INTEGER){
			eb.setType(Type.BOOLEAN);
		}
		else if((eb.op == Kind.OP_AND || eb.op == Kind.OP_OR) && (eb.e0.getType()==Type.BOOLEAN || eb.e0.getType()==Type.INTEGER)){
			eb.setType(eb.e0.getType());
		}
		else if((eb.op == Kind.OP_DIV ||eb.op == Kind.OP_MINUS ||eb.op == Kind.OP_PLUS ||eb.op == Kind.OP_POWER ||eb.op == Kind.OP_TIMES)&&(eb.e0.getType()==Type.INTEGER)){
			eb.setType(eb.e0.getType());
		}
		else{
			throw new SemanticException(eb.firstToken, "Error: invalid binary expression");
		}
		return null;
		
//		int val0=0,val1,val2;
//		val1= (Integer)expression_Binary.e0.visit(this, null);
//		val2= (Integer)expression_Binary.e1.visit(this, null);
//		switch(expression_Binary.op){
//			case OP_PLUS: val0=val1+val2;break;
//			case OP_MINUS: val0=val1-val2;break;
//			case OP_TIMES: val0=val1*val2;break;
//			case OP_DIV: val0=val1/val2;break;		//if val2==0 throw semantic error
//			case OP_MOD: val0=val1%val2;break;		//if val2==0 throw semantic error
//		}
//		expression_Binary.val = new Integer(val0);
//		return expression_Binary.val;
	}

//	Expression_Unary ::= op Expression
//			Expression_Unary.Type <=
//				let t = Expression.Type in 
//		            if op ∈ {EXCL} && (t == BOOLEAN || t == INTEGER) then t
//		            else if op {PLUS, MINUS} && t == INTEGER then INTEGER
//				    else $
//		    REQUIRE:  Expression_ Unary.Type ≠ $   
	@Override
	public Object visitExpression_Unary(Expression_Unary eu,	//expression_Unary
			Object arg) throws Exception {
		
		if(eu.e==null)
			throw new SemanticException(eu.firstToken, "Error: visitExpression_Unary: expression null");
		
		eu.e.visit(this, arg);	//CHECK null
		Type t = eu.e.getType();
		if(eu.op==Kind.OP_EXCL &&(t==Type.BOOLEAN || t==Type.INTEGER)){
			eu.setType(t);
		}
		else if((eu.op==Kind.OP_PLUS|| eu.op==Kind.OP_MINUS) && t==Type.INTEGER){
			eu.setType(Type.INTEGER);
		}
		else{
			throw new SemanticException(eu.firstToken, "Error: invalid unary expression");
		}
		return null;
	}


//	Expression_PixelSelector ::=   name Index
//         	name.Type <= SymbolTable.lookupType(name)
//	Expression_PixelSelector.Type <=  if name.Type == IMAGE then INTEGER 
//                                    else if Index == null then name.Type
//                                    else  $
//              REQUIRE:  Expression_PixelSelector.Type ≠ $
	@Override
	public Object visitExpression_PixelSelector(
			Expression_PixelSelector eps, Object arg)	//expression_PixelSelector
			throws Exception {
		
		Declaration dec= symTab.get(eps.name);
		if(dec==null){
			throw new SemanticException(eps.firstToken, "Error: variable declaration not found");
		}
		if(dec.getType()==Type.IMAGE)
			eps.setType(Type.INTEGER);
		else if(eps.index==null)
			eps.setType(dec.getType());
		else
			throw new SemanticException(eps.firstToken, "Error: invalid expression for pixel selector");
		return null;
	}

	
//	Expression_Conditional ::=  Expressioncondition Expressiontrue Expressionfalse
//			REQUIRE:  Expressioncondition.Type == BOOLEAN &&  Expressiontrue.Type ==Expressionfalse.Type
//			Expression_Conditional.Type <= Expressiontrue.Type
	@Override
	public Object visitExpression_Conditional(
			Expression_Conditional ec, Object arg)	//expression_Conditional
			throws Exception {
		
		if(ec.condition==null || ec.trueExpression==null|| ec.falseExpression==null)
			throw new SemanticException(ec.firstToken, "Error: visitExpression_Conditional: expression is null");
		
		ec.condition.visit(this, arg);	//CHECK null
		ec.trueExpression.visit(this, arg);
		ec.falseExpression.visit(this, arg);
		if(ec.condition.getType()==Type.BOOLEAN && ec.trueExpression.getType()== ec.falseExpression.getType()){
			ec.setType(ec.trueExpression.getType());
		}
		else{
			throw new SemanticException(ec.firstToken, "Error: invalid Expression_Conditional");
		}
		
		return null;
	}

//	Expression_FunctionApp  ::= Expression_FunctionAppWithExprArg | Expression_FunctionAppWithIndexArg 
//	Expression_FunctionApp.Type <= Expression_FunctionAppWithXArg.Type

//	Expression_FunctionAppWithExprArg ::=  function Expression
//		REQUIRE:  Expression.Type == INTEGER
//		Expression_FunctionAppWithExprArg.Type <= INTEGER
	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg exparg,		//expression_FunctionAppWithExprArg
			Object arg) throws Exception {
		
		exparg.setType(Type.INTEGER);
		
		if(exparg.arg==null){
			throw new SemanticException(exparg.firstToken, "Error: Expression_FunctionAppWithExprArg: arg expression null");
		}
		
		exparg.arg.visit(this, arg);
		if(exparg.arg.getType() != Type.INTEGER){
			throw new SemanticException(exparg.firstToken, "Error: Expression_FunctionAppWithExprArg: invalid arg type");
		}
		return null;
	}

//	Expression_FunctionAppWithIndexArg ::=   function Index
//      Expression_FunctionAppWithIndexArg.Type <= INTEGER
	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg indarg,	//expression_FunctionAppWithIndexArg
			Object arg) throws Exception {
		
		indarg.setType(Type.INTEGER);
		
		return null;
	}

//	Expression_PredefinedName ::=  predefNameKind
//		Expression_PredefinedName.TYPE <= INTEGER
	@Override
	public Object visitExpression_PredefinedName(
			Expression_PredefinedName epn, Object arg)	//expression_PredefinedName
			throws Exception {
		
		epn.setType(Type.INTEGER);
		
		return null;
	}


//	Expression_BooleanLit ::=  value
//		Expression_BooleanLit.Type <= BOOLEAN
	@Override
	public Object visitExpression_BooleanLit(
			Expression_BooleanLit expression_BooleanLit, Object arg)
			throws Exception {
		
		expression_BooleanLit.setType(Type.BOOLEAN);
		return null;
	}
//	Expression_IntLit ::=  value
//		Expression_IntLIt.Type <= INTEGER
	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit,
			Object arg) throws Exception {
//		return expression_IntLit.val = expression_IntLit.value;
		
		expression_IntLit.setType(Type.INTEGER);
		return null;
	}
//	Expression_Ident  ::=   name
//		Expression_Ident.Type <= symbolTable.lookupType(name)
	@Override
	public Object visitExpression_Ident(Expression_Ident expression_Ident,
			Object arg) throws Exception {
		
		try{
			expression_Ident.setType(symTab.get(expression_Ident.name).getType());
		}
		catch(Exception e){
			throw new SemanticException(expression_Ident.firstToken, e.getMessage());	//if any instance is null but accessed
		}
		return null;
	}

}
