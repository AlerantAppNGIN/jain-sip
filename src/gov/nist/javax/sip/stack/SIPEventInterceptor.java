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
package gov.nist.javax.sip.stack;

import javax.sip.SipStack;
import javax.sip.message.Message;
/**
 * This interface is the solution for https://jain-sip.dev.java.net/issues/show_bug.cgi?id=337
 * It allows to wrap the JSIP pipeline and execute custom analysis logic as SIP messages advance
 * through the pipeline checkpoints.
 * 
 * @author Vladimir Ralev
 *
 */
public interface SIPEventInterceptor {
	
	public static final int FLAG_DROP_MESSAGE = 1;

	/**
	 * This method is called immediately after a SIP message has been parsed and
	 * before it is processed in the JAIN SIP pipeline. Currently only used for non-UDP.
	 * 
	 * @param message
	 */
	void beforeMessage(Message message);

	/**
	 * This method is called immediately after a SIP message has been parsed and
	 * before it is processed in the JAIN SIP pipeline. The application may
	 * return different flags to the stack to alter the behavior of the
	 * pipeline. Currently only used on UDP.
	 *
	 * @param message
	 *            The parsed message.
	 * @param receptionTime
	 *            The reception time of the message.
	 * @return A bitwise OR of the flags defined in this interface, or 0 if no
	 *         special action is to be taken.
	 */
	int beforeMessage(Message message, long receptionTime);

	/**
	 * This message is called after the message has been processed by JAIN SIP
	 * @param message
	 */
	void afterMessage(Message message);
	
	/**
	 * This method is called when the interceptor is initialized. You can perform any initialization here.
	 * 
	 * @param stack
	 */
	public void init(SipStack stack);
	
	/**
	 * This method is called when the interceptor is about to be destroyed. You can perform any cleanup here.
	 */
	public void destroy();
}
