package cop5556fa17;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import cop5556fa17.Scanner.Kind;
import cop5556fa17.ImageSupport.*;

public class MyTest {
	 static BufferedImage image;
	 public final static String imageFile1 = "/Users/nj/UFL/plp/eclipse_workspace/PLPHomework/src/cop5556fa17/ShelterPoint.jpg";
	 public static void main(String[] args) {
		 image = ImageSupport.readImage(imageFile1,null,null);
		 String imageFile2 = "/Users/nj/UFL/plp/eclipse_workspace/PLPHomework/src/cop5556fa17/ShelterPoint2.jpg";;
		 ImageSupport.write(image, imageFile2);
		 try {
			ImageFrame.makeFrame(image);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
//		  int x = ImageSupport.getX(image);
//		 image = ImageSupport.readImage(imageFile1,ImageSupport.getX(image),ImageSupport.getX(image));
		 int x=0;
		 int y=1;
		 RuntimeFunctions.polar_r(x,y);
		 ImageSupport.getPixel(image,x,y);
		 
		 String name = "nimisj";
		 String x1= name;
//		 int x=112313414;
//		 int y=x;
		 
		 try {
			 File file = new File(imageFile1);
			 String s = file.getName();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 int r=10, theta=12;
		 RuntimeFunctions.cart_x( r,  theta);
		 RuntimeFunctions.polar_r( r,  theta);
		 RuntimeFunctions.abs( r);
		 RuntimeFunctions.log( r);
		 
	 }
}

//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 1);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 2);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 3);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 4);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 5);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 6);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 7);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 8);

//int X=512,Y=512;
//image = ImageSupport.makeImage(X,Y);
//int val = 16777215;
//int x,y;
//for(y=0; y < Y; ++y) {
//	 for (x = 0; x < X; ++x ) {
//		 ImageSupport.setPixel(val, image, x, y);
//	 }
//}

//mv.visitLdcInsn(new Integer(16777215));
//mv.visitVarInsn(ISTORE, 3);
//Label l4 = new Label();
//mv.visitLabel(l4);
//mv.visitLineNumber(17, l4);
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 5);
//Label l5 = new Label();
//mv.visitLabel(l5);
//Label l6 = new Label();
//mv.visitJumpInsn(GOTO, l6);
//Label l7 = new Label();
//mv.visitLabel(l7);
//mv.visitLineNumber(18, l7);
//mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.INTEGER}, 0, new Object[] {});
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 4);
//Label l8 = new Label();
//mv.visitLabel(l8);
//Label l9 = new Label();
//mv.visitJumpInsn(GOTO, l9);
//Label l10 = new Label();
//mv.visitLabel(l10);
//mv.visitLineNumber(19, l10);
//mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER}, 0, new Object[] {});
//mv.visitVarInsn(ILOAD, 3);
//mv.visitFieldInsn(GETSTATIC, "cop5556fa17/MyTest", "image", "Ljava/awt/image/BufferedImage;");
//mv.visitVarInsn(ILOAD, 4);
//mv.visitVarInsn(ILOAD, 5);
//mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "setPixel", "(ILjava/awt/image/BufferedImage;II)V", false);
//Label l11 = new Label();
//mv.visitLabel(l11);
//mv.visitLineNumber(18, l11);
//mv.visitIincInsn(4, 1);
//mv.visitLabel(l9);
//mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//mv.visitVarInsn(ILOAD, 4);
//mv.visitVarInsn(ILOAD, 1);
//mv.visitJumpInsn(IF_ICMPLT, l10);
//Label l12 = new Label();
//mv.visitLabel(l12);
//mv.visitLineNumber(17, l12);
//mv.visitIincInsn(5, 1);
//mv.visitLabel(l6);
//mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.INTEGER}, 0, new Object[] {});
//mv.visitVarInsn(ILOAD, 5);
//mv.visitVarInsn(ILOAD, 2);
//mv.visitJumpInsn(IF_ICMPLT, l7);

//int x = 3;
//int y=x^Integer.MAX_VALUE;
//System.out.println(y);
//
//final int mt =3;
//System.out.println(mt);
////mt=4;

//------------------------------------------------------------------------------------------------------------------------------------------
//create a Short object using one of the below given constructors
//1. Create a Short object from short
//short s = 10;
//Short sObj1 = new Short(s);
//String x = "helo";	//args[0];
//System.out.print(x);

//String a="helpo";
//int b = 12;
////System.out.print(a.);
//System.out.print(MyType.INTEGER.toString());
//Boolean g;
//g = true;
////g -> SCREEN;
//g = false;
////g -> SCREEN;
//return ;
//------------------------------------------------------------------------------------------------------------------------------------------
//public enum MyType{//https://stackoverflow.com/questions/6667243/using-enum-values-as-string-literals
//INTEGER("I"), BOOLEAN ("ZX");
//
//private final String name;
//
//private MyType(String s) {
//    name = s;
//}
//public String toString() {
//    return this.name;
// }
//
//};

//https://github.com/AjanthaRamineni/Compiler/blob/master/src/cop5556sp17/CodeGenVisitor.java
//	(eb.op == Kind.OP_AND || eb.op == Kind.OP_OR)
//else if(eb.op == Kind.OP_AND){
//	Label l1 = new Label();
//	Label l2 = new Label();
//	eb.e0.visit(this, arg);
//	eb.e1.visit(this,arg);
//	mv.visitJumpInsn(IFEQ, l1);
//	mv.visitJumpInsn(IFEQ, l1);
//	mv.visitInsn(ICONST_1);
//	mv.visitJumpInsn(GOTO, l2);
//	mv.visitLabel(l1);
//	mv.visitInsn(ICONST_0);
//	mv.visitLabel(l2);
//}

//               else if(eb.op == Kind.OP_AND){
//                       eb.e0.visit(this, arg);
//                       Label l1 = new Label();
//                       mv.visitJumpInsn(IFEQ, l1);
//                       eb.e1.visit(this,arg);
//                       mv.visitJumpInsn(IFEQ, l1);
//                       mv.visitInsn(ICONST_1);
//                       Label l2 = new Label();
//                       mv.visitJumpInsn(GOTO, l2);
//                       mv.visitLabel(l1);
//                       mv.visitInsn(ICONST_0);
//                       mv.visitLabel(l2);
//               }

//               else if(eb.op == Kind.OP_OR){
//                       eb.e0.visit(this, arg);
//                       Label l1 = new Label();
//                       mv.visitJumpInsn(IFNE, l1);
//                       eb.e1.visit(this,arg);
//                       mv.visitJumpInsn(IFNE, l1);
//                       mv.visitInsn(ICONST_0);
//                       Label l2 = new Label();
//                       mv.visitJumpInsn(GOTO, l2);
//                       mv.visitLabel(l1);
//                       mv.visitInsn(ICONST_1);
//                       mv.visitLabel(l2);
//               }



//else if(eb.op == Kind.OP_AND){
//Label l1 = new Label();
//Label l2 = new Label();
//
//eb.e0.visit(this, arg);	//true
////mv.visitInsn(Opcodes.DUP);
//eb.e1.visit(this,arg);	//false
////mv.visitInsn(ICONST_0);
//mv.visitJumpInsn(IFEQ, l1);	//goto l1	IF_ICMPEQ //consuming top 2?
////mv.visitJumpInsn(IF_ICMPEQ, l1);
//mv.visitInsn(ICONST_1);
////mv.visitInsn(ICONST_0);
////mv.visitJumpInsn(IF_ICMPEQ, l1);
////mv.visitInsn(ICONST_1);
//mv.visitJumpInsn(GOTO, l2);
//mv.visitLabel(l1);
//mv.visitInsn(ICONST_0);
//mv.visitLabel(l2);
//}

//else if(eb.op == Kind.OP_OR){
//Label l1 = new Label();
//Label l2 = new Label();
//eb.e0.visit(this, arg);
//eb.e1.visit(this,arg);
//mv.visitJumpInsn(IFNE, l1);
//mv.visitFieldInsn(GETSTATIC, className, eb.e1.name, eb.e1.getType().toString());	//CHECK
//mv.visitJumpInsn(IFNE, l1);
//mv.visitInsn(ICONST_0);
//mv.visitJumpInsn(GOTO, l2);
//mv.visitLabel(l1);
//mv.visitInsn(ICONST_1);
//mv.visitLabel(l2);
//}
////	(eb.op == Kind.OP_AND || eb.op == Kind.OP_OR)
//else if(eb.op == Kind.OP_AND){
//Boolean flg=false;
//eb.e0.visit(this, arg);
//Label l1 = new Label();
//mv.visitJumpInsn(IFEQ, l1);
//flg=true;
//eb.e1.visit(this,arg);
//mv.visitJumpInsn(IFEQ, l1);
//mv.visitInsn(ICONST_1);
//Label l2 = new Label();
//mv.visitJumpInsn(GOTO, l2);
//mv.visitLabel(l1);
//if(flg==false)	eb.e1.visit(this,arg);	//EVEN if 1st EXP is false still call the other expression
//mv.visitInsn(ICONST_0);
//mv.visitLabel(l2);
//}
//else if(eb.op == Kind.OP_AND){
//eb.e0.visit(this, arg);
//Label l1 = new Label();
//Label l2 = new Label();
//Label l3 = new Label();
//mv.visitJumpInsn(IFEQ, l1);
//
//eb.e1.visit(this,arg);
//mv.visitJumpInsn(IFEQ, l3);
//mv.visitInsn(ICONST_1);
//mv.visitJumpInsn(GOTO, l2);
//
//mv.visitLabel(l1);
////eb.e1.visit(this,arg);
//mv.visitLabel(l3);
//mv.visitInsn(ICONST_0);
//mv.visitLabel(l2);
//}
//else if(eb.op == Kind.OP_OR){
//Label l3 = new Label();
//Label l4 = new Label();
//Boolean flg=false;
//eb.e0.visit(this, arg);
////mv.visitInsn(Opcodes.DUP);
//eb.e1.visit(this,arg);
////mv.visitInsn(Opcodes.DUP);
//Label l1 = new Label();
//mv.visitJumpInsn(IFNE, l1);
//flg=true;
////eb.e1.visit(this,arg);
////mv.visitJumpInsn(IFNE, l1);
//mv.visitInsn(ICONST_0);
//Label l2 = new Label();
//mv.visitJumpInsn(GOTO, l2);
//mv.visitLabel(l1);
////if(flg==false)	eb.e1.visit(this,arg);	//even if 1st EXP is true, still call other expression
//mv.visitInsn(ICONST_1);
//mv.visitLabel(l2);
//}



//-----------------------------------------------------------------------------------------------

//****Note currently we have value of expression on top of stack. lets store it in temporary variable
//mv.visitVarInsn(ISTORE, 10);		//10 is tmp variable.  	
////WE need value of X and Y from image now: getX, getY.   (if there are multiple images, X,Y will store only latest value)
//mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
//mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getX", "(Ljava/awt/image/BufferedImage;)I", false);
////mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
//mv.visitVarInsn(ISTORE, 3);	//X
//
//mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
//mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "getY", "(Ljava/awt/image/BufferedImage;)I", false);
////ERROR: Type 'java/lang/Integer' (current frame, stack[0]) is not assignable to integer
////mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);		
//mv.visitVarInsn(ISTORE, 4);	//Y
//
////					x,y,X,Y	>1,2,3,4
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 2);	//5	//variable y
//Label l5 = new Label();
//mv.visitLabel(l5);
//Label l6 = new Label();
//mv.visitJumpInsn(GOTO, l6);
//Label l7 = new Label();
//mv.visitLabel(l7);
////					mv.visitLineNumber(18, l7);
////					mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.INTEGER}, 0, new Object[] {});
//mv.visitInsn(ICONST_0);
//mv.visitVarInsn(ISTORE, 1);		//x
//Label l8 = new Label();
//mv.visitLabel(l8);
//Label l9 = new Label();
//mv.visitJumpInsn(GOTO, l9);
//Label l10 = new Label();
//mv.visitLabel(l10);
////					mv.visitLineNumber(19, l10);
////					mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER}, 0, new Object[] {});
////mv.visitVarInsn(ILOAD, 10);	//tmp stores value 
//mv.visitVarInsn(ILOAD, 1);	//x
//mv.visitVarInsn(ILOAD, 2);	//y
//mv.visitInsn(IMUL);
//mv.visitFieldInsn(GETSTATIC, className, lhs.name, "Ljava/awt/image/BufferedImage;");
//mv.visitVarInsn(ILOAD, 1);	//x
//mv.visitVarInsn(ILOAD, 2);	//y
//mv.visitMethodInsn(INVOKESTATIC, "cop5556fa17/ImageSupport", "setPixel", "(ILjava/awt/image/BufferedImage;II)V", false);
//Label l11 = new Label();
//mv.visitLabel(l11);
////					mv.visitLineNumber(18, l11);
//mv.visitIincInsn(1, 1);		//inc x by 1
//mv.visitLabel(l9);	//inner for loop condition---------------
////					mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
//mv.visitVarInsn(ILOAD, 1);	//x
//mv.visitVarInsn(ILOAD, 3);	//X		//these are related to the image; and not tempVariable stored on stack
//mv.visitJumpInsn(IF_ICMPLT, l10); //inner for loop---------------
//Label l12 = new Label();
//mv.visitLabel(l12);
////					mv.visitLineNumber(17, l12);
//mv.visitIincInsn(2, 1);		//inc y by 1
//mv.visitLabel(l6);	//outer for loop condition---------------
////					mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"[Ljava/lang/String;", Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.INTEGER, Opcodes.TOP, Opcodes.INTEGER}, 0, new Object[] {});
//mv.visitVarInsn(ILOAD, 2);	//y
//mv.visitVarInsn(ILOAD, 4);	//Y		//these are related to the image; and not tempVariable stored on stack
//mv.visitJumpInsn(IF_ICMPLT, l7); //outer for loop---------------