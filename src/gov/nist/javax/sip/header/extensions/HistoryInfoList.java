package gov.nist.javax.sip.header.extensions;

import gov.nist.javax.sip.header.SIPHeaderList;

/**
 * List of HistoryInfo headers.
 */
public class HistoryInfoList extends SIPHeaderList<HistoryInfo> {

	private static final long serialVersionUID = 2995763757016623192L;

	public Object clone() {
		HistoryInfoList retval = new HistoryInfoList();
		retval.clonehlist(this.hlist);
		return retval;
	}

	public HistoryInfoList() {
		super(HistoryInfo.class, HistoryInfoHeader.NAME);

	}

}
