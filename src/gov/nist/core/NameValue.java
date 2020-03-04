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
/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
 *******************************************************************************/
package gov.nist.core;

import java.util.Map.Entry;

/*
 * Bug reports and fixes: Kirby Kiem, Jeroen van Bemmel.
 */

/**
 * Generic structure for storing name-value pairs.
 *
 * @version 1.2
 *
 * @author M. Ranganathan <br/>
 *
 *
 *
 */
public class NameValue extends GenericObject implements Entry<String,String> {
    private static StackLogger logger = CommonLogger.getLogger(NameValue.class);

    private static final long serialVersionUID = -1857729012596437950L;

	// By default intern() all names and values, as it saves a lot of memory by only
	// storing duplicate strings once.
	// Good examples are protocol strings that appear in almost every
	// request/response, e.g. "lr", "branch", "transport", "tag", etc.
	// Disable interning if explicitly requested by system property, but allow runtime toggling via setter method if required for whatever reason.
    private static volatile boolean INTERN_STRINGS = !Boolean.getBoolean("gov.nist.core.NameValue.disableStringIntern");
    static {
        if (logger.isLoggingEnabled(LogLevels.TRACE_DEBUG)) {
            logger.logDebug("gov.nist.core.NameValue initially configured to " + (INTERN_STRINGS ? "" : "not ")
            + "intern String values, use `-Dgov.nist.core.NameValue.disableStringIntern=(true|false)` to change this");
        }
    }

    protected boolean isQuotedString;

    protected final boolean isFlagParameter;

    private String separator;

    private String quotes;

    private String name;

    private Object value;

    public NameValue() {
        name = null;
        value = "";
        separator = Separators.EQUALS;
        this.quotes = "";
        this.isFlagParameter = false;
    }

    /**
     * New constructor, taking a boolean which is set if the NV pair is a flag
     *
     * @param n
     * @param v
     * @param isFlag
     */
    public NameValue(String n, Object v, boolean isFlag) {

        // assert (v != null ); // I dont think this assertion is correct mranga

        setName(n);
        setValueAsObject(v);
        separator = Separators.EQUALS;
        quotes = "";
        this.isFlagParameter = isFlag;
    }

    /**
     * Original constructor, sets isFlagParameter to 'false'
     *
     * @param n
     * @param v
     */
    public NameValue(String n, Object v) {
        this(n, v, false);
    }

    /**
     * Set the separator for the encoding method below.
     */
    public void setSeparator(String sep) {
        separator = sep;
    }

    /**
     * A flag that indicates that doublequotes should be put around the value
     * when encoded (for example name=value when value is doublequoted).
     */
    public void setQuotedValue() {
        isQuotedString = true;
        this.quotes = Separators.DOUBLE_QUOTE;
    }

    /**
     * Return true if the value is quoted in doublequotes.
     */
    public boolean isValueQuoted() {
        return isQuotedString;
    }

    public String getName() {
        return name;
    }

    public Object getValueAsObject() {
        return getValueAsObject(true);
    }
    
    public Object getValueAsObject(boolean stripQuotes) {
        if(isFlagParameter)
            return ""; // never return null for flag params
         
        // Issue 315 : (https://jain-sip.dev.java.net/issues/show_bug.cgi?id=315)
        // header.getParameter() doesn't return quoted value
        if(!stripQuotes && isQuotedString)
            return quotes + value.toString() + quotes; // add the quotes for quoted string
        
        return value;
    }

    /**
     * Set the name member
     */
    public void setName(String n) {
        name = INTERN_STRINGS && n != null ? n.intern() : n;
    }

    /**
     * Set the value member
     */
    public void setValueAsObject(Object v) {
        value = INTERN_STRINGS && v instanceof String ? ((String) v).intern() : v;
    }

    /**
     * Get the encoded representation of this namevalue object. Added
     * doublequote for encoding doublequoted values.
     *
     * Bug: RFC3261 stipulates that an opaque parameter in authenticate header
     * has to be:
     * opaque              =  "opaque" EQUAL quoted-string
     * so returning just the name is not acceptable. (e.g. LinkSys phones
     * are picky about this)
     *
     * @since 1.0
     * @return an encoded name value (eg. name=value) string.
     */
    public String encode() {
        return encode(new StringBuilder(200)).toString();
    }

    public StringBuilder encode(StringBuilder buffer) {
        if (name != null && value != null && !isFlagParameter) {
            if (GenericObject.isMySubclass(value.getClass())) {
                GenericObject gv = (GenericObject) value;
                buffer.append(name).append(separator).append(quotes);
                gv.encode(buffer);
                buffer.append(quotes);
                return buffer;
            } else if (GenericObjectList.isMySubclass(value.getClass())) {
                GenericObjectList gvlist = (GenericObjectList) value;
                buffer.append(name).append(separator).append(gvlist.encode());
                return buffer;
            } else if ( value.toString().length() == 0) {
                // opaque="" bug fix - pmusgrave
                /*if (name.toString().equals(gov.nist.javax.sip.header.ParameterNames.OPAQUE))
                    return name + separator + quotes + quotes;
                else
                    return name;*/
                if ( this.isQuotedString ) {
                    buffer.append(name).append(separator).append(quotes).append(quotes);
                    return buffer;
                } else {
                    buffer.append(name).append(separator); // JvB: fix, case: "sip:host?subject="
                    return buffer;
                }
            } else {
                buffer.append(name).append(separator).append(quotes).append(value.toString()).append(quotes);
                return buffer;
            }
        } else if (name == null && value != null) {
            if (GenericObject.isMySubclass(value.getClass())) {
                GenericObject gv = (GenericObject) value;
                gv.encode(buffer);
                return buffer;
            } else if (GenericObjectList.isMySubclass(value.getClass())) {
                GenericObjectList gvlist = (GenericObjectList) value;
                buffer.append(gvlist.encode());
                return buffer;
            } else {
                buffer.append(quotes).append(value.toString()).append(quotes);
                return buffer;
            }
        } else if (name != null && (value == null || isFlagParameter)) {
            buffer.append(name);
            return buffer;
        } else {
            return buffer;
        }
    }

    public Object clone() {
        NameValue retval = (NameValue) super.clone();
        if (value != null)
            retval.value = makeClone(value);
        return retval;
    }

    /**
     * Equality comparison predicate.
     */
    public boolean equals(Object other) {
        if (other == null ) return false;
        if (!other.getClass().equals(this.getClass()))
            return false;
        NameValue that = (NameValue) other;
        if (this == that)
            return true;
        if (this.name == null && that.name != null || this.name != null
                && that.name == null)
            return false;
        if (this.name != null && that.name != null
                && this.name.compareToIgnoreCase(that.name) != 0)
            return false;
        if (this.value != null && that.value == null || this.value == null
                && that.value != null)
            return false;
        if (this.value == that.value)
            return true;
        if (value instanceof String) {
            // Quoted string comparisions are case sensitive.
            if (isQuotedString)
                return this.value.equals(that.value);
            String val = (String) this.value;
            String val1 = (String) that.value;
            return val.compareToIgnoreCase(val1) == 0;
        } else
            return this.value.equals(that.value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map$Entry#getKey()
     */
    public String getKey() {

        return this.name;
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map$Entry#getValue()
     */
    public String getValue() {

        if(value == null)
            return null;
        
        return value.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map$Entry#setValue(java.lang.Object)
     */
    public String setValue(String value) {
        String retval = getValue();
        this.value = INTERN_STRINGS && value != null ? value.intern() : value;
        return retval;

    }
    
    @Override
    public int hashCode() {
        return this.encode().toLowerCase().hashCode();
    }

    public static void setInternStrings(boolean value) {
        if (logger.isLoggingEnabled(LogLevels.TRACE_DEBUG)) {
            logger.logDebug("gov.nist.core.NameValue now configured to " + (INTERN_STRINGS ? "" : "not ") + "intern Strings");
        }
        INTERN_STRINGS = value;
    }
}
