package test.unit.gov.nist.javax.sip.parser.extensions;

import java.text.ParseException;

import javax.sip.address.SipURI;
import javax.sip.header.ReasonHeader;

import gov.nist.javax.sip.address.UriDecoder;
import gov.nist.javax.sip.header.ReasonList;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.SIPHeaderList;
import gov.nist.javax.sip.header.extensions.HistoryInfo;
import gov.nist.javax.sip.header.extensions.HistoryInfoHeader;
import gov.nist.javax.sip.header.extensions.HistoryInfoHeader.TargetParam;
import gov.nist.javax.sip.parser.ReasonParser;
import gov.nist.javax.sip.parser.extensions.HistoryInfoParser;
import test.unit.gov.nist.javax.sip.parser.ParserTestCase;

/**
 * Test case for History-Info parser
 *
 */
public class HistoryInfoParserTest extends ParserTestCase {

    public void testParser() {

        String headers[] = {
                "History-Info:<sip:+123456@localhost;user=phone?Reason=SIP%3Bcause%3D302%3Btext%3D%22Moved%20Temporarily%22>;index=1," + 
                "<sip:+234567@localhost;user=phone;cause=302?Reason=SIP%3Bcause%3D302%3Btext%3D%22Moved%20Temporarily%22>;index=1.1," + 
                " <sip:+345678@localhost;user=phone;cause=302;add=test?Reason=SIP%3Bcause%3D486%3Btext%3D%22User%20Busy%22>;index=1.1.1;rc=1.1," + 
                "\t<sip:+456789@localhost;user=phone;cause=302>;index=1.1.1.1\n",
         };
        SIPHeader[] ret =  super.testParser(HistoryInfoParser.class, headers);

        @SuppressWarnings("unchecked")
		SIPHeaderList<HistoryInfo> list0 = (SIPHeaderList<HistoryInfo>) ret[0];
        assertEquals(4, list0.size());
        HistoryInfoHeader hientry_0_2 = list0.get(2);
        System.out.println("3rd hi-entry: " + hientry_0_2);
        System.out.println("Index of 3rd hi-entry: " + hientry_0_2.getIndex());
        assertEquals("1.1.1", hientry_0_2.getIndex());
        System.out.println("hi-target-param of 3rd hi-entry: " + hientry_0_2.getTargetParamType() + " = " + hientry_0_2.getTargetParamValue());
        assertEquals(TargetParam.rc, hientry_0_2.getTargetParamType());
        assertEquals("1.1", hientry_0_2.getTargetParamValue());
        assertEquals("phone", ((SipURI) hientry_0_2.getAddress().getURI()).getUserParam());
        assertEquals("test", ((SipURI) hientry_0_2.getAddress().getURI()).getParameter("add"));
        String reasonH = ((SipURI) hientry_0_2.getAddress().getURI()).getHeader("Reason");
        assertNotNull(reasonH);
        reasonH = "Reason: " +  UriDecoder.decode(reasonH) + "\n"; // parser can only handle decoded full header
        System.out.println("Reason header of 3rd hi-entry: " + reasonH);
        try {
            ReasonList rl = (ReasonList) createParser(ReasonParser.class, reasonH).parse();
			ReasonHeader reason = rl.get(0);
			assertEquals(486, reason.getCause());
			assertEquals("User Busy", reason.getText());
		} catch (ParseException e) {
			e.printStackTrace();
			fail("Unexpected parse error for Reason header in History-Info" + getClass().getName());
		}
    }

}
