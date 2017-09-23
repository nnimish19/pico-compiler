/**
 * /**
 * JUunit tests for the Scanner for the class project in COP5556 Programming Language Principles 
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

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556fa17.Scanner.LexicalException;
import cop5556fa17.Scanner.Token;

import static cop5556fa17.Scanner.Kind.*;

public class ScannerTest {

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
	 *Retrieves the next token and checks that it is an EOF token. 
	 *Also checks that this was the last token.
	 *
	 * @param scanner
	 * @return the Token that was retrieved
	 */
	
	Token checkNextIsEOF(Scanner scanner) {
		Scanner.Token token = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF, token.kind);
		assertFalse(scanner.hasTokens());
		return token;
	}


	/**
	 * Retrieves the next token and checks that its kind, position, length, line, and position in line
	 * match the given parameters.
	 * 
	 * @param scanner
	 * @param kind
	 * @param pos
	 * @param length
	 * @param line
	 * @param pos_in_line
	 * @return  the Token that was retrieved
	 */
	Token checkNext(Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line) {
		Token t = scanner.nextToken();
		assertEquals(scanner.new Token(kind, pos, length, line, pos_in_line), t);
		return t;
	}

	/**
	 * Retrieves the next token and checks that its kind and length match the given
	 * parameters.  The position, line, and position in line are ignored.
	 * 
	 * @param scanner
	 * @param kind
	 * @param length
	 * @return  the Token that was retrieved
	 */
	Token check(Scanner scanner, Scanner.Kind kind, int length) {
		Token t = scanner.nextToken();
		assertEquals(kind, t.kind);
		assertEquals(length, t.length);
		return t;
	}

	/**
	 * Simple test case with a (legal) empty program
	 *   
	 * @throws LexicalException
	 */
	@Test
	public void testEmpty() throws LexicalException {
		String input = "";  //The input is the empty string.  This is legal
		show(input);        //Display the input 
		Scanner scanner = new Scanner(input).scan();  //Create a Scanner and initialize it
		show(scanner);   //Display the Scanner
		checkNextIsEOF(scanner);  //Check that the only token is the EOF token.
	}
	
	/**
	 * Test illustrating how to put a new line in the input program and how to
	 * check content of tokens.
	 * 
	 * Because we are using a Java String literal for input, we use \n for the
	 * end of line character. (We should also be able to handle \n, \r, and \r\n
	 * properly.)
	 * 
	 * Note that if we were reading the input from a file, as we will want to do 
	 * later, the end of line character would be inserted by the text editor.
	 * Showing the input will let you check your input is what you think it is.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void testSemi() throws LexicalException {
		String input = ";;\n;;";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
		checkNext(scanner, SEMI, 0, 1, 1, 1);//Scanner scanner, Scanner.Kind kind, int pos, int length, int line, int pos_in_line)
		checkNext(scanner, SEMI, 1, 1, 1, 2);
		checkNext(scanner, SEMI, 3, 1, 2, 1);
		checkNext(scanner, SEMI, 4, 1, 2, 2);
		checkNextIsEOF(scanner);
	}
		
	/**
	 * This example shows how to test that your scanner is behaving when the
	 * input is illegal.  In this case, we are giving it a String literal
	 * that is missing the closing ".  
	 * 
	 * Note that the outer pair of quotation marks delineate the String literal
	 * in this test program that provides the input to our Scanner.  The quotation
	 * mark that is actually included in the input must be escaped, \".
	 * 
	 * The example shows catching the exception that is thrown by the scanner,
	 * looking at it, and checking its contents before rethrowing it.  If caught
	 * but not rethrown, then JUnit won't get the exception and the test will fail.  
	 * 
	 * The test will work without putting the try-catch block around 
	 * new Scanner(input).scan(); but then you won't be able to check 
	 * or display the thrown exception.
	 * 
	 * @throws LexicalException
	 */
	@Test
	public void failUnclosedStringLiteral() throws LexicalException {
		String input = "\" greetings  ";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			new Scanner(input).scan();
		} catch (LexicalException e) {  //
			show(e);
			assertEquals(13,e.getPos());
			throw e;
		}
	}

	@Test
	public void testGeneral1() throws LexicalException {
		String input = "a+=b";
		show(input);
		try{
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		}catch(LexicalException e){
			show(e);
			throw e;
		}
		
		
	}
	@Test
	public void testGeneral2() throws LexicalException {
		String input = "a+ =b";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
	}
	@Test
	public void testGeneral3() throws LexicalException {
		String input = "false == truely yours";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
	}
	@Test
	public void testGeneral4() throws LexicalException {
		String input = "true>>0123";
		Scanner scanner = new Scanner(input).scan();
		show(input);
		show(scanner);
	}
	@Test
	public void testGeneral5() throws LexicalException {
		String input = "if(a!=b)print\n \"do\nne\"; ";
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral6() throws LexicalException {
		String input = "sin(a!=b)print \"done\"; ";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral7() throws LexicalException {
		String input = "SIN(a!=b)print \"done\"; //halo";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral8() throws LexicalException {
		String input = "a,b,c\n****\n***";
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral9() throws LexicalException {
		String input = "[9][\"/\"//b/\"\"]]";		//	"/"//b/""	>look closely, its a string followed by comment		
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral10() throws LexicalException {
		String input = "[10]\"str\n\" ?";	//No ending quote found for string starting at line:position 1:1 
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral11() throws LexicalException {
		String input = "[11]\"abc\t\"";		//length of string =6
		show(input);
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral12() throws LexicalException {
		String input = "[12]\"\\\t\"";		//invalid string. input = "\   "
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral13() throws LexicalException {
		String input = "[13]\"\\t\"";		//invalid string. input = "\t"
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral14() throws LexicalException {
		String input = "[14]\"\\a\"";		//"\a"		//Illegal backslash encountered in string starting at line:position 1:1
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}

	@Test
	public void testGeneral15() throws LexicalException {
		String input = "[15]\"\\\t\"";		//"\	"	//Illegal backslash encountered in string starting at line:position 1:1
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral16() throws LexicalException {
		String input = "[16]\"\\\\t\"";		//"\\t"
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral17() throws LexicalException {
		String input = "[17]\"\\\\\\n\"";		//	"\\\n"
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral18() throws LexicalException {
		String input = "[18][\" \\\\\" \"]";  //[" \\" "]		//No ending quote found for string starting at line:position 1:8
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}	
	@Test
	public void testGeneral19() throws LexicalException {
		String input = "[19][[\"\\\\\\\"]]";	//  [["\\\"]]
		show(input);
		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral20() throws LexicalException {
		String input = "[20][\"\\\\\\\"\"]]";	//  [["\\\""]] 
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral21() throws LexicalException {
		String input = "   \r\n    \r\n   ";//"b\bab"; //"\"abc\\\"a\"";//"\"abc\\\"a"; //"\" greet\\ings\"";//2147483647";	//  [["\\\""]] 
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral22() throws LexicalException {
		String input = "/ /// Hoping this is /// still in comment. \r\n / //";//"b\bab"; //"\"abc\\\"a\"";//"\"abc\\\"a"; //"\" greet\\ings\"";//2147483647";	//  [["\\\""]] 
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
	@Test
	public void testGeneral23() throws LexicalException {
		String input = "<=<<- ->>=<<-";//"<---> <--";	//" ->>= ->>>= "; 
		show(input);
//		thrown.expect(LexicalException.class);  //Tell JUnit to expect a LexicalException
		try {
			Scanner scanner = new Scanner(input).scan();
			show(scanner);
		} catch (LexicalException e) {
			show(e);
			throw e;
		}
	}
}
