package cop5556fa17;

import java.util.ArrayList;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.TypeUtils.Type;
import cop5556fa17.AST.ASTNode;
import cop5556fa17.AST.ASTVisitor;
import cop5556fa17.AST.Declaration;
import cop5556fa17.AST.Declaration_Image;
import cop5556fa17.AST.Declaration_SourceSink;
import cop5556fa17.AST.Declaration_Variable;
import cop5556fa17.AST.Expression;
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
import cop5556fa17.AST.Sink_Ident;
import cop5556fa17.AST.Sink_SCREEN;
import cop5556fa17.AST.Source;
import cop5556fa17.AST.Source_CommandLineParam;
import cop5556fa17.AST.Source_Ident;
import cop5556fa17.AST.Source_StringLiteral;
import cop5556fa17.AST.Statement_In;
import cop5556fa17.AST.Statement_Out;
import cop5556fa17.AST.Statement_Assign;
//import cop5556fa17.image.ImageFrame;
//import cop5556fa17.image.ImageSupport;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * All methods and variable static.
	 */


	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction

	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;
//
//	http://asm.ow2.org/asm40/javadoc/user/org/objectweb/asm/MethodVisitor.html
//    * @param opcode
//    *            the opcode of the local variable instruction to be visited.
//    *            This opcode is either ILOAD, LLOAD, FLOAD, DLOAD, ALOAD,
//    *            ISTORE, LSTORE, FSTORE, DSTORE, ASTORE or RET.
//    * @param var
//    *            the operand of the instruction to be visited. This operand is
//    *            the index of a local variable.
//	public void visitVarInsn(int opcode, int var) {

    /* @param opcode
    *            the opcode of the instruction to be visited. This opcode is
    *            either NOP, ACONST_NULL, ICONST_M1,
    *            ICONST_0, ICONST_1,ICONST_2, ICONST_3, ICONST_4, ICONST_5, LCONST_0, LCONST_1,FCONST_0, FCONST_1, FCONST_2, DCONST_0, DCONST_1,
    *            IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD, SALOAD,
    *            IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE,
    *            POP, POP2, DUP, DUP_X1, DUP_X2, DUP2, DUP2_X1,DUP2_X2, SWAP,
    *            IADD, LADD, FADD, DADD, ISUB, LSUB, FSUB, DSUB,
    *            IMUL, LMUL, FMUL, DMUL, IDIV, LDIV, FDIV, DDIV,
    *            IREM, LREM, FREM, DREM, INEG, LNEG, FNEG, DNEG,
    *            ISHL, LSHL, ISHR, LSHR, IUSHR, LUSHR,
    *            IAND, LAND, IOR, LOR, IXOR, LXOR, I2L, I2F, I2D,
    *            L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S,
    *            LCMP, FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN, FRETURN,
    *            DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW, MONITORENTER,
    *            or MONITOREXIT.
    */
//	public void visitInsn(int opcode) {

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
		className = program.name;
		classDesc = "L" + className + ";";
		String sourceFileName = (String) arg;
		cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object", null);
		cw.visitSource(sourceFileName, null);
		// create main method
		mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
		// initialize
		mv.visitCode();
		//add label before first instruction
		Label mainStart = new Label();
		mv.visitLabel(mainStart);
		// if GRADE, generates code to add string to log
//		CodeGenUtils.genLog(GRADE, mv, "entering main");
//		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);

		// visit decs and statements to add field to class
		//  and instructions to main method, respectivley
		ArrayList<ASTNode> decsAndStatements = program.decsAndStatements;
		for (ASTNode node : decsAndStatements) {
//			System.out.println("------------new node--------------");
			node.visit(this, arg);
		}

		//generates code to add string to log
//		CodeGenUtils.genLog(GRADE, mv, "leaving main");

		//adds the required (by the JVM) return statement to main
		mv.visitInsn(RETURN);

		//adds label at end of code
		Label mainEnd = new Label();
		mv.visitLabel(mainEnd);

		//handles parameters and local variables of main. Right now, only args
		mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);

		//Sets max stack size and number of local vars.
		//Because we use ClassWriter.COMPUTE_FRAMES as a parameter in the constructor,
		//asm will calculate this itself and the parameters are ignored.
		//If you have trouble with failures in this routine, it may be useful
		//to temporarily set the parameter in the ClassWriter constructor to 0.
		//The generated classfile will not be correct, but you will at least be
		//able to see what is in it.
		mv.visitMaxs(0, 0);

		//terminate construction of main method
		mv.visitEnd();

		//terminate class construction
		cw.visitEnd();

		//generate classfile as byte array and return
		return cw.toByteArray();
	}

//	VariableDeclaration ::= VarType IDENTIFIER ( OP_ASSIGN Expression | null)
//	Add field, name, as static member of class.
//	If there is an expression, generate code to evaluate it and store the results in the field.
//	See comment about this below.
	@Override
	public Object visitDeclaration_Variable(Declaration_Variable dv, Object arg) throws Exception {	//declaration_Variable
		// TODO
//		throw new UnsupportedOperationException();

//		Add field, name, as static member of class.
		cw.visitField(ACC_STATIC, dv.name, dv.getType().toString(), null, null).visitEnd();

		if(dv.e!=null){
			dv.e.visit(this, arg);	//puts value of expression on top of stack.
//			mv.visitVarInsn(ALOAD, 0);		//ours is a static variable. no need for this
//			assignmentStatement.expression.visit(this, arg);
			mv.visitFieldInsn(PUTSTATIC, className, dv.name, dv.getType().toString());
		}

		return null;
	}

	@Override
	public Object visitDeclaration_Image(Declaration_Image declaration_Image, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitDeclaration_SourceSink(Declaration_SourceSink declaration_SourceSink, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}


//	For INTEGERS and BOOLEANS, the only "sink" is the screen, so generate code to print to the console here.
//	Use java.io.PrintStream .println.  This is a virtual method, you can use the static field PrintStream "out" from class java.lang.System as the object.
	/** For Integers and booleans, the only "sink"is the screen, so generate code to print to console.
	 * For Images, load the Image onto the stack and visit the Sink which will generate the code to handle the image.
	 */
	@Override	//public Object visitPrintStatement(PrintStatement printStatement, Object arg)
	public Object visitStatement_Out(Statement_Out so, Object arg) throws Exception {	//statement_Out
		// TODO in HW5:  only INTEGER and BOOLEAN
		// TODO HW6 remaining cases
//		throw new UnsupportedOperationException();

		mv.visitFieldInsn(GETSTATIC, className, so.name, so.getDec().getType().toString());	//LEAVING A VALUE ON TOP OF STACK;
		CodeGenUtils.genLogTOS(GRADE, mv, so.getDec().getType());	//PRINT VALUE just pushed on STACK
		if(so.sink.getType()==Type.SCREEN){
			CodeGenUtils.genPrintTOS(GRADE, mv, so.getDec().getType());	//NOTE THIS IS PRINT TOS
			mv.visitInsn(POP);	//REMOVE VALUE ON STACK
		}

//		so.sink.visit(this, arg);


		return null;
	}

	/**
	 * Visit source to load rhs, which will be a String, onto the stack
	 *
	 *  In HW5, you only need to handle INTEGER and BOOLEAN
	 *  Use java.lang.Integer.parseInt or java.lang.Boolean.parseBoolean
	 *  to convert String to actual type.
	 *
	 *  TODO HW6 remaining types
	 */
//	Generate code to get value from the source and store it in variable name.
//	For Assignment 5, the only source that needs to be handled is the command line.
//
//Visit source to leave string representation of the value on top of stack
//Convert to a value of correct type:  If name.type == INTEGER generate code to invoke
//Java.lang.Integer.parseInt.   If BOOLEAN, invoke java/lang/Boolean.parseBoolean
//
//TODO:  Handling IMAGE type left for assignment 6.

	//WE DONT KNOW what to parseInt. source can be anything.
	@Override
	public Object visitStatement_In(Statement_In si, Object arg) throws Exception {//statement_In
		// TODO (see comment )
//		throw new UnsupportedOperationException();

//		mv.visitVarInsn(ALOAD, 0);
//		mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
//		mv.visitInsn(POP);
		si.source.visit(this, arg);

		if(si.getDec().getType()==Type.INTEGER){		//the variable already has a defined type.
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
//			CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);	//PRINT VALUE just pushed on STACK. NO LOGGING HERE IN ASSIGNMENT-5 
			mv.visitFieldInsn(PUTSTATIC, className, si.name, "I");	//THIS WILL BE USED WHEN VARIABLE IS ACCESSED NEXT
		}
		else if(si.getDec().getType()==Type.BOOLEAN){
			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
//			CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);	//PRINT VALUE just pushed on STACK. NO LOGGING HERE IN ASSIGNMENT-5 
			mv.visitFieldInsn(PUTSTATIC, className, si.name, "Z");
		}

		return null;
	}


	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
//	REQUIRE:  LHS.Type == Expression.Type
//	StatementAssign.isCartesian <= LHS.isCartesian

	@Override
	public Object visitStatement_Assign(Statement_Assign sa, Object arg) throws Exception {	//statement_Assign
		//TODO  (see comment)
//		throw new UnsupportedOperationException();
		sa.e.visit(this, arg);
		sa.lhs.visit(this, arg);

		return null;
	}

	// generate code to leave the two values on the stack
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}


	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
//	If LHS.Type  is INTEGER or BOOLEAN, generate code to
//    store the value on top of the stack in variable name.
//TODO Assignment 6:  handle case where LHS.Type is IMAGE
	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {
		//TODO  (see comment)
//		throw new UnsupportedOperationException();

		mv.visitFieldInsn(PUTSTATIC, className, lhs.name, lhs.getType().toString());

//		if(lhs.getType() == Type.INTEGER){
//			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, lhs.getType().toString());		//"I"
//			mv.visitFieldInsn(GETFIELD, className, lhs.name, lhs.getType().toString());		//	WHY LEAVE VARIABLE ON STACK?
//		}
//		else if(lhs.getType() == Type.BOOLEAN){
//			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, lhs.getType().toString());		//"Z"
//			mv.visitFieldInsn(GETFIELD, className, lhs.name, lhs.getType().toString());		//"Z"
//		}

		return null;
	}


	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sc, Object arg) throws Exception {	//sink_SCREEN
		//TODO HW6
		throw new UnsupportedOperationException();

//		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//		mv.visitFieldInsn(GETSTATIC, className, so.name, so.getDec().getType().toString());
//		//mv.visitInsn(SWAP);

//		String desc = "(" + so.getDec().getType().toString() + ")V";
//		System.out.print("---------------"+desc+"------------\n");
//		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", desc, false);

//		return null;
	}
	@Override
	public Object visitSink_Ident(Sink_Ident sink_Ident, Object arg) throws Exception {
		//TODO HW6
		throw new UnsupportedOperationException();
	}


	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral source_StringLiteral, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}
//	Generate code to evaluate the expression and use aaload to read the element from the command line array using the expression value as the index.
//	The command line array is the String[] args param passed to main.
//	"g <- @ 0;\n"
//	"h <- @ 1;\n"

	@Override
	public Object visitSource_CommandLineParam(Source_CommandLineParam scp, Object arg)	//	source_CommandLineParam
			throws Exception {
		// TODO
//		throw new UnsupportedOperationException();
//		https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-6.html#jvms-6.5.aaload
//		http://homepages.inf.ed.ac.uk/kwxm/JVM/aaload.html
//		mv.visitFieldInsn(GETFIELD, className, "args", "[Ljava/lang/String;");
		mv.visitVarInsn(ALOAD,0);
		scp.paramNum.visit(this, arg);	//it must be INTEGER checked in Symantic analysis: that specifies index of argument of String args[]
		mv.visitInsn(AALOAD);
//		CodeGenUtils.genLogTOS(GRADE, mv, scp.getType());
		return null;
	}
	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}
//	--------------------------------------------------------------------------------------------------------------------------
//	For each expression kind, generate code to leave the value of the expression on top of the stack.

//	Generate code to evaluate the expression and leave the value on top of the stack.
//	Visiting the nodes for Expression0 and Expression1  will generate code to leave those values on the stack.Then just generate code to perform the op.
	@Override
	public Object visitExpression_Binary(Expression_Binary eb, Object arg) throws Exception {		//expression_Binary
		// TODO
//		throw new UnsupportedOperationException();

		//CASE: PUSH BOOL(0/1) RESULT ON STACK
//				eb.op == Kind.OP_EQ ||eb.op == Kind.OP_NEQ
//				eb.op == Kind.OP_GE || eb.op == Kind.OP_GT || eb.op == Kind.OP_LT||eb.op == Kind.OP_LE
		if(eb.op == Kind.OP_EQ ||eb.op == Kind.OP_NEQ||eb.op == Kind.OP_GE || eb.op == Kind.OP_GT || eb.op == Kind.OP_LT||eb.op == Kind.OP_LE ){
			eb.e0.visit(this,arg);
			eb.e1.visit(this,arg);
			Label le1 = new Label();
			switch(eb.op){
				case OP_EQ:
					mv.visitJumpInsn(IF_ICMPNE, le1);
					break;
				case OP_NEQ:
					mv.visitJumpInsn(IF_ICMPEQ, le1);
					break;
				case OP_GE:
					mv.visitJumpInsn(IF_ICMPLT, le1);
					break;
				case OP_GT:
					mv.visitJumpInsn(IF_ICMPLE, le1);
					break;
				case OP_LT:
					mv.visitJumpInsn(IF_ICMPGE, le1);
					break;
				case OP_LE:
					mv.visitJumpInsn(IF_ICMPGT, le1);
					break;
			}
			mv.visitInsn(ICONST_1);
			Label le2 = new Label();
			mv.visitJumpInsn(GOTO, le2);
			mv.visitLabel(le1);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(le2);
		}
		//	(eb.op == Kind.OP_AND || eb.op == Kind.OP_OR)
		else if(eb.op == Kind.OP_AND){
			eb.e0.visit(this,arg);
			eb.e1.visit(this,arg);
			mv.visitInsn(IAND);
		}
		else if(eb.op == Kind.OP_OR){
			eb.e0.visit(this,arg);
			eb.e1.visit(this,arg);
			mv.visitInsn(IOR);
		}
		//CASE: PUSH INT RESULT ON STACK
		else if(eb.op == Kind.OP_DIV ||eb.op == Kind.OP_MINUS || eb.op == Kind.OP_MOD ||eb.op == Kind.OP_PLUS ||eb.op == Kind.OP_POWER ||eb.op == Kind.OP_TIMES){
			//IF both integer
			eb.e0.visit(this,arg);
			eb.e1.visit(this,arg);
			switch(eb.op) {	//OP_POWER is left to be implemented
				case OP_PLUS:
					mv.visitInsn(IADD);
					break;
				case OP_MINUS:
					mv.visitInsn(ISUB);
					break;
				case OP_TIMES:
					mv.visitInsn(IMUL);
					break;
				case OP_DIV:
					mv.visitInsn(IDIV);
					break;
				case OP_MOD:
					mv.visitInsn(IREM);
			}
		}


//		CodeGenUtils.genLogTOS(GRADE, mv, eb.getType());
		return null;
	}

//	Generate code to evaluate the unary expression and leave its value on top of the stack.
//    Which code is generated will depend on the operator.  If the op is OP_PLUS, the value
//that should be left on the stack is just the value of Expression
	@Override
	public Object visitExpression_Unary(Expression_Unary eu, Object arg) throws Exception {	//expression_Unary
		// TODO
//		throw new UnsupportedOperationException();

		eu.e.visit(this, arg);	//leaves value on top of stack
		if(eu.op==Kind.OP_EXCL)
		{
			if(eu.getType()==Type.BOOLEAN){
				Label l1 = new Label();
				mv.visitJumpInsn(IFEQ, l1);
				mv.visitInsn(ICONST_0);
				Label l2 = new Label();
				mv.visitJumpInsn(GOTO, l2);
				mv.visitLabel(l1);
				mv.visitInsn(ICONST_1);
				mv.visitLabel(l2);
			}
			else if(eu.getType() == Type.INTEGER){
				mv.visitLdcInsn(Integer.MAX_VALUE);
				mv.visitInsn(IXOR);
			}
		}
		else if(eu.op==Kind.OP_MINUS){
			mv.visitInsn(INEG);
		}

//		CodeGenUtils.genLogTOS(GRADE, mv, eu.e.getType());
		return null;
	}



	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector expression_PixelSelector, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

//	Generate code to evaluate the Expressioncondition and depending on its
//    Value, to leave the value of either Expressiontrue  or Expressionfalse on top of the stack.
//    Hint:  you will need to use labels,  a conditional instruction, and goto.
//	public final Expression condition;
//	public final Expression trueExpression;
//	public final Expression falseExpression;
	@Override
	public Object visitExpression_Conditional(Expression_Conditional ec, Object arg)	//expression_Conditional
			throws Exception {
		// TODO
//		throw new UnsupportedOperationException();
		ec.condition.visit(this, arg);	//leaves value of expr on top of stack
		Label if_false = new Label();
		Label if_true = new Label();

		mv.visitJumpInsn(IFEQ, if_false);	//consumes value on top stack
		ec.trueExpression.visit(this, arg);
		mv.visitJumpInsn(GOTO, if_true);	//Does not consume value on top stack

		mv.visitLabel(if_false);
		ec.falseExpression.visit(this, arg);

		mv.visitLabel(if_true);

//  THIS WAS TO BE LOGGED IN ASSIGNMENT 5 ALSO//		CodeGenUtils.genLogTOS(GRADE, mv, ec.trueExpression.getType());	//CHECK WHAT WILL COME HERE
		return null;
	}

	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg expression_FunctionAppWithExprArg, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg expression_FunctionAppWithIndexArg, Object arg) throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName expression_PredefinedName, Object arg)
			throws Exception {
		// TODO HW6
		throw new UnsupportedOperationException();
	}


//	Generate code to leave the value of the literal on top of the stack
	@Override
	public Object visitExpression_BooleanLit(Expression_BooleanLit expression_BooleanLit, Object arg) throws Exception {
		//TODO
//		throw new UnsupportedOperationException();

		if (expression_BooleanLit.value == true) {
			mv.visitInsn(ICONST_1);
		} else {
			mv.visitInsn(ICONST_0);
		}

//		CodeGenUtils.genLogTOS(GRADE, mv, Type.BOOLEAN);
		return null;
	}
//	Generate code to leave constant on stack
	@Override
	public Object visitExpression_IntLit(Expression_IntLit expression_IntLit, Object arg) throws Exception {
		// TODO
//		throw new UnsupportedOperationException();
		mv.visitLdcInsn(expression_IntLit.value);

//		CodeGenUtils.genLogTOS(GRADE, mv, Type.INTEGER);
		return null;
	}
//	Generate code to get the value of the variable and leave it on top of the stack.
	@Override
	public Object visitExpression_Ident(Expression_Ident ei,	//expression_Ident
			Object arg) throws Exception {
		//TODO
//		throw new UnsupportedOperationException();

//		mv.visitLdcInsn(expression_Ident.name);

		mv.visitFieldInsn(GETSTATIC, className, ei.name, ei.getType().toString());	//CHECK
//		String varName = ei.name;
//		String varType = ei.getType();
//		mv.visitVarInsn(ALOAD, 0);
//		if (varType.equals(intType) || varType.equals(booleanType) || varType.equals(stringType)){
//			mv.visitFieldInsn(GETFIELD, className, varName, varType);
//		}
//
//		CodeGenUtils.genLogTOS(GRADE, mv, ei.getType());
		return null;
	}

//	@Override
//	public Object visitIdentExpression(IdentExpression identExpression,
//			Object arg) throws Exception {
//		String varName = identExpression.identToken.getText();
//		String varType = identExpression.getType();
//		MethodVisitor mv = ((InheritedAttributes) arg).mv;
//		mv.visitVarInsn(ALOAD, 0);
//		if (varType.equals(intType) || varType.equals(booleanType) || varType.equals(stringType)){
//			mv.visitFieldInsn(GETFIELD, className, varName, varType);
//		} else if (varType.substring(0, varType.indexOf("<")).equals("Ljava/util/List")) {
//			mv.visitFieldInsn(GETFIELD, className, varName, "Ljava/util/List;");
//		}
//
//		return null;
//	}
}
