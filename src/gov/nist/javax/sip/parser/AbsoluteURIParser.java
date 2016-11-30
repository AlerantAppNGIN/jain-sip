package gov.nist.javax.sip.parser;

import java.text.ParseException;

import gov.nist.core.Token;
import gov.nist.javax.sip.address.GenericURI;

public class AbsoluteURIParser extends URLParser {
    public AbsoluteURIParser(String url) {
        super(url);
    }

    // public tag added - issued by Miguel Freitas
    public AbsoluteURIParser(Lexer lexer) {
        super(lexer);
    }

    public GenericURI uriReference(boolean inBrackets) throws ParseException {
        if (debug)
            dbg_enter("uriReference");
        GenericURI retval = null;
        Token[] tokens = lexer.peekNextToken(2);
        String urlString = uricString();
        try {
            retval = new GenericURI(urlString);
        } catch (ParseException ex) {
            throw createParseException(ex.getMessage());
        } finally {
            if (debug)
                dbg_leave("uriReference");
        }
        return retval;
    }
}
