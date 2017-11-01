package cop5556fa17.AST;

import cop5556fa17.Scanner.Token;
import cop5556fa17.TypeUtils.Type;

public abstract class Source extends ASTNode{

	public Source(Token firstToken) {
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
