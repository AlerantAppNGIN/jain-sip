package test.unit.gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;
import junit.framework.Assert;

/**
 * Tests NameValue intern() usage.
 */
public class StringMsgParserTest extends junit.framework.TestCase {

	public void testValidMessages() throws ParseException {

        String messages[] = {
        		"INVITE sip:user@company.com SIP/2.0\r\n" + 
        		"To: <sip:user@company.com>\r\n" + 
        		"From: <sip:caller@university.edu>\r\n" + 
        		"Call-ID: k345asrl3fdbv@10.0.0.1\r\n" + 
        		"CSeq: 1 INVITE\r\n" + 
        		"Via: SIP/2.0/UDP 135.180.130.133\r\n" + 
        		"Max-Forwards: 0\r\n" + 
        		"Content-Type: application/sdp\r\n" + 
        		"Content-Length: 161\r\n" +
        		"\r\n" + 
        		"v=0\r\n" + 
        		"o=mhandley 29739 7272939 IN IP4 126.5.4.3\r\n" + 
        		"s=SIP Call\r\n" + 
        		"t=0 0\r\n" + 
        		"c=IN IP4 135.180.130.88\r\n" + 
        		"m=audio 492170 RTP/AVP 0 12\r\n" + 
        		"m=video 3227 RTP/AVP 31\r\n" + 
        		"a=rtpmap:31 LPC"
        };

        StringMsgParser p = new StringMsgParser();
        for (int i = 0; i < messages.length; i++ ) {
        	final String msg = messages[i];
            SIPMessage m = p.parseSIPMessage(messages[i].getBytes(), true, false, new ParseExceptionListener() {
				@Override
				public void handleException(ParseException ex, SIPMessage sipMessage, Class headerClass, String headerText,
						String messageText) throws ParseException {
					Assert.fail("Failed to parse message\n" + msg + "\nheaderText=\n" + headerText + "\nmessageText=\n" + messageText);
				}
			});
            Assert.assertEquals(messages[i], m.encode());
        }
	}

}
