package gov.nist.javax.sip.header.extensions;

import java.text.ParseException;
import java.util.regex.Pattern;

import javax.sip.header.ExtensionHeader;

import gov.nist.javax.sip.address.AddressImpl;
import gov.nist.javax.sip.header.AddressParametersHeader;

public class HistoryInfo extends AddressParametersHeader implements HistoryInfoHeader, ExtensionHeader {

	private static final long serialVersionUID = 1L;

	/**
	 * constructor
	 *
	 * @param address
	 *            the hi-targeted-to-uri
	 */
	public HistoryInfo(AddressImpl address) {
		super(NAME);
		this.address = address;
	}

	/**
	 * default constructor
	 */
	public HistoryInfo() {
		super(NAME);
	}

	/**
	 * Encode into canonical form.
	 *
	 * @return String containing the canonically encoded header.
	 */
	public StringBuilder encodeBody(StringBuilder retval) {
		// StringBuilder retval = new StringBuilder(200);
		if (address.getAddressType() == AddressImpl.ADDRESS_SPEC) {
			retval.append(LESS_THAN);
		}
		address.encode(retval);
		if (address.getAddressType() == AddressImpl.ADDRESS_SPEC) {
			retval.append(GREATER_THAN);
		}

		if (!parameters.isEmpty()) {
			retval = retval.append(SEMICOLON);
			retval = this.parameters.encode(retval);
		}
		return retval;
	}

	public Object clone() {
		HistoryInfo retval = (HistoryInfo) super.clone();
		return retval;
	}

	public void setValue(String value) throws ParseException {
		throw new UnsupportedOperationException();
	}

	private static final Pattern indexPattern = Pattern.compile("(?:0|[1-9][0-9]*)(?:[.](?:0|[1-9][0-9]*))*");

	private void checkIndexValue(String value) {
		if (value == null)
			throw new NullPointerException("History-Info index value must not be null");
		// TODO maybe improve performance
		if (!indexPattern.matcher(value).matches()) {
			throw new IllegalArgumentException("'" + value + "' does not match the ABNF of a valid History-Info index");
		}
	}

	@Override
	public void setIndex(String index) {
		checkIndexValue(index);
		this.parameters.set("index", index);
	}

	@Override
	public String getIndex() {
		return this.parameters.getParameter("index");
	}

	@Override
	public void setTargetParam(TargetParam target, String value) {
		if (target == null)
			throw new NullPointerException("History-Info target parameter type must not be null");
		checkIndexValue(value);
		// only one of the types can appear, always remove the other ones
		for (TargetParam tp : TargetParam.values()) {
			if (tp == target)
				this.parameters.set(target.toString(), value);
			else
				this.parameters.remove(tp.toString());
		}
	}

	@Override
	public TargetParam getTargetParamType() {
		for (TargetParam tp : TargetParam.values()) {
			// only one of them can be present
			if (this.parameters.containsKey(tp.toString()))
				return tp;
		}
		return null;
	}

	@Override
	public String getTargetParamValue() {
		String ret;
		for (TargetParam tp : TargetParam.values())
			if (null != (ret = this.parameters.getParameter(tp.toString())))
				return ret;
		return null;
	}

	// TODO: checks can be circumvented by using the generic setParameter* methods.
	// Those should be overridden and if the parameter name matches one of the
	// special parameters, delegate to the special setters that enforce the checks

}
