package test.unit.gov.nist.core;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nist.core.NameValue;

/**
 * Tests NameValue intern() usage.
 */
public class NameValueInternTest extends junit.framework.TestCase {

	final int count = 10000;
	final int mod = 100;

	public void testIntern() throws ParseException {
		performTest(mod, true);
	}

	public void testNoIntern() throws ParseException {
		performTest(count, false);
	}

	private void performTest(int expectedInstances, boolean intern) throws ParseException {
		NameValue.setInternStrings(intern);
		String name = "param", basevalue = "sdfasdffdagssdfegeilugnergkaegerngergaergergega";
		List<NameValue> nvl = new ArrayList<>(count);
		long memusageBefore, memusageAfter, memusageAfterGc;
		MemoryMXBean membean = ManagementFactory.getMemoryMXBean();
		System.gc();
		memusageBefore = membean.getHeapMemoryUsage().getUsed();
		// we add a lot of name-value pairs, but the actual value set for both the name
		// and value parts is limited to <different> different values
		for (int i = 0; i < count; i++) {
			NameValue nv = new NameValue(name + (i % mod), basevalue + (i % mod));
			nvl.add(nv);
		}
		System.gc();
		memusageAfter = membean.getHeapMemoryUsage().getUsed();
		// this is only informational, not precise
		Set<Integer> differentNames = new HashSet<>(expectedInstances);
		Set<Integer> differentValues = new HashSet<>(expectedInstances);
		for (NameValue nv : nvl) {
			differentNames.add(System.identityHashCode(nv.getName()));
			differentValues.add(System.identityHashCode(nv.getValue()));
		}
		assertEquals(expectedInstances, differentNames.size());
		assertEquals(expectedInstances, differentValues.size());
		nvl.clear();
		nvl = null;
		System.gc();
		memusageAfterGc = membean.getHeapMemoryUsage().getUsed();
		System.out.println("Intern: " + intern + ", before: " + memusageBefore + ", after: " + memusageAfter
				+ ", after GC: " + memusageAfterGc);
	}

	/** This test just makes sure that intern()-ed strings get cleaned up
	 *  when app-level references are lost, and no long-term leak occurs
	 *  from interning names/values that are only used once.
	 *  This actually tests the String implementation, not NameValue.*/
	public void testNoLeakWithIntern() {
		long memusageBefore, memusageAfterGc;
		MemoryMXBean membean = ManagementFactory.getMemoryMXBean();
		System.gc();
		memusageBefore = membean.getHeapMemoryUsage().getUsed();
		String s = "a";
		for (int i = 0; i < 32; i++) {
			try {
				s = (s + s).intern(); // this could grow to a 4GB String! (2^32 chars) 
			} catch (OutOfMemoryError e) {
				break;
			}
		}
		s = null;
		System.gc();
		memusageAfterGc = membean.getHeapMemoryUsage().getUsed();
		System.out.println("Intern leak test; before: " + memusageBefore + ", after GC: " + memusageAfterGc);
		assertTrue(memusageAfterGc < memusageBefore);
	}
}
