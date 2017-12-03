package cop5556fa17;

import cop5556fa17.Scanner.Token;

public class TypeUtils {

	public static enum Type {
		INTEGER("I"),
		BOOLEAN("Z"),
		IMAGE("Ljava/awt/image/BufferedImage;"),	//Ljava.awt.image.BufferedImage
		URL("Ljava/lang/String;"),		//Ljava/net/URL;
		FILE("Ljava/lang/String;"),	//Ljava/io/File;
		SCREEN("Screen"),
		NONE(null);

		private final String name;

	    private Type(String s) {
	        name = s;
	    }
	    public String toString() {
	        return this.name;
	    }
	}


	public static Type getType(Token token){
		switch (token.kind){
		case KW_int: {return Type.INTEGER;}
		case KW_boolean: {return Type.BOOLEAN;}
		case KW_image: {return Type.IMAGE;}
		case KW_url: {return Type.URL;}
		case KW_file: {return Type.FILE;}
		case KW_SCREEN: {return Type.SCREEN;}
			default :
				break;
		}
		assert false;  //should not reach here
		return null;
	}

	public static Type getType(Scanner.Kind k){
		switch (k){
		case KW_int: {return Type.INTEGER;}
		case KW_boolean: {return Type.BOOLEAN;}
		case KW_image: {return Type.IMAGE;}
		case KW_url: {return Type.URL;}
		case KW_file: {return Type.FILE;}
		case KW_SCREEN: {return Type.SCREEN;}
			default :
				break;
		}
		assert false;  //should not reach here
		return null;
	}
}
