package test.unit.gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.javax.sip.header.RequestLine;
import gov.nist.javax.sip.parser.RequestLineParser;
import junit.framework.Assert;

/**
 * Tests NameValue intern() usage.
 */
public class RequestLineParserTest extends junit.framework.TestCase {

	public void testValidLines() throws ParseException {

        String requestLines[] = {
            "REGISTER sip:192.168.0.68 SIP/2.0\r\n",
            "REGISTER sip:company.com SIP/2.0\r\n",
            "INVITE sip:3660@166.35.231.140 SIP/2.0\r\n",
            "INVITE sip:user@company.com SIP/2.0\r\n",
            "REGISTER sip:[2001::1]:5060;transport=tcp SIP/2.0\r\n", // Added by Daniel J. Martinez Manzano <dani@dif.um.es>
            "REGISTER sip:[2002:800:700:600:30:4:6:1]:5060;transport=udp SIP/2.0\r\n", // Added by Daniel J. Martinez Manzano <dani@dif.um.es>
            "REGISTER sip:[3ffe:800:700::30:4:6:1]:5060;transport=tls SIP/2.0\r\n", // Added by Daniel J. Martinez Manzano <dani@dif.um.es>
            "REGISTER sip:[2001:720:1710:0:201:29ff:fe21:f403]:5060;transport=udp SIP/2.0\r\n",
            "OPTIONS sip:135.180.130.133 SIP/2.0\r\n" };
        for (int i = 0; i < requestLines.length; i++ ) {
            RequestLineParser rlp = new RequestLineParser(requestLines[i]);
            RequestLine rl = rlp.parse();
            System.out.println("encoded = " + rl.encode());
            Assert.assertEquals(requestLines[i], rl.encode());
        }
	}

}
