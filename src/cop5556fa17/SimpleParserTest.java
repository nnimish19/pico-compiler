package cop5556fa17;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.Scanner.Token;
import cop5556fa17.SimpleParser.SyntaxException;

import static cop5556fa17.Scanner.Kind.*;

public class SimpleParserTest {

	//set Junit to be able to catch exceptions
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	
	//To make it easy to print objects and turn this output on and off
	static final boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}



	/**
	 * Simple test case with an empty program.  This test 
	 * expects an SyntaxException because all legal programs must
	 * have at least an identifier
	 *   
	 * @throws LexicalException
	 * @throws SyntaxException 
	 */
	@Test
	public void testEmpty() throws LexicalException, SyntaxException {
		String input = "";  //The input is the empty string.  This is not legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //Create a parser
		thrown.expect(SyntaxException.class);
		try {
		parser.parse();  //Parse the program
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}

	
	/** Another example.  This is a legal program and should pass when 
	 * your parser is implemented.
	 * 
	 * @throws LexicalException
	 * @throws SyntaxException
	 */

	@Test
	public void testDec1() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
		parser.parse();
	}
	

	/**
	 * This example invokes the method for expression directly. 
	 * Effectively, we are viewing Expression as the start
	 * symbol of a sub-language.
	 *  
	 * Although a compiler will always call the parse() method,
	 * invoking others is useful to support incremental development.  
	 * We will only invoke expression directly, but 
	 * following this example with others is recommended.  
	 * 
	 * @throws SyntaxException
	 * @throws LexicalException
	 */
	@Test
	public void expression1a() throws SyntaxException, LexicalException {
		String input = "2";	//"5*8";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		SimpleParser parser = new SimpleParser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	
	@Test
	public void expression1b() throws SyntaxException, LexicalException {
		String input = "2?1:0";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		SimpleParser parser = new SimpleParser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	@Test
	public void expression1c() throws SyntaxException, LexicalException {
		String input = "2-(3*4);";
		show(input);
		Scanner scanner = new Scanner(input).scan();  
		show(scanner);   
		SimpleParser parser = new SimpleParser(scanner);  
		parser.expression();  //Call expression directly.  
	}
	
//	TEST CASES
	@Test
	public void testDec1a() throws LexicalException, SyntaxException {
		String input = "prog int k;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
		parser.parse();
	}

	@Test
	public void testDec2() throws LexicalException, SyntaxException {
		String input = "prog float flag;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
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
	public void testDec3() throws LexicalException, SyntaxException {
		String input = "prog int flag";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
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
	public void testDec4() throws LexicalException, SyntaxException {
		String input = "prog boolean flag; int sajk;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
		parser.parse();
	}

	@Test
	public void imgOut() throws LexicalException, SyntaxException {
		String input = "prog abc -> SCREEN;";
		show(input);
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		SimpleParser parser = new SimpleParser(scanner);  //
		parser.parse();
	}

	@Test
	public void function() throws SyntaxException, LexicalException {
		String input = "sin [2,5]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		SimpleParser parser = new SimpleParser(scanner);
		//parser.functionApplication();  //Call expression directly.
		parser.expression();
	}

	@Test
	public void function1() throws SyntaxException, LexicalException {
		String input = "x [2,5]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		SimpleParser parser = new SimpleParser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			//parser.expression(); //This is not throwing an exception
			parser.parse();
			//parser.functionApplication();  //Call expression directly.
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}

	@Test
	public void lhsSelector() throws SyntaxException, LexicalException {
		String input = "[ r,  \n  A]";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		SimpleParser parser = new SimpleParser(scanner);
		parser.lhsSelector();  //Call expression directly.
	}

	@Test
	public void xySelector() throws SyntaxException, LexicalException {
		String input = "x, a";
		show(input);
		Scanner scanner = new Scanner(input).scan();
		show(scanner);
		SimpleParser parser = new SimpleParser(scanner);
		thrown.expect(SyntaxException.class);
		try {
			parser.parse();  //Call expression directly.
		}
		catch (SyntaxException e) {
			show(e);
			throw e;
		}
	}
	
//	MORE TEST CASES
//	@Test
//	public void expression4() throws SyntaxException, LexicalException {
//		String input = 
//"class4 A1=key([->]); d=value(a); s=size(a(10)); ] ";
//		show(input);
//		try {
//			Scanner scanner = new Scanner(input).scan();  
//			show(scanner);   
//			SimpleParser parser = new SimpleParser(scanner);
//			parser.parse();  //Call expression directly.  
//		}
//		catch (Exception e) {	//SyntaxException, LexicalException
//			show(e);
//			throw e;
//		}
//	}
	@Test
	public void expression5() throws SyntaxException, LexicalException {
		String input = "longint var[[x,y]]=5;";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();  
			show(scanner);   
			SimpleParser parser = new SimpleParser(scanner);
			parser.parse();  //Call expression directly.  
		}
		catch (Exception e) {	//SyntaxException, LexicalException
			show(e);
			throw e;
		}
	}
//	@Test
//	public void expression6() throws SyntaxException, LexicalException {
//		String input = "class6 A  [x = @[a+b, !b<<s, [->]; y=@[ @@[a+b:[s:string->a=b;]]];] ";
//		show(input);
//		try {
//			Scanner scanner = new Scanner(input).scan();  
//			show(scanner);   
//			SimpleParser parser = new SimpleParser(scanner);
//			parser.parse();  //Call expression directly.  
//		}
//		catch (Exception e) {	//SyntaxException, LexicalException
//			show(e);
//			throw e;
//		} 
//	}
//	@Test
//	public void expression7() throws SyntaxException, LexicalException {
//		String input = "class7 A  [ \n x = @[a,b,c]; \n y = @[d,e,f]+x; \n ]  ";
//		show(input);
//		try {
//			Scanner scanner = new Scanner(input).scan();  
//			show(scanner);   
//			SimpleParser parser = new SimpleParser(scanner);
//			parser.parse();  //Call expression directly.  
//		}
//		catch (Exception e) {	//SyntaxException, LexicalException
//			show(e);
//			throw e;
//		}  
//	}
//	
}

