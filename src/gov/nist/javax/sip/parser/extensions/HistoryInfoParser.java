package gov.nist.javax.sip.parser.extensions;

import java.text.ParseException;

import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.extensions.HistoryInfo;
import gov.nist.javax.sip.header.extensions.HistoryInfoList;
import gov.nist.javax.sip.header.extensions.HistoryInfoHeader.TargetParam;
import gov.nist.javax.sip.parser.AddressParametersParser;
import gov.nist.javax.sip.parser.Lexer;
import gov.nist.javax.sip.parser.TokenTypes;

/**
 * A parser for the SIP History-Info header.
 */
public class HistoryInfoParser extends AddressParametersParser {

	public HistoryInfoParser(String historyInfo) {
		super(historyInfo);
	}

	protected HistoryInfoParser(Lexer lexer) {
		super(lexer);
		this.lexer = lexer;
	}

	public SIPHeader parse() throws ParseException {
		// past the header name and the colon.
		headerName(TokenTypes.HISTORY_INFO);
		HistoryInfoList retval = new HistoryInfoList();
		while (true) {
			HistoryInfo hientry = new HistoryInfo();
			super.parse(hientry);
			handleSpecialParameters(hientry);
			retval.add(hientry);
			this.lexer.SPorHT();
			char la = lexer.lookAhead(0);
			if (la == ',') {
				this.lexer.match(',');
				this.lexer.SPorHT();
			} else if (la == '\n' || la == '\0')
				break;
			else
				throw createParseException("unexpected char");
		}
		return retval;
	}

	/**
	 * Checks and sets special parameters parsed by the generic address header
	 * parser according to History-Info logic.
	 *
	 * @throws ParseException
	 */
	private void handleSpecialParameters(HistoryInfo hientry) throws ParseException {
		String index = hientry.getParameter("index");
		if (index != null) {
			hientry.setIndex(index);
		} else {
			throw createParseException("missing index parameter");
		}
		for (TargetParam tp : TargetParam.values()) {
			String val = hientry.getParameter(tp.toString());
			if (val != null) {
				hientry.setTargetParam(tp, val);
				break; // set will remove the other 2 values, no point in continuing
			}
		}

	}

}
