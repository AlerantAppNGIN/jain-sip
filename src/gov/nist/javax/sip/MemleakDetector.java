package gov.nist.javax.sip;

import gov.nist.core.CommonLogger;
import gov.nist.core.StackLogger;

/**
 * Scanner thread to check for stuck transactions and possibly other sources of
 * memleaks.
 *
 */
public class MemleakDetector {

	private static StackLogger logger = CommonLogger.getLogger(MemleakDetector.class);

	private SipStackImpl sipStack;

	public MemleakDetector(SipStackImpl sipStackImpl) {
		this.sipStack = sipStackImpl;
	}
	
	public void start() {
		Thread myThread = new Thread(this::run, "MemleakDetectorThread");
		myThread.setDaemon(true);
		myThread.start();
	}

	private void run() {
		logger.logInfo("Memleak detector starting");
		try {
			while (true) {

				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					logger.logInfo("Memleak detector sleep interrupted");
				}
				if (!sipStack.isAlive()) {
					logger.logInfo("Stopped memleak detector (stack stopped).");
					break;
				}

				// convert to ms + add 10% to allow the normal timeouts to happen, if any
				long minimumAgeMs = 1100
						* Math.max(sipStack.getMaxTxLifetimeInvite(), sipStack.getMaxTxLifetimeNonInvite());
				if (minimumAgeMs < 0)
					minimumAgeMs = 3600 * 1000; // 1h default
				logger.logInfo("Checking for leaked transactions (created more than " + minimumAgeMs + "ms ago)");
				try {
					sipStack.removeLeakedTransactions(minimumAgeMs);
				} catch (Throwable t) {
					logger.logError("Exception while performing leak detection in stack");
					logger.logException(t);
				}

			}
		} finally {
			logger.logInfo("Memleak detector exited");
		}
	}

}
