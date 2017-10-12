package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.AST.*;
import cop5556fa17.Parser.SyntaxException;
import static cop5556fa17.Scanner.Kind.*;

public class ParserTest {

	// set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	// To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}

	/**
	 * Simple test case with an empty program. This test expects an exception
	 * because all legal programs must have at least an identifier
	 * 
	 * @throws LexicalException
	 * @throws SyntaxException
	 */
	@Test
	public void testEmpty() throws LexicalException, SyntaxException {
		String input = ""; // The input is the empty string. Parsing should fail
		show(input); // Display the input
		Scanner scanner = new Scanner(input).scan(); // Create a Scanner and
														// initialize it
		show(scanner); // Display the tokens
		Parser parser = new Parser(scanner); //Create a parser
		thrown.expect(SyntaxException.class);
		try {
			ASTNode ast = parser.parse();; //Parse the program, which should throw an exception
		} catch (SyntaxException e) {
			show(e);  //catch the exception and show it
			throw e;  //rethrow for Junit
		}
	}


	@Test
	public void testNameOnly() throws LexicalException, SyntaxException {
		String input = "prog";  //Legal program with only a name
		show(input);            //display input
		Scanner scanner = new Scanner(input).scan();   //Create scanner and create token list
		show(scanner);    //display the tokens
		Parser parser = new Parser(scanner);   //create parser
		Program ast = parser.parse();          //parse program and get AST
		show(ast);                             //Display the AST
		assertEquals(ast.name, "prog");        //Check the name field in the Program object
		assertTrue(ast.decsAndStatements.isEmpty());   //Check the decsAndStatements list in the Program object.  It should be empty.
	}

	@Test
	public void testDec1() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan(); 
		show(scanner); 
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
		assertEquals(ast.name, "prog"); 
		//This should have one Declaration_Variable object, which is at position 0 in the decsAndStatements list
		Declaration_Variable dec = (Declaration_Variable) ast.decsAndStatements
				.get(0);  
		assertEquals(KW_int, dec.type.kind);
		assertEquals("k", dec.name);
		assertNull(dec.e);
	}

//--------------
	@Test
	public void expression1a() throws SyntaxException, LexicalException {
		String input = "2";	//"5*8";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	
	@Test
	public void expression1b() throws SyntaxException, LexicalException {
		String input = "2?1:0";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	@Test
	public void expression1c() throws SyntaxException, LexicalException {
		String input = "2-(3*4);";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	
//	TEST CASES
	@Test
	public void testDec1a() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		Parser parser = new Parser(scanner);  //
		parser.parse();
	}

	@Test
	public void testDec1b() throws LexicalException, SyntaxException {
		String input = "prog float flag;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		Parser parser = new Parser(scanner);  //
		thrown.expect(SyntaxException.class);
		try {
			parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}

	@Test
	public void testDec1c() throws LexicalException, SyntaxException {
		String input = "prog int flag";
		show(input);
		thrown.expect(SyntaxException.class);
		try {
			Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
			show(scanner);   //Display the Scanner
			Parser parser = new Parser(scanner);
			parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}

	@Test
	public void testDec1d() throws LexicalException, SyntaxException {
		String input = "prog boolean flag; int sajk;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		Parser parser = new Parser(scanner);  //
		parser.parse();
	}

	@Test
	public void imgOut() throws LexicalException, SyntaxException {
		String input = "prog abc -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		Parser parser = new Parser(scanner);  //
		parser.parse();
	}

	@Test
	public void function1a() throws SyntaxException, LexicalException {
		String input = "sin [2,5]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		Parser parser = new Parser(scanner);
		//parser.functionApplication();  //Call expression directly.
		parser.expression();
	}

	@Test
	public void function1b() throws SyntaxException, LexicalException {
		String input = "x [2,5]";
		show(input);
		thrown.expect(SyntaxException.class);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
			Parser parser = new Parser(scanner);
			parser.parse();
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}

	@Test
	public void testcase() throws SyntaxException, LexicalException {
		String input = "x, a";
		show(input);
		thrown.expect(SyntaxException.class);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
			Parser parser = new Parser(scanner);
			parser.parse();
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}
	
	@Test
	public void expression5() throws SyntaxException, LexicalException {
		String input = "longint var[[x,y]]=5;";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();  
			show(scanner);   
			Parser parser = new Parser(scanner);
			parser.parse();  //Call expression directly.  
		}
		catch (Exception e) {	//SyntaxException, LexicalException
			show(e);
			throw e;
		}
	}
	
//MORE Test Cases
	@Test
	public void testDec2() throws LexicalException, SyntaxException {
//prog  int k;  image[56/34,67/34] cgh ;  image svf <- @(x+y+2/4*5*r*Z); image vkdj ;  boolean k = true; int k = a + 76/9 * 67;  url vgjvbhvj = "vhvjhm";  url fbdjob = vjsdlv ; file file_name = @(x+y+a*X) ;  //this is a commment  ; 		
		String input = "prog "
		        + " int k; "
		        + " image[56/34,67/34] cgh ; "
		        + " image svf <- @(x+y+2/4*5*r*Z);"
		        + " image vkdj ; "
		        + " boolean k = true;"
		        + " int k = a + 76/9 * 67; "
		        + " url vgjvbhvj = \"vhvjhm\"; "
		        + " url fbdjob = vjsdlv ;"
		        + " file file_name = @(x+y+a*X) ; "
		        + " //this is a commment  ; ";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();  
//			show(scanner);   
			Parser parser = new Parser(scanner);
			parser.parse();  //Call expression directly.  
		}
		catch (Exception e) {	//SyntaxException, LexicalException
			show(e);
			throw e;
		}  
	}
	//positive test cases

	@Test
    public void statement() throws LexicalException, SyntaxException {
        String input = "prog sai [[x,y]] = (67/x*y);"
                + "sgf[[r,A]] = 534657+x ;"
                + "nath -> screen ;"
                + "gsf ->vfgh ;"
                + "reddy <- @x+y ;"
                + "dtgbg = u[534/x,67+y];";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.parse();
    }
	
	@Test
    public void expressionabs() throws LexicalException, SyntaxException {
        String input = "abs(x/y);";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	@Test
    public void expressionsin() throws LexicalException, SyntaxException {
        String input = "sin[x/y,a/A];";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	@Test
    public void expressionsin1() throws LexicalException, SyntaxException {
        String input = "sin(DEF_X);";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	@Test
    public void expressioncos() throws LexicalException, SyntaxException {
        String input = "cos(DEF_Y);";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	@Test
    public void expressionatan() throws LexicalException, SyntaxException {
        String input = "atan[R,r] / abs(34+23+A-R/Y);";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	@Test
    public void expressioncartx() throws LexicalException, SyntaxException {
        String input = "cart_x[(45*(X)/Z)>54*Y,a+!A+R+r+y*Y-DEF_X /DEF_Y > (x/X)]; ";
        show(input);
        Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
        //show(scanner);   //Display the Scanner
        Parser parser = new Parser(scanner);  //
        parser.expression();
    }
	
//	---------------
	@Test
	public void testcase0() throws SyntaxException, LexicalException {
		String input =  "(6*2/23/4*22*sin(x))>=(abs(6*2*12)+cart_x[x,y]+cart_y[(6/23),(7/23)]+polar_a[6/2/2,2/3/4]+polar_r(z,y,x))";  //Should fail as () can hold only expression
//(6*2/23/4*22*sin(x))>=(abs(6*2*12)+cart_x[x,y]+cart_y[(6/23),(7/23)]+polar_a[6/2/2,2/3/4]+polar_r(z,y,x))
		show(input);
		Scanner scanner = new Scanner(input).scan();  
//		show(scanner);   
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.expression();   //Parse the parse
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}
	
	@Test
	public void testcase1() throws SyntaxException, LexicalException {
		String input =  "(6*2/23/4*22*sin(x))==(abs(6*2*12)+cart_x[x,y]+cart_y[(6/23),(7/23)]+polar_a[6/2/2,2/3/4]+polar_r(z))";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		Parser parser = new Parser(scanner);
//		parser.expression();
		Expression ast = parser.expression();
		show(ast);
	}
	
	@Test
	public void testcase2() throws SyntaxException, LexicalException {
		String input =  "imageProgram image imageName;"
				+ "\n imageName->abcdpng; "
				+ "\n imageName -> SCREEN; "
				+ "\n imageName <- \"awesome\";"
				+ "\n imageName <- @express; \n"
				+ "\n imageName <- abcdpng;";  // Image related Test cases
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast); 
	}
	
	@Test
	public void testcase3() throws SyntaxException, LexicalException { 
		String input =  "assign int abc=123456;\n"
				+ "abc[[x,y]]=123456;\n"
				+ "abc[[r,A]]=123244;\n";//Assignment statement
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		Parser parser = new Parser(scanner);
		Program ast = parser.parse();
		show(ast);
	}
}
