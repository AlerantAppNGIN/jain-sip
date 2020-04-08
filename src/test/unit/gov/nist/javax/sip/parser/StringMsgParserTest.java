package test.unit.gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.javax.sip.message.SIPMessage;
import gov.nist.javax.sip.parser.ParseExceptionListener;
import gov.nist.javax.sip.parser.StringMsgParser;
import junit.framework.Assert;

public class StringMsgParserTest extends junit.framework.TestCase {

	public void testValidMessages() throws ParseException {

        String messages[] = {
                "SIP/2.0 200 OK\r\n"
                        + "To: \"The Little Blister\" <sip:LittleGuy@there.com>;tag=469bc066\r\n"
                        + "From: \"The Master Blaster\" <sip:BigGuy@here.com>;tag=11\r\n"
                        + "Via: SIP/2.0/UDP 139.10.134.246:5060;branch=z9hG4bK8b0a86f6_1030c7d18e0_17;received=139.10.134.246\r\n"
                        + "Call-ID: 1030c7d18ae_a97b0b_b@8b0a86f6\r\n"
                        + "CSeq: 1 SUBSCRIBE\r\n"
                        + "Contact: <sip:172.16.11.162:5070>\r\n"
                        + "Content-Length: 0\r\n\r\n",

                "SIP/2.0 180 Ringing\r\n"
                        + "Via: SIP/2.0/UDP 172.18.1.29:5060;branch=z9hG4bK43fc10fb4446d55fc5c8f969607991f4\r\n"
                        + "To: \"0440\" <sip:0440@212.209.220.131>;tag=2600\r\n"
                        + "From: \"Andreas\" <sip:andreas@e-horizon.se>;tag=8524\r\n"
                        + "Call-ID: f51a1851c5f570606140f14c8eb64fd3@172.18.1.29\r\n"
                        + "CSeq: 1 INVITE\r\n" + "Max-Forwards: 70\r\n"
                        + "Record-Route: <sip:212.209.220.131:5060>\r\n"
                        + "Content-Length: 0\r\n\r\n",
                        
                "REGISTER sip:nist.gov SIP/2.0\r\n"
                        + "Via: SIP/2.0/UDP 129.6.55.182:14826\r\n"
                        + "Max-Forwards: 70\r\n"
                        + "From: <sip:mranga@nist.gov>;tag=6fcd5c7ace8b4a45acf0f0cd539b168b;epid=0d4c418ddf\r\n"
                        + "To: <sip:mranga@nist.gov>\r\n"
                        + "Call-ID: c5679907eb954a8da9f9dceb282d7230@129.6.55.182\r\n"
                        + "CSeq: 1 REGISTER\r\n"
                        + "Contact: <sip:129.6.55.182:14826>;methods=\"INVITE, MESSAGE, INFO, SUBSCRIBE, OPTIONS, BYE, CANCEL, NOTIFY, ACK, REFER\"\r\n"
                        + "User-Agent: RTC/(Microsoft RTC)\r\n"
                        + "Event: registration\r\n"
                        + "Allow-Events: presence\r\n"
                        + "Content-Length: 0\r\n\r\n",
                        
                "INVITE sip:littleguy@there.com:5060 SIP/2.0\r\n"
                        + "Via: SIP/2.0/UDP 65.243.118.100:5050\r\n"
                        + "From: \"M. Ranganathan\" <sip:M.Ranganathan@sipbakeoff.com>;tag=1234\r\n"
                        + "To: \"littleguy@there.com\" <sip:littleguy@there.com:5060>\r\n"
                        + "Call-ID: Q2AboBsaGn9!?x6@sipbakeoff.com\r\n"
                        + "CSeq: 1 INVITE\r\n"
                        + "Content-Length: 247\r\n\r\n"
                        + "v=0\r\n"
                        + "o=4855 13760799956958020 13760799956958020 IN IP4  129.6.55.78\r\n"
                        + "s=mysession session\r\n" + "p=+46 8 52018010\r\n"
                        + "c=IN IP4  129.6.55.78\r\n" + "t=0 0\r\n"
                        + "m=audio 6022 RTP/AVP 0 4 18\r\n"
                        + "a=rtpmap:0 PCMU/8000\r\n"
                        + "a=rtpmap:4 G723/8000\r\n"
                        + "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n",
		
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
