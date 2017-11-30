package gov.nist.javax.sip.header.extensions;

import javax.sip.address.Address;
import javax.sip.header.Header;
import javax.sip.header.HeaderAddress;
import javax.sip.header.Parameters;


/**
 * History-Info header as defined by RFC 7044.
 *
 * <p>It is represented as a standard address-list type header:
 * <li> the <code>hi-targeted-to-uri</code> is the {@link Address} of the header
 * <li> the <code>hi-index</code> and <code>hi-target-param</code> have dedicated setters
 * <li> additional <code>hi-extension</code> parameters can be set through the {@link Parameters} interface. 
 * 
 */

public interface HistoryInfoHeader extends HeaderAddress, Parameters, Header {

    /**
     * Name of the header.
     */
    public final static String NAME = "History-Info";

    /** Constants for the <code>hi-target-param</code> types.*/
    public static enum TargetParam {
        /**
         <pre>
         "rc": The hi-targeted-to-URI represents a change in
         Request-URI, while the target user remains the same.  This
         occurs, for example, when the user has multiple AORs as an
         alias.  The "rc" header field parameter contains the value of
         the hi-index in the hi-entry with an hi-targeted-to-uri that
         reflects the Request-URI that was retargeted.
         </pre>
         */
        rc,

        /**
         <pre>
         "mp": The hi-targeted-to-URI represents a user other than the
         target user associated with the Request-URI in the incoming
         request that was retargeted.  This occurs when a request is
         statically or dynamically retargeted to another user
         represented by an AOR unassociated with the AOR of the original
         target user.  The "mp" header field parameter contains the
         value of the hi-index in the hi-entry with an
         hi-targeted-to-uri that reflects the Request-URI that was
         retargeted, thus identifying the "mapped from" target.
         </pre>
         */
        mp,

        /**
         <pre>
         "np": The hi-targeted-to-URI represents that there was no
         change in the Request-URI.  This would apply, for example, when
         a proxy merely forwards a request to a next-hop proxy and loose
         routing is used.  The "np" header field parameter contains the
         value of the hi-index in the hi-entry with an
         hi-targeted-to-uri that reflects the Request-URI that was
         copied unchanged into the request represented by this hi-entry.
         That value will usually be the hi-index of the parent hi-entry
         of this hi-entry.
         </pre>
         */
        np
    }

    /**
     * Set the value of the <code>hi-index</code> header parameter.
     * @param index Must adhere to the ABNF:
     *   <pre>
     *   index-val = number *("." number)
     *   number =  [ %x31-39 *DIGIT ] DIGIT
     *   </pre>
     *  @throws NullPointerException if the index is null
     *  @throws IllegalArgumentException if the parameter is syntactically invalid
     */
    public void setIndex(String index);

    /** Get the value of the <code>hi-index</code> header parameter. */
    public String getIndex();

    /**
     * Set a <code>hi-target-param</code> header parameter.
     * @param target one of the specified names for this parameter
     * @param value an <code>index-val</code> as value, see {@link #setIndex(String)} for accepted format
     * @throws NullPointerException if either of the parameters is null
     * @throws IllegalArgumentException if the value is not syntactically correct. Note that the value is
     *         not checked semantically, i.e. it can reference a non-existent hi-entry.
     */
    public void setTargetParam(TargetParam target, String value);

    public TargetParam getTargetParamType();

    public String getTargetParamValue();
}
