package gov.nist.javax.sip.parser.ims;

/*
* Conditions Of Use
*
* This software was developed by employees of the National Institute of
* Standards and Technology (NIST), an agency of the Federal Government.
* Pursuant to title 15 Untied States Code Section 105, works of NIST
* employees are not subject to copyright protection in the United States
* and are considered to be in the public domain.  As a result, a formal
* license is not needed to use the software.
*
* This software is provided by NIST as a service and is expressly
* provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
* OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
* AND DATA ACCURACY.  NIST does not warrant or make any representations
* regarding the use of the software or the results thereof, including but
* not limited to the correctness, accuracy, reliability or usefulness of
* the software.
*
* Permission to use this software is contingent upon your acceptance
* of the terms of this agreement
*
* .
*
*/
import java.text.ParseException;

import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.header.AddressParametersHeader;
import gov.nist.javax.sip.header.SIPHeader;
import gov.nist.javax.sip.header.ims.PProfileKey;
import gov.nist.javax.sip.parser.AddressParser;
import gov.nist.javax.sip.parser.Lexer;
import gov.nist.javax.sip.parser.PProfileKeyAddressParser;
import gov.nist.javax.sip.parser.ParametersParser;
import gov.nist.javax.sip.parser.TokenTypes;

/**
 *
 * @author aayush.bhatnagar Rancore Technologies Pvt Ltd, Mumbai India.
 *
 */
public class PProfileKeyParser extends ParametersParser implements TokenTypes {

	protected PProfileKeyParser(Lexer lexer) {
		super(lexer);

	}

	public PProfileKeyParser(String profilekey) {
		super(profilekey);
	}

	public SIPHeader parse() throws ParseException {
		if (debug)
			dbg_enter("PProfileKey.parse");
		try {

			this.lexer.match(TokenTypes.P_PROFILE_KEY);
			this.lexer.SPorHT();
			this.lexer.match(':');
			this.lexer.SPorHT();

			PProfileKey p = new PProfileKey();
			parseAddressParameter(p);
			return p;

		} finally {
			if (debug)
				dbg_leave("PProfileKey.parse");
		}
	}

	protected void parseAddressParameter(AddressParametersHeader addressParametersHeader) throws ParseException {
		dbg_enter("AddressParametersParser.parse");
		try {
			PProfileKeyAddressParser addressParser = new PProfileKeyAddressParser(this.getLexer());
			AddressImpl addr = addressParser.address(false);
			addressParametersHeader.setAddress(addr);
			lexer.SPorHT();
			char la = this.lexer.lookAhead(0);
			if (this.lexer.hasMoreChars() && la != '\0' && la != '\n' && this.lexer.startsId()) {

				super.parseNameValueList(addressParametersHeader);

			} else {
				super.parse(addressParametersHeader);
			}

		} catch (ParseException ex) {
			throw ex;
		} finally {
			dbg_leave("AddressParametersParser.parse");
		}
	}
}
