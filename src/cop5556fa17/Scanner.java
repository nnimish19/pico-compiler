/* *
 * Scanner for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2017.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2017 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2017
  */

package cop5556fa17;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Scanner {
	
	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {
		
		int pos;

		public LexicalException(String message, int pos) {
			super(message);
			this.pos = pos;
		}
		
		public int getPos() { return pos; }

	}

	public static enum Kind {
		IDENTIFIER, INTEGER_LITERAL, BOOLEAN_LITERAL, STRING_LITERAL, 
		KW_x/* x */, KW_X/* X */, KW_y/* y */, KW_Y/* Y */, KW_r/* r */, KW_R/* R */, KW_a/* a */, 
		KW_A/* A */, KW_Z/* Z */, KW_DEF_X/* DEF_X */, KW_DEF_Y/* DEF_Y */, KW_SCREEN/* SCREEN */, 
		KW_cart_x/* cart_x */, KW_cart_y/* cart_y */, KW_polar_a/* polar_a */, KW_polar_r/* polar_r */, 
		KW_abs/* abs */, KW_sin/* sin */, KW_cos/* cos */, KW_atan/* atan */, KW_log/* log */, 
		KW_image/* image */,  KW_int/* int */, 
		KW_boolean/* boolean */, KW_url/* url */, KW_file/* file */, OP_ASSIGN/* = */, OP_GT/* > */, OP_LT/* < */, 
		OP_EXCL/* ! */, OP_Q/* ? */, OP_COLON/* : */, OP_EQ/* == */, OP_NEQ/* != */, OP_GE/* >= */, OP_LE/* <= */, 
		OP_AND/* & */, OP_OR/* | */, OP_PLUS/* + */, OP_MINUS/* - */, OP_TIMES/* * */, OP_DIV/* / */, OP_MOD/* % */, 
		OP_POWER/* ** */, OP_AT/* @ */, OP_RARROW/* -> */, OP_LARROW/* <- */, LPAREN/* ( */, RPAREN/* ) */, 
		LSQUARE/* [ */, RSQUARE/* ] */, SEMI/* ; */, COMMA/* , */, EOF;
	}
	
//	Hashmap: string/char to Token kind.
	HashMap mp = new HashMap();
	private void myInitialise(){
		mp.put("x", Kind.KW_x);
		mp.put("X", Kind.KW_X);
		mp.put("y", Kind.KW_y);
		mp.put("Y", Kind.KW_Y);
		mp.put("r", Kind.KW_r);
		mp.put("R", Kind.KW_R);
		mp.put("a", Kind.KW_a);
		mp.put("A", Kind.KW_A);
		mp.put("Z", Kind.KW_Z);
		mp.put("DEF_X", Kind.KW_DEF_X);
		mp.put("DEF_Y", Kind.KW_DEF_Y);
		mp.put("SCREEN", Kind.KW_SCREEN);
		mp.put("cart_x", Kind.KW_cart_x);
		mp.put("cart_y", Kind.KW_cart_y);
		mp.put("polar_a", Kind.KW_polar_a);
		mp.put("polar_r", Kind.KW_polar_r);
		mp.put("abs", Kind.KW_abs);
		mp.put("sin", Kind.KW_sin);
		mp.put("cos", Kind.KW_cos);
		mp.put("atan", Kind.KW_atan);
		mp.put("log", Kind.KW_log);
		mp.put("image", Kind.KW_image);
		mp.put("int", Kind.KW_int);
		mp.put("boolean", Kind.KW_boolean);
		mp.put("url", Kind.KW_url);
		mp.put("file", Kind.KW_file);
		mp.put("true", Kind.BOOLEAN_LITERAL);
		mp.put("false", Kind.BOOLEAN_LITERAL);
		mp.put("=", Kind.OP_ASSIGN);
		mp.put(">", Kind.OP_GT);
		mp.put("<", Kind.OP_LT);
		mp.put("!", Kind.OP_EXCL);
		mp.put("?", Kind.OP_Q);
		mp.put(":", Kind.OP_COLON);
		mp.put("==", Kind.OP_EQ);
		mp.put("!=", Kind.OP_NEQ);
		mp.put(">=", Kind.OP_GE);
		mp.put("<=", Kind.OP_LE);
		mp.put("&", Kind.OP_AND);
		mp.put("|", Kind.OP_OR);
		mp.put("+", Kind.OP_PLUS);
		mp.put("-", Kind.OP_MINUS);
		mp.put("*", Kind.OP_TIMES);
		mp.put("/", Kind.OP_DIV);
		mp.put("%", Kind.OP_MOD);
		mp.put("**", Kind.OP_POWER);
		mp.put("@", Kind.OP_AT);
		mp.put("->", Kind.OP_RARROW);
		mp.put("<-", Kind.OP_LARROW);
		mp.put("(", Kind.LPAREN);
		mp.put(")", Kind.RPAREN);
		mp.put("[", Kind.LSQUARE);
		mp.put("]", Kind.RSQUARE);
		mp.put(";", Kind.SEMI);
		mp.put(",", Kind.COMMA);
	}
	

//	Potentially useful methods from the java.lang.Character class  include isDigit, isWhiteSpace, isJavaIdentifierStart, 
//	isJavaIdentifierPart, along with java.lang.Integer.parseInt.  
//	In my solution, I used java.util.HashMap to map keywords to their Token.kind.
	

	
	
	/** Class to represent Tokens. 
	 * 
	 * This is defined as a (non-static) inner class
	 * which means that each Token instance is associated with a specific 
	 * Scanner instance.  We use this when some token methods access the
	 * chars array in the associated Scanner.
	 * 
	 * 
	 * @author Beverly Sanders
	 *
	 */
	public class Token {
		public final Kind kind;
		public final int pos;
		public final int length;
		public final int line;
		public final int pos_in_line;

		public Token(Kind kind, int pos, int length, int line, int pos_in_line) {
			super();
			this.kind = kind;
			this.pos = pos;
			this.length = length;
			this.line = line;
			this.pos_in_line = pos_in_line;
		}

		public String getText() {
			if (kind == Kind.STRING_LITERAL) {
				return chars2String(chars, pos, length);
			}
			else return String.copyValueOf(chars, pos, length);
		}
		public boolean isKind(Kind k){
			return this.kind==k;
		}

		/**
		 * To get the text of a StringLiteral, we need to remove the
		 * enclosing " characters and convert escaped characters to
		 * the represented character.  For example the two characters \ t
		 * in the char array should be converted to a single tab character in
		 * the returned String
		 * 
		 * @param chars
		 * @param pos
		 * @param length
		 * @return
		 */
		private String chars2String(char[] chars, int pos, int length) {
			StringBuilder sb = new StringBuilder();
			for (int i = pos + 1; i < pos + length - 1; ++i) {// omit initial and final "
				char ch = chars[i];
				if (ch == '\\') { // handle escape
					i++;
					ch = chars[i];
					switch (ch) {
					case 'b':
						sb.append('\b');
						break;
					case 't':
						sb.append('\t');
						break;
					case 'f':
						sb.append('\f');
						break;
					case 'r':
						sb.append('\r'); //for completeness, line termination chars not allowed in String literals
						break;
					case 'n':
						sb.append('\n'); //for completeness, line termination chars not allowed in String literals
						break;
					case '\"':
						sb.append('\"');
						break;
					case '\'':
						sb.append('\'');
						break;
					case '\\':
						sb.append('\\');
						break;
					default:
						assert false;
						break;
					}
				} else {
					sb.append(ch);
				}
			}
			return sb.toString();
		}

		/**
		 * precondition:  This Token is an INTEGER_LITERAL
		 * 
		 * @returns the integer value represented by the token
		 */
		public int intVal() {
			assert kind == Kind.INTEGER_LITERAL;
			return Integer.valueOf(String.copyValueOf(chars, pos, length));
		}

		public String toString() {
			return "[" + kind + "," + String.copyValueOf(chars, pos, length)  + "," + pos + "," + length + "," + line + ","
					+ pos_in_line + "]";
		}

		/** 
		 * Since we overrode equals, we need to override hashCode.
		 * https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-
		 * 
		 * Both the equals and hashCode method were generated by eclipse
		 * 
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((kind == null) ? 0 : kind.hashCode());
			result = prime * result + length;
			result = prime * result + line;
			result = prime * result + pos;
			result = prime * result + pos_in_line;
			return result;
		}

		/**
		 * Override equals method to return true if other object
		 * is the same class and all fields are equal.
		 * 
		 * Overriding this creates an obligation to override hashCode.
		 * 
		 * Both hashCode and equals were generated by eclipse.
		 * 
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Token other = (Token) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (kind != other.kind)
				return false;
			if (length != other.length)
				return false;
			if (line != other.line)
				return false;
			if (pos != other.pos)
				return false;
			if (pos_in_line != other.pos_in_line)
				return false;
			return true;
		}

		/**
		 * used in equals to get the Scanner object this Token is 
		 * associated with.
		 * @return
		 */
		private Scanner getOuterType() {
			return Scanner.this;
		}

	}//End of Class Token

	
//	-----------------------------------------------------------
	/** 
	 * Extra character added to the end of the input characters to simplify the
	 * Scanner.  
	 */
	static final char EOFchar = 0;
	
	/**
	 * The list of tokens created by the scan method.
	 */
	final ArrayList<Token> tokens;
	
	/**
	 * An array of characters representing the input.  These are the characters
	 * from the input string plus and additional EOFchar at the end.
	 */
	final char[] chars;  
	
	/**
	 * position of the next token to be returned by a call to nextToken
	 */
	private int nextTokenPos = 0;		//position in the "tokens" array
	
//	current state of DFA  
	private State state;
		

//Scanner Constructor
	Scanner(String inputString) {
		int numChars = inputString.length();
		this.chars = Arrays.copyOf(inputString.toCharArray(), numChars + 1); // input string terminated with null char(ASCII value=0)
		chars[numChars] = EOFchar;			//input converted to char array. (explicit mention. last char = ASCII 0)
		tokens = new ArrayList<Token>();	//initialise token array. Empty
		myInitialise();
	}


	/**
	 * Method to scan the input and create a list of Tokens.
	 * 
	 * If an error is encountered during scanning, throw a LexicalException.
	 * 
	 * @return
	 * @throws LexicalException
	 */
	public Scanner scan_old() throws LexicalException {
		/* TODO  Replace this with a correct and complete implementation!!! */
		int pos = 0;
		int line = 1;
		int posInLine = 1;
		tokens.add(new Token(Kind.EOF, pos, 0, line, posInLine));
		return this;
		
		
	}
	
	private enum State {
		START,
		
		EQL,
		EXL,
		LTH,
		GTR,
		SUB,
		MUL,
		
		COM, 	//Comment
		IDN,
		DIG,
		STR,
		 
//		GOT_DOT,
//		GOT_SLASH, 				//got '/'
//		GOT_SLASHSTAR,			//got '/*'
//		GOT_SLASH2STAR,			//got '/*......*'
//		GOT_BSLASHR,			//got '\r'
		
		EOF
	}

	private String charToString(int pos, int length){
		return new String(chars, pos, length);
	}

	public Scanner scan() throws LexicalException {
	    int pos = 0;
	    int line = 1;
	    int pos_in_line = 1;
	    State state = State.START;
	
	    int token_start_pos = 0;	//of the current token?
	    
	    while (pos < chars.length) {
	        char ch = chars[pos];
	        switch (state) {
	            case START: {
//	                ch = pos < length ? chars.charAt(pos) : -1;
	            	token_start_pos = pos;
	            	int last_token_length=0;	//CHECK: this may not work in case of string literal? or we consider string literal in one line only? 
	            	
//	            	We move forward the position_in_line only by knowing 
//	            	a) the length of prev token. (when token was added)
//	            	b) if whitespace comes we don't add token. but move the position forward. so pos_in_line should also move. 
//	            	c) when comment comes, our pos_in_line is already updated so don't do anything. 
	            	if(pos>0 && Character.isWhitespace(chars[pos-1])){
	            		if(chars[pos-1]!='\n' && chars[pos-1]!='\r')	last_token_length = 1;
	            	}
	            	else if (tokens != null && !tokens.isEmpty()) {
	            		Token t = tokens.get(tokens.size()-1);
	            		if(t.line == line && t.pos_in_line==pos_in_line) last_token_length = t.length;
	        		}
	            	pos_in_line += last_token_length;
	            	
//	            	simple tokens: separators, simple operators, escape sequence, 	            	
//	            	complex tokens:
//	            		0. 2 letter tokens (operators)
//	            		1. identifier
//	            		2. keyword/reserved word
//	            		3. int literal
//	            		4. boolean literal
//	            		5. string literal
	            	
	            	switch (ch) {
	            	
	//	            	simple token: CHECK If we need to clear the  previous token as well based upon current  token 
	//	            	Hashmap token(keywords) to its token_kind
		            	case '[':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case ']':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '(':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case ')':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case ';':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case ',':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	
		            	case '?':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case ':':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '&':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '|':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '+':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
//		            	case '/':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '%':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
		            	case '@':{tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos++, 1, line, pos_in_line));}	break;
	
		            	case '0':{tokens.add(new Token(Kind.INTEGER_LITERAL, pos++, 1, line, pos_in_line));}	break;
		            	  
		            	case EOFchar:{tokens.add(new Token(Kind.EOF, pos++, 0, line, pos_in_line));}	break;
		            	
		            	default:{
//			            	input char = operator
		            		if(ch=='='){state = State.EQL; pos++;}		//==
		            		else if(ch=='!'){state = State.EXL; pos++;} //!=
		            		else if(ch=='<'){state = State.LTH; pos++;} //<=  | <-
		            		else if(ch=='>'){state = State.GTR; pos++;} //>=
		            		else if(ch=='-'){state = State.SUB; pos++;} //->
		            		else if(ch=='*'){state = State.MUL; pos++;} //**
		            		
//		            		input char = comment
		            		else if(ch=='/'){
		            			if(pos<chars.length-2 && chars[pos+1]=='/'){
		            				state = State.COM; 
		            				pos++;pos++;
		            				pos_in_line+=2;	//Note since its a single line comment(till the end of line), 
//		            								pos_in_line is not required to be maintained. But for EOF char. I am still doing it.   
		            			}else{
		            				tokens.add(new Token((Kind) mp.get(charToString(token_start_pos, 1)), pos, 1, line, pos_in_line));
		            				pos++;			//Here we do not increment pos_in_line because it will be handled at the START.
		            			}
		            		}
		            	
//		            		input char = Identifiers/keywords/boolean literal
		                    else if (Character.isJavaIdentifierStart(ch)) {
		                        state = State.IDN;pos++;
		                    }
		            		
//		            		input char = int literal
		                    else if (Character.isDigit(ch)) {	
		            			state = State.DIG;pos++;
		            		}	//0 is already covered above. 
		                    
//		            		input char = string literal
		                    else if(ch=='"'){
		                    	state = State.STR; pos++;
		                    }
		            		
//		            		input char = ' ', '\t, '\n', '\r', '\f'
		                    else if (Character.isWhitespace(ch)){	
		                    	if(ch=='\r'){
		                    		if(pos<chars.length-2 && chars[pos+1]=='\n')	//CHECK: last input char is at pos chars.length-2    
		                    			pos++;
		                    		line++; pos_in_line=1;
		                    	}
		                    	else if(ch=='\n'){
		                    		line++; pos_in_line=1;
		                    	}
		                   		pos++;
		                    }
		            		
		                    else { //Illegal character - '\b', '\"', '\'',  {, }, #, ~
		                    	String msg = "Illegal character encountered at line:position " + Integer.toString(line) + ":"+ Integer.toString(pos_in_line);
		                    	throw new LexicalException(msg,pos);
		                    	//error();  
		                    }
		            		
		            	}//end case:default
	            	}//switch ch
	            }  break; //case START
	              
//	            comment
	            case COM:{
	            	if(chars[pos]=='\n'||chars[pos]=='\r'|| chars[pos]==EOFchar){	//Note we need to put EOFchar into tokens. So go to Start
	            		state=State.START;	//STOP the comment and process current char in START
	            	} else {
	            		pos++;pos_in_line++;
	            	}
	            }break;
	            
	            //IMP: KEEP CHECK of HOW WE ARE MOVING WITHIN STATES/pos/token_start_pos and update
	            case IDN:{
	                if (Character.isJavaIdentifierStart(ch) || Character.isDigit(ch)) {	//Character.isJavaIdentifierPart(ch)
	                    pos++;
	              } else {
//	           		  	Now check here if its boolean literal, keyword, else put IDENTIFIER
//	            		Note current character = chars[pos] is not part of the identifier/keyword/boolean literal
//	            	  	SO length of token is just pos - token_start_pos
	            	  	String token = charToString(token_start_pos, pos - token_start_pos);
	            	  	if(mp.containsKey(token)){	//so this token is reserved as - Boolean literal or keyword
	            	  		tokens.add(new Token((Kind)mp.get(token), token_start_pos, token.length(), line, pos_in_line));
	            	  	}
	            	  	else {
	            	  		tokens.add(new Token(Kind.IDENTIFIER, token_start_pos, token.length(), line, pos_in_line));
	            	  	}
	            	  	
	                    state = State.START;
	              }
	            }break;
	            
	            case DIG:{
	            	if(Character.isDigit(ch)){
	            		pos++;
	            	}else{	//do not skip current pos
//	            		first check if it is under the integer
	            		try{
	            			Integer.parseInt(charToString(token_start_pos, pos - token_start_pos));
	            			tokens.add(new Token(Kind.INTEGER_LITERAL, token_start_pos, pos - token_start_pos, line, pos_in_line));
		                    state = State.START;	
	            		}catch(Exception e){
	            			String msg = "Integer out of range in line:position "+ Integer.toString(line) + ":"+ Integer.toString(pos_in_line);
	            			throw new LexicalException(msg, pos);
	            		}
	            	}
	            	
	            }break;
//	          So in short a string literal can have an actual java escape sequence apart from line terminators and escaped escape sequences, any all other characters and  not a single slash and " .
	            case STR:{
	            	if(ch=='"'){	//CHECK: throw error at token_start_pos if you find you don;t find closing ". True when "pos"> chars.length
//	            		wind up this string
//	            		Current pos('"') is included in the string literal so length = (pos - token_start_pos +1)
	            		tokens.add(new Token(Kind.STRING_LITERAL, token_start_pos, pos - token_start_pos +1, line, pos_in_line));
	            		state = State.START;
	            	}
//	            	else if (ch==92 && Character.isWhitespace(chars[pos+1])){	//ASCII 92= backslash, 34 = ", 39 = '
//	            	else if (ch==92 && !(chars[pos+1]=='b'||chars[pos+1]=='t' ||chars[pos+1]=='n' ||chars[pos+1]=='f' ||chars[pos+1]=='r'  || chars[pos+1]==39 )){	//ASCII 92= backslash
//	            		if(chars[pos+1]==92){	//next backslash will merge with this one, so skip it 
//	            			pos++;
//	            		}
	            	else if(ch==92){
	            		if(chars[pos+1]=='b'||chars[pos+1]=='t' ||chars[pos+1]=='n' ||chars[pos+1]=='f' ||chars[pos+1]=='r'  || chars[pos+1]==39 ||chars[pos+1]==34||chars[pos+1]==92){
	            			pos++;
	            		}
	            		else {
	            			String msg = "Illegal backslash encountered in string starting at line:position " + Integer.toString(line) + ":"+ Integer.toString(pos_in_line);
	            			throw new LexicalException(msg, pos);
	            		}
	            	}
	            	else if (ch=='\n' || ch=='\r' || ch==EOFchar){	//EOF file encountered.
	            		String msg = "No ending quote found for string starting at line:position " + Integer.toString(line) + ":"+ Integer.toString(pos_in_line);
	            		throw new LexicalException(msg, pos);
	            	}
	            			
	            	pos++;
//	            	NOTE there is a posibility that we may find illegal character here. because we are skipping the START switch case
//	            	if(ch=='\'){	this case: \\\ is covered in START switch case
	            }break;
	            
//	            OPERATOR CASES
	            case EQL: {
	            	if(ch=='='){tokens.add(new Token((Kind)mp.get("=="), token_start_pos, 2, line, pos_in_line)); state=State.START;pos++; }	            	
	            	else{		tokens.add(new Token((Kind)mp.get("="), token_start_pos, 1, line, pos_in_line)); state=State.START;	} //= //process this "pos" again.
	            } break;
	            
	            case EXL: {
	            	if(ch=='='){tokens.add(new Token((Kind)mp.get("!="), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;	}
	            	else{tokens.add(new Token((Kind)mp.get("!"), token_start_pos, 1, line, pos_in_line)); state=State.START;	}	//process this "pos" again.
	            }break;

	            case LTH: {
	            	if(ch=='='){	 tokens.add(new Token((Kind)mp.get("<="), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;}
	            	else if(ch=='-'){tokens.add(new Token((Kind)mp.get("<-"), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;}
	            	else{tokens.add(new Token((Kind)mp.get("<"), token_start_pos, 1, line, pos_in_line)); state=State.START;}	//process this "pos" again.
	            }break;
	            
	            case GTR: {
	            	if(ch=='='){ tokens.add(new Token((Kind)mp.get(">="), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;}
	            	else{		 tokens.add(new Token((Kind)mp.get(">"), token_start_pos, 1, line, pos_in_line));state=State.START;}	//process this "pos" again.
	            }break;
	            
	            case SUB: {
	            	if(ch=='>'){tokens.add(new Token((Kind)mp.get("->"), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;}
	            	else{		tokens.add(new Token((Kind)mp.get("-"), token_start_pos, 1, line, pos_in_line));state=State.START;}
	            }break;
	            
	            case MUL: {
	            	if(ch=='*'){tokens.add(new Token((Kind)mp.get("**"), token_start_pos, 2, line, pos_in_line));state=State.START;pos++;}
	            	else{tokens.add(new Token((Kind)mp.get("*"), token_start_pos, 1, line, pos_in_line));state=State.START;}	//process this "pos" again.
	            }break;

	        }// switch(state)
	    } // while
	    
	    
	    
	    
//	    tokens.add(new Token(Kind.EOF, pos, 0, line, pos_in_line));	//the last token as EOF | Token(Kind kind, int pos, int length, int line, int pos_in_line)
	    return this;
	}
	
	/**
	 * Returns true if the internal iterator has more Tokens
	 * 
	 * @return
	 */
	public boolean hasTokens() {
		return nextTokenPos < tokens.size();
	}

	/**
	 * Returns the next Token and updates the internal iterator so that
	 * the next call to nextToken will return the next token in the list.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * @return
	 */
	public Token nextToken() {
		return tokens.get(nextTokenPos++);
	}
	
	/**
	 * Returns the next Token, but does not update the internal iterator.
	 * This means that the next call to nextToken or peek will return the
	 * same Token as returned by this methods.
	 * 
	 * It is the callers responsibility to ensure that there is another Token.
	 * 
	 * Precondition:  hasTokens()
	 * 
	 * @return next Token.
	 */
	public Token peek() {
		return tokens.get(nextTokenPos);
	}
	
	
	/**
	 * Resets the internal iterator so that the next call to peek or nextToken
	 * will return the first Token.
	 */
	public void reset() {
		nextTokenPos = 0;
	}

	/**
	 * Returns a String representation of the list of Tokens 
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Tokens:\n");
		for (int i = 0; i < tokens.size(); i++) {
			sb.append(tokens.get(i)).append('\n');
		}
		return sb.toString();
	}

}
