package fragment.submissions;

import org.junit.Assert;
import org.junit.Test;

import fragment.submissions.OverlapSystem.Overlap;
import fragment.submissions.OverlapSystem.Worker;

public class overlapTest {

    @Test
    public void test() {

	Overlap test1 = Worker.countOverlap("DEFG", "ABCDEF", 1, false);
	if (test1.getOverlapCount() == 0) {
	    test1 = Worker.countOverlap("DEFG", "ABCDEF", 3, true);
	}
	Assert.assertTrue(test1.getOverlapCount() == 3);

	Overlap test2 = Worker.countOverlap("ABCDEF", "XYZABC", 1, false);
	if (test2.getOverlapCount() == 0) {
	    test2 = Worker.countOverlap("ABCDEF", "XYZABC", 5, true);
	}
	Assert.assertTrue(test2.getOverlapCount() == 3);

	Overlap test3 = Worker.countOverlap("BCDE", "ABCDEF", 1, false);
	if (test3.getOverlapCount() == 0) {
	    test3 = Worker.countOverlap("BCDE", "ABCDEF", 4, true);
	}
	Assert.assertTrue(test3.getOverlapCount() == 4);

	Overlap test4 = Worker.countOverlap("XCDEZ", "ABCDEF", 1, false);
	if (test4.getOverlapCount() == 0) {
	    test4 = Worker.countOverlap("XCDEZ", "ABCDEF", 5, true);
	}
	Assert.assertTrue(test4.getOverlapCount() == 0);
    }

}
