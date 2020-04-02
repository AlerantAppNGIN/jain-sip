package test.unit.gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.javax.sip.header.StatusLine;
import gov.nist.javax.sip.parser.StatusLineParser;
import junit.framework.Assert;

/**
 * Tests NameValue intern() usage.
 */
public class StatusLineParserTest extends junit.framework.TestCase {

	public void testValidLines() throws ParseException {

		String[] okStatusLines = {
				"SIP/2.0 200 OK\r\n",
				"SIP/2.0 183 Session Progress\r\n",
				"SIP/2.0 500 OK bad things happened\r\n"
				};
		for (int i = 0; i < okStatusLines.length; i++) {
			StatusLineParser slp = new StatusLineParser(okStatusLines[i]);
			StatusLine sl = slp.parse();
			Assert.assertEquals(okStatusLines[i], sl.encode());
		}
	}

	public void testInvalidLines() {
		String[] invalidStatusLines = {
				"BOO 200 OK\r\n",
				"SIP/2.0 notNumber OK\r\n",
				};
		for (int i = 0; i < invalidStatusLines.length; i++) {
			StatusLineParser slp = new StatusLineParser(invalidStatusLines[i]);
			try {
				StatusLine sl = slp.parse();
				System.err.println("Parsed invalid line: " + sl.encode());
				Assert.fail("should have failed on invalid status line: " + invalidStatusLines[i]);
			} catch (ParseException e) {
				System.out.println("Got expected exception: " + e.getMessage());
			}
		}
	}
}
