package gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.address.GenericURI;

public class PProfileKeyAddressParser extends AddressParser {
    public PProfileKeyAddressParser(Lexer lexer) {
        super(lexer);
    }

    public PProfileKeyAddressParser(String address) {
        super(address);
    }

    protected AddressImpl nameAddr() throws ParseException {
        if (debug)
            dbg_enter("nameAddr");
        try {
            if (this.lexer.lookAhead(0) == '<') {
                this.lexer.consume(1);
                this.lexer.selectLexer("sip_urlLexer");
                this.lexer.SPorHT();
                int savedLexerPosition = this.lexer.markInputPosition();
                URLParser uriParser = new URLParser((Lexer) lexer);
                GenericURI uri = uriParser.uriReference(true);

                this.lexer.SPorHT();
                if (this.lexer.lookAhead(0) != '>') {
                    // in case the address parser didn't parse the whole uri, we
                    // try to parse it as an absolute-uri
                    this.lexer.rewindInputPosition(savedLexerPosition);
                    AbsoluteURIParser absoluteURIParser = new AbsoluteURIParser((Lexer) lexer);
                    uri = absoluteURIParser.uriReference(true);
                }
                AddressImpl retval = new AddressImpl();
                retval.setAddressType(AddressImpl.NAME_ADDR);
                retval.setURI(uri);
                this.lexer.match('>');
                return retval;
            } else {
                AddressImpl addr = new AddressImpl();
                addr.setAddressType(AddressImpl.NAME_ADDR);
                String name = null;
                if (this.lexer.lookAhead(0) == '\"') {
                    name = this.lexer.quotedString();
                    this.lexer.SPorHT();
                } else
                    name = this.lexer.getNextToken('<');
                addr.setDisplayName(name.trim());
                this.lexer.match('<');
                this.lexer.SPorHT();
                URLParser uriParser = new URLParser((Lexer) lexer);
                GenericURI uri = uriParser.uriReference(true);
                addr.setAddressType(AddressImpl.NAME_ADDR);
                addr.setURI(uri);
                this.lexer.SPorHT();
                this.lexer.match('>');
                return addr;
            }
        } finally {
            if (debug)
                dbg_leave("nameAddr");
        }
    }
}
