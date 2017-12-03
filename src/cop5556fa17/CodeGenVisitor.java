package cop5556fa17;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
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
import cop5556fa17.ImageFrame;
import cop5556fa17.ImageSupport;

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
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 1);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 2);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 3);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 4);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 5);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 6);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 7);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 8);
		
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
		mv.visitLocalVariable("x", "I", null, mainStart, mainEnd, 1);
		mv.visitLocalVariable("y", "I", null, mainStart, mainEnd, 2);
		mv.visitLocalVariable("X", "I", null, mainStart, mainEnd, 3);
		mv.visitLocalVariable("Y", "I", null, mainStart, mainEnd, 4);
		mv.visitLocalVariable("r", "I", null, mainStart, mainEnd, 5);
		mv.visitLocalVariable("a", "I", null, mainStart, mainEnd, 6);
		mv.visitLocalVariable("R", "I", null, mainStart, mainEnd, 7);
		mv.visitLocalVariable("A", "I", null, mainStart, mainEnd, 8);
		mv.visitLocalVariable("Z", "I", null, mainStart, mainEnd, 9);
		mv.visitLocalVariable("tmp", "I", null, mainStart, mainEnd, 10);
		mv.visitLocalVariable("tmps", "Ljava/lang/String;", null, mainStart, mainEnd, 11);

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

//	1.Add a field to the class with type java.awt.image.BufferedImage.  
//	2.If there is a source, visit the AST node to load the String containing the URL of file name onto the stack.  
//	3.Use the cop5556fa17.ImageSupport.readImage method to read the image.	
		//public static BufferedImage readImage(String source, Integer X, Integer Y) {
		//return type is BufferedImage = java.awt.image
//	4.If no index is given pass null for the xSize and ySize (called X and Y elsewhere)  parameters, 
//	otherwise visit the index to leave the values on top of the stack.  
//	These are ints, use java.lang.Integer.valueOf to convert to Integer.
	
//	If no source is given use the makeImage method to create an image.  
//	If no size is given use values of the predefined constants def_X and def_Y.
//	Store the image reference in the field.****
	
//	public final Expression xSize;
//	public final Expression ySize;
//	public final String name;
//	public final Source source;
//	"\nimage[512,512] g; \n"
//	"\nimage g; \n"

	public static final String ImageClassName = "java/awt/image/BufferedImage";
	public static final String ImageDesc = "Ljava/awt/image/BufferedImage;";//TypeUtils.java
	@Override
	public Object visitDeclaration_Image(Declaration_Image di, Object arg) throws Exception {	//declaration_Image
		// TODO HW6
//		throw new UnsupportedOperationException();
		cw.visitField(ACC_STATIC, di.name, di.getType().toString(), null, null).visitEnd();	//di.getType().toString() = imageType

		//Both xsize and ysize if present are integers. note we have already computed their types. 
		//TypeCheckVisitor: if(di.xSize.getType()!=Type.INTEGER || di.ySize.getType()!=Type.INTEGER){ throw error
		if(di.xSize!=null && di.ySize!=null) {
			di.xSize.visit(this, arg);	//put on stack	//store this in image property? This is automaticaly handled by makeImage
			mv.visitVarInsn(ISTORE, 3);	//put in X
			di.ySize.visit(this, arg);	//put on stack
			mv.visitVarInsn(ISTORE, 4);	//put in Y
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "makeImage", "(II)Ljava/awt/image/BufferedImage;", false);
			mv.visitFieldInsn(PUTSTATIC, className, di.name, di.getType().toString());		//CHECK:"Ljava/awt/image/BufferedImage;" 
		}
		else if(di.xSize==null) {
			mv.visitLdcInsn(256);	//def_X = 256 default
			mv.visitVarInsn(ISTORE, 3);	//put in X
			mv.visitLdcInsn(256);	//def_Y = 256 default
			mv.visitVarInsn(ISTORE, 4);	//put in Y
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "makeImage", "(II)Ljava/awt/image/BufferedImage;", false);
			mv.visitFieldInsn(PUTSTATIC, className, di.name, di.getType().toString());		//CHECK:"Ljava/awt/image/BufferedImage;"
		}
		
		if(di.source!=null){
			di.source.visit(this, arg);	//place string containing URL on top of stack

//			TODO
			if(di.xSize==null) {	//load original image.
				mv.visitInsn(ACONST_NULL);
				mv.visitInsn(ACONST_NULL);
//				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readFromFile", "(Ljava/lang/String;)Ljava/awt/image/BufferedImage;", false);
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readImage", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/awt/image/BufferedImage;", false);
				mv.visitFieldInsn(PUTSTATIC, className, di.name, "Ljava/awt/image/BufferedImage;");	
			}
			else {
//				BufferedImage refImage0 = ImageSupport.readImage(imageFile1, 128, 128);
//				string is already on top of stack. now push the size X, Y.
				mv.visitFieldInsn(GETSTATIC, className, di.name, "Ljava/awt/image/BufferedImage;");
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getX", "(Ljava/awt/image/BufferedImage;)I", false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
				
				mv.visitFieldInsn(GETSTATIC, className, di.name, "Ljava/awt/image/BufferedImage;");
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getY", "(Ljava/awt/image/BufferedImage;)I", false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
//				NOTE: We have converted the "I" type to "Ljava/lang/Integer;" before calling!!
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readImage", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/awt/image/BufferedImage;", false);
				mv.visitFieldInsn(PUTSTATIC, className, di.name, "Ljava/awt/image/BufferedImage;");
			}
		}		
		
		return null;
	}

//	Add a field to the class with the given name.  
//	If there is a Source, visit it to generate code to leave a String describing the sport on top of the stack and 
//	then write it to the field.
	
//	public final String name;
//	public final Source source;
//	KW_url url1 = "http://"		Type: url
//	KW_file file1 = "Users/nj/,,"	Type: file
	@Override		//write it to the field.  
	public Object visitDeclaration_SourceSink(Declaration_SourceSink ds, Object arg)	//declaration_SourceSink
			throws Exception {
		// TODO HW6
//		throw new UnsupportedOperationException();
		cw.visitField(ACC_STATIC, ds.name, ds.getType().toString(), null, null).visitEnd();	//ds.getType().toString() = ImageDesc
		if(ds.source!=null){
			ds.source.visit(this, arg);	//place value on top of stack: Ljava/lang/String;
			mv.visitFieldInsn(PUTSTATIC, className, ds.name, ds.getType().toString());
//			mv.visitVarInsn(ASTORE, 11);
//			if(ds.getType()==Type.URL) {		//https://stackoverflow.com/questions/15842239/how-to-cast-a-string-to-an-url-in-java
////				URL file = new URL(imageFile1);
//				mv.visitTypeInsn(NEW, "java/net/URL");
//				mv.visitInsn(DUP);
////				mv.visitLdcInsn("/Users/nj/UFL/plp/eclipse_workspace/PLPHomework/src/cop5556fa17/ShelterPoint.jpg");
//				mv.visitVarInsn(ALOAD, 11);
//				mv.visitMethodInsn(INVOKESPECIAL, "java/net/URL", "<init>", "(Ljava/lang/String;)V", false);
////				mv.visitVarInsn(ASTORE, 5);
//				mv.visitFieldInsn(PUTSTATIC, className, ds.name, ds.getType().toString());
//			}else {		//https://stackoverflow.com/questions/11583364/convert-string-to-file-in-java
////				File file = new File(imageFile1);
//				mv.visitTypeInsn(NEW, "java/io/File");
//				mv.visitInsn(DUP);
////				mv.visitLdcInsn("/Users/nj/UFL/plp/eclipse_workspace/PLPHomework/src/cop5556fa17/ShelterPoint.jpg");
//				mv.visitVarInsn(ALOAD, 11);
//				mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
//				mv.visitFieldInsn(PUTSTATIC, className, ds.name, ds.getType().toString());
//			}
		}
		
//		mv.visitFieldInsn(PUTSTATIC, className, ds.name, ds.getType().toString());	//TODO: what type? ImageDesc? ds.getType().toString()
		
		return null;
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
//		if(so.sink.getType()==Type.SCREEN){
		if(so.getDec().getType()==Type.INTEGER || so.getDec().getType()==Type.BOOLEAN){
			CodeGenUtils.genPrintTOS(GRADE, mv, so.getDec().getType());	//NOTE THIS IS PRINT TOS
			mv.visitInsn(POP);	//REMOVE VALUE ON STACK
		}else {
			so.sink.visit(this, arg);
		}

//		TODO: if SINK = SCREEN handled.   //true for int, bool, image 
//		if SINK = file:  true only for source = image: check parser.java and typeCheckVisitor.java 


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
		else {	//image
//			if(si.getDec().xsize)
			Declaration_Image dec = (Declaration_Image) si.getDec();
			if(dec.xSize==null) {	//load original image.
				mv.visitInsn(ACONST_NULL);
				mv.visitInsn(ACONST_NULL);
//				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readFromFile", "(Ljava/lang/String;)Ljava/awt/image/BufferedImage;", false);
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readImage", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/awt/image/BufferedImage;", false);
				mv.visitFieldInsn(PUTSTATIC, className, si.name, "Ljava/awt/image/BufferedImage;");	
			}
			else {
//				BufferedImage refImage0 = ImageSupport.readImage(imageFile1, 128, 128);
//				string is already on top of stack. now push the size X, Y.
				mv.visitFieldInsn(GETSTATIC, className, si.name, "Ljava/awt/image/BufferedImage;");
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getX", "(Ljava/awt/image/BufferedImage;)I", false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
				
				mv.visitFieldInsn(GETSTATIC, className, si.name, "Ljava/awt/image/BufferedImage;");
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getY", "(Ljava/awt/image/BufferedImage;)I", false);
				mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
//				NOTE: We have converted the "I" type to "Ljava/lang/Integer;" before calling!!
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "readImage", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Integer;)Ljava/awt/image/BufferedImage;", false);
				mv.visitFieldInsn(PUTSTATIC, className, si.name, "Ljava/awt/image/BufferedImage;");
			}
			
		}

		return null;
	}


	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
//	REQUIRE:  LHS.Type == Expression.Type
//	StatementAssign.isCartesian <= LHS.isCartesian

//	1.If the type is Image, generate code to loop over the pixels of the image.  
//	2.For each pixel, visit the expression to generate code to leave its value on top of the stack and visit LHS to 
//	generate code to store the value in the image.   
//	3.The range of x is [0, X) and y is [0, Y) where X and Y can be obtained from the image object using methods 
//	ImageSupport.getX and imageSupport.getY.   
//	4.Note that x, y, X, and Y are all predefined variables that need to be added to the class.  
//	5.See comments below.   (Hint:  write a little Java program to see how to implement the loops).  

//	To handle polar coordinates, you still loop over x and y,  but calculate r and a using RuntimeFunctions.polar_r and RuntimeFunctions.polar_a.

	@Override
	public Object visitStatement_Assign(Statement_Assign sa, Object arg) throws Exception {	//statement_Assign
		//TODO  (see comment)
//		throw new UnsupportedOperationException();
		if(ImageDesc.equals(sa.lhs.getType().toString())){
			//place X and Y on stack		//mv.visitMethodInsn(INVOKEVIRTUAL, "java/awt/image/BufferedImage", "getWidth", "()I", false);
//			mv.visitMethodInsn(INVOKEVIRTUAL, cop5556fa17.imageSupport, "getX", "(java/awt/image/BufferedImage)I", false);
//			image[512,512] g;		//declaration
//			g[[x,y]] = 16711680;		//assignment: g[[x,y]] comes from LHS and 16711680 comes from e.  
			
//			Declaration_Image dec = (Declaration_Image)sa.lhs.getDec();
//			int X,Y;
//			if(dec.xSize!=null) {
//				Expression_IntLit Xsize = (Expression_IntLit)dec.xSize;
//				Expression_IntLit Ysize = (Expression_IntLit)dec.ySize;
//				X=Xsize.value;
//				Y=Ysize.value;
//			}else {
//				X=256;	//default image size is orignal image size so this is wrong!
//				Y=256;
//			}
			
			//					 int X=512,Y=512;		//This are set during declaration
			//					 image = ImageSupport.makeImage(X,Y);
			//					 int val = 16777215;		this is expression value		//mv.visitLdcInsn(new Integer(16777215));
			//					 int x,y;				
			//					 for(y=0; y < Y; ++y) {
			//						 for (x = 0; x < X; ++x ) {
			//							 ImageSupport.setPixel(val, image, x, y);
			//						 }
			//					 }
			
			mv.visitFieldInsn(GETSTATIC, className, sa.lhs.name, "Ljava/awt/image/BufferedImage;");
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getX", "(Ljava/awt/image/BufferedImage;)I", false);
			mv.visitVarInsn(ISTORE, 3);	//X
			
			mv.visitFieldInsn(GETSTATIC, className, sa.lhs.name, "Ljava/awt/image/BufferedImage;");
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getY", "(Ljava/awt/image/BufferedImage;)I", false);		
			mv.visitVarInsn(ISTORE, 4);	//Y
			
//					x,y,X,Y	>1,2,3,4
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 2);	//y-for loop
			Label l6 = new Label();
			mv.visitJumpInsn(GOTO, l6);
			
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 1);	//x-for loop
			Label l9 = new Label();
			mv.visitJumpInsn(GOTO, l9);
			Label l10 = new Label();
			mv.visitLabel(l10);
			
			//call expression: //			mv.visitVarInsn(ILOAD, 10);	//tmp stores value
				//			mv.visitVarInsn(ILOAD, 1);	//x
				//			mv.visitVarInsn(ILOAD, 2);	//y
				//			mv.visitInsn(IMUL);
			sa.e.visit(this, arg);
			sa.lhs.visit(this, arg);
			
			mv.visitIincInsn(1, 1);		//inc x by 1
			mv.visitLabel(l9);	//inner for loop condition---------------
			mv.visitVarInsn(ILOAD, 1);	//x
			mv.visitVarInsn(ILOAD, 3);	//X		//these are related to the image; and not tempVariable stored on stack
			mv.visitJumpInsn(IF_ICMPLT, l10); //inner for loop---------------

			mv.visitIincInsn(2, 1);		//inc y by 1
			mv.visitLabel(l6);	//outer for loop condition---------------
			mv.visitVarInsn(ILOAD, 2);	//y
			mv.visitVarInsn(ILOAD, 4);	//Y		//these are related to the image; and not tempVariable stored on stack
			mv.visitJumpInsn(IF_ICMPLT, l7); //outer for loop---------------
			
			
		}else{	//INT or BOOLEAN
			sa.e.visit(this, arg);
			sa.lhs.visit(this, arg);
		}
		
		return null;
	}

//	Visit the expressions to leave the values on top of the stack.  If isCartesian, you are done.  
//	If not, generate code to convert r and a to x and y using (cart_x and cart_y).  Hint:  you will need to manipulate the stack a little bit to handle the two values.   You may find  DUP2, DUP_X2, and POP useful.  
	@Override
	public Object visitIndex(Index index, Object arg) throws Exception {
		// TODO HW6
//		throw new UnsupportedOperationException();
		if(index.isCartesian()) {
			index.e0.visit(this, arg);
			index.e1.visit(this, arg);
		}else {
			index.e0.visit(this, arg);
			index.e1.visit(this, arg);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "cart_x", "(II)I", false);
			index.e0.visit(this, arg);
			index.e1.visit(this, arg);
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "cart_y", "(II)I", false);
		}
		return null;
	}


	/**
	 * In HW5, only handle INTEGER and BOOLEAN types.
	 */
//	If LHS.Type  is INTEGER or BOOLEAN, generate code to
//    store the value on top of the stack in variable name.
//TODO Assignment 6:  handle case where LHS.Type is IMAGE
	@Override
	public Object visitLHS(LHS lhs, Object arg) throws Exception {	//lhs has String name, Index index
		//TODO  (see comment)
//		throw new UnsupportedOperationException();

		if(lhs.getType() == Type.INTEGER || lhs.getType() == Type.BOOLEAN){		//simply put the value in variable.
			mv.visitFieldInsn(PUTSTATIC, className, lhs.name, lhs.getType().toString());
		}
		else {	//image:set each and every pixel of [x,y] or [r,a]		//check if it is predefined variables : x,y only then iterate  
			if(lhs.index!=null) {
				try {
					Expression_PredefinedName exp0= (Expression_PredefinedName)lhs.index.e0;
					Expression_PredefinedName exp1= (Expression_PredefinedName)lhs.index.e1;
					if(exp0.kind==Kind.KW_r && exp1.kind==Kind.KW_a) {		//handle the radian case. iterate over all
						mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
						mv.visitVarInsn(ILOAD, 1);	//x
						mv.visitVarInsn(ILOAD, 2);	//y
//						lhs.index.visit(this, arg);
						mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "setPixel", "(ILjava/awt/image/BufferedImage;II)V", false);
					}
					else if(exp0.kind==Kind.KW_x && exp1.kind==Kind.KW_y) {
//						****Note currently we have value of expression(PIXEL) on top of stack. lets store it in temporary variable
						mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
						mv.visitVarInsn(ILOAD, 1);	//x
						mv.visitVarInsn(ILOAD, 2);	//y
//						lhs.index.visit(this, arg);
						mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "setPixel", "(ILjava/awt/image/BufferedImage;II)V", false);
					}
				}catch(Exception e) {
					//it means index values are integers. Set pixel value at those locations only.
//					g[12,13] = 65280;	//you don't need to iterate anything.
					mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
					lhs.index.visit(this, arg);
					mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "setPixel", "(ILjava/awt/image/BufferedImage;II)V", false);
				}	
			}
		}

		
		return null;
	}


//	Generate code to display the image (whose ref should be on top of the stack already) on the screen.  
//	Call ImageFrame.makeFrame.  Note that this method returns a reference to the frame which is not needed, 
//	so pop it off the stack.
	@Override
	public Object visitSink_SCREEN(Sink_SCREEN sc, Object arg) throws Exception {	//sink_SCREEN
		//TODO HW6
//		throw new UnsupportedOperationException();
		mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageFrame", "makeFrame", "(Ljava/awt/image/BufferedImage;)Ljavax/swing/JFrame;", false);
		mv.visitInsn(POP);
		return null;

//		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//		mv.visitFieldInsn(GETSTATIC, className, so.name, so.getDec().getType().toString());
//		//mv.visitInsn(SWAP);

//		String desc = "(" + so.getDec().getType().toString() + ")V";
//		System.out.print("---------------"+desc+"------------\n");
//		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", desc, false);

//		return null;
	}
	
//	The identifier should contain a reference to a String representing a filename.  
//	Generate code to write the image to the file.  The image reference should already be on the stack, 
//	so load the filename and invoke ImageSupport.write.
	@Override
	public Object visitSink_Ident(Sink_Ident si, Object arg) throws Exception {	//sink_Ident
//		TODO HW6
//		throw new UnsupportedOperationException();
//		mv.visitFieldInsn(GETSTATIC, "cop5556fa17/MyTest", "image", "Ljava/awt/image/BufferedImage;");
//		mv.visitVarInsn(ALOAD, 1);
		mv.visitFieldInsn(GETSTATIC, className, si.name, si.getType().toString());	
		//TYPE URL and FILE both: si.getType().toString(): STRING: See TypeUtils and visitDeclaration_SourceSink
		mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "write", "(Ljava/awt/image/BufferedImage;Ljava/lang/String;)V", false);
		return null;
	}

//	Load the String(fileOrURL) onto the stack
//	Check: Parser.java or TypeCheckVisitor.java
	@Override
	public Object visitSource_StringLiteral(Source_StringLiteral ssl, Object arg) throws Exception {	//source_StringLiteral
		// TODO HW6
//		throw new UnsupportedOperationException();
//		mv.visitFieldInsn(GETFIELD, className, ssl.fileOrUrl, ssl.getType().toString());
//		mv.visitFieldInsn(GETSTATIC, className, varName, varType);
		mv.visitLdcInsn(new String(ssl.fileOrUrl));
		return null;
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
		mv.visitVarInsn(ALOAD,0);	//object load 0
		scp.paramNum.visit(this, arg);	//it must be INTEGER checked in Symantic analysis: that specifies index of argument of String args[]
		mv.visitInsn(AALOAD);
//		CodeGenUtils.genLogTOS(GRADE, mv, scp.getType());
		return null;
	}

	//	This identifier refers to a String.  Load it onto the stack.|| it is either FILE or URL: load that string onto stack!!
	@Override
	public Object visitSource_Ident(Source_Ident source_Ident, Object arg) throws Exception {
		// TODO HW6
//		throw new UnsupportedOperationException();
		//this variable must have been declared previously. load its value in top of stack
		mv.visitFieldInsn(GETSTATIC, className, source_Ident.name, "Ljava/lang/String;");
//		mv.visitLdcInsn(new String(source_Ident.name));		//name = file_name: 
		return null;
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
//					mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
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


//	Generate code to load the image reference on the stack.  
//	Visit the index to generate code to leave Cartesian location of index on the stack.  
//	Then  invoke ImageSupport.getPixel which generates code to leave the value of the pixel on the stack.
	@Override
	public Object visitExpression_PixelSelector(Expression_PixelSelector eps, Object arg)	//expression_PixelSelector
			throws Exception {
		// TODO HW6
//		throw new UnsupportedOperationException();
		mv.visitFieldInsn(GETSTATIC, className, eps.name, "Ljava/awt/image/BufferedImage;");
		eps.index.visit(this, arg);
		mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getPixel", "(Ljava/awt/image/BufferedImage;II)I", false);
		return null;
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

//	Visit the expression to generate code to leave its value on top of the stack.  
//	Then invoke the corresponding function in RuntimeFunctions.  The functions that belong here are abs and log.
//	(You do not need to implement sin, cos, or atan)
	@Override
	public Object visitExpression_FunctionAppWithExprArg(
			Expression_FunctionAppWithExprArg ef, Object arg) throws Exception {	//expression_FunctionAppWithExprArg
		// TODO HW6
//		throw new UnsupportedOperationException();
		ef.arg.visit(this, arg);
		if(ef.function==Kind.KW_abs) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "abs", "(I)I", false);
		}
		else if(ef.function==Kind.KW_log) {
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "log", "(I)I", false);
		}
		
		return null;
	}

//	Visit the expressions belonging to the index.  
//	Then invoke the corresponding function in RuntimeFunctions.  
//	The functions that belong here are cart_x, cart_y, polar_r, and polar_a.  
//	These functions convert between the cartesian (x,y) and polar (r,a) 
//	(i.e. radius and angle in degrees) representations of the location in the image.
	@Override
	public Object visitExpression_FunctionAppWithIndexArg(
			Expression_FunctionAppWithIndexArg ef, Object arg) throws Exception {	//expression_FunctionAppWithIndexArg
		// TODO HW6
//		throw new UnsupportedOperationException();
		if(ef.arg!=null) {
			ef.arg.e0.visit(this, arg);
			ef.arg.e1.visit(this, arg);
			
			if(ef.function==Kind.KW_cart_x) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "cart_x", "(II)I", false);
			}
			else if(ef.function==Kind.KW_cart_y) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "cart_y", "(II)I", false);
			}
			else if(ef.function==Kind.KW_polar_r) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_r", "(II)I", false);
			}
			else if(ef.function==Kind.KW_polar_a) {
				mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_a", "(II)I", false);
			}
		}
		return null;
	}

	@Override
	public Object visitExpression_PredefinedName(Expression_PredefinedName epn, Object arg)	//expression_PredefinedName
			throws Exception {
		// TODO HW6
//		throw new UnsupportedOperationException();
//		x, y, X,Y, r,a, R,A, DEF_X, DEF_Y, Z
		int slot_no = 1;
		if(epn.kind==Kind.KW_x) {		//need to initialise them at the start of program
//			mv.visitInsn(ICONST_0);
//			mv.visitVarInsn(ISTORE, 1);
			mv.visitVarInsn(ILOAD, 1);
//			mv.visitInsn(ICONST_0);
//			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		}
		else if(epn.kind==Kind.KW_y){
			mv.visitVarInsn(ILOAD, 2);
//			mv.visitInsn(ICONST_0);
//			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		}
		else if(epn.kind==Kind.KW_X){
			mv.visitVarInsn(ILOAD, 3);
//			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		}
		else if(epn.kind==Kind.KW_Y){
			mv.visitVarInsn(ILOAD, 4);
//			mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
		}
//		r:  the radius in the polar representation of cartesian location x and y.  
//		Obtain from x and y with RuntimeFunctions.polar_r.
		else if(epn.kind==Kind.KW_r){
			mv.visitVarInsn(ILOAD, 1);	//x
			mv.visitVarInsn(ILOAD, 2);	//y
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_r", "(II)I", false);
			mv.visitVarInsn(ISTORE, 5);
			mv.visitVarInsn(ILOAD, 5);
		}
//		 the angle, in degrees, in the polar representation of cartesian location x and y.  
//		Obtain from x and y with RuntimeFunctions.polar_a.		
		else if(epn.kind==Kind.KW_a){
			mv.visitVarInsn(ILOAD, 1);	//x
			mv.visitVarInsn(ILOAD, 2);	//y
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_a", "(II)I", false);
			mv.visitVarInsn(ISTORE, 6);
			mv.visitVarInsn(ILOAD, 6);
		}
//		R: the upper bound on r, obtain from polar_r(X,Y)
		else if(epn.kind==Kind.KW_R){
			mv.visitVarInsn(ILOAD, 3);	//X
			mv.visitVarInsn(ILOAD, 4);	//Y
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_r", "(II)I", false);
			mv.visitVarInsn(ISTORE, 7);
			mv.visitVarInsn(ILOAD, 7);
		}
//		A:  the upper bound on a, obtain from polar_a(0,Y)
		else if(epn.kind==Kind.KW_A){
			mv.visitInsn(ICONST_0);	//0
			mv.visitVarInsn(ILOAD, 4);	//y
			mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/RuntimeFunctions", "polar_a", "(II)I", false);
			mv.visitVarInsn(ISTORE, 8);
			mv.visitVarInsn(ILOAD, 8);
		}
		else if(epn.kind==Kind.KW_DEF_X){
			mv.visitLdcInsn(new Integer(256));
		}
		else if(epn.kind==Kind.KW_DEF_Y){
			mv.visitLdcInsn(new Integer(256));
		}
		else if(epn.kind==Kind.KW_Z){
			mv.visitLdcInsn(new Integer(16777215));
		}
		return null;
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
