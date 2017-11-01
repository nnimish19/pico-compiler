package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils.Type;

public abstract class Declaration extends ASTNode {
	
	public Declaration(Token firstToken) {
		super(firstToken);
	}

	Type typename;
	
	public Type getType(){
		return typename;
	}
	
	public void setType(Type type){
		typename= type;
	}


}
