package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class OverlapSystem {

	public static void main(String[] args) throws IOException {
		try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) {
			in.lines().map(Worker::reassemble).forEach(System.out::println);
		}
	}

	public static class Worker {
		public static String reassemble(String input) {
			String[] result = processLineRecursive(input);
			return Arrays.toString(result).replace("[", "").replace("]", "");//just to be sure
		}

		private static String[] processLineRecursive(String inputLine) {
			String[] inputSeparated = inputLine.split(";");

			//copy, don't mess original
			String[] inputProcess = inputSeparated;
			while (inputProcess.length > 1) {
				MatchingPair matchingPair = findBestPair(inputProcess);
				if (matchingPair.getOverlap().getOverlapCount() > 0) {//only merge if get any score
					System.out.println(matchingPair.toString());
					inputProcess = merge(inputProcess, matchingPair);
					Arrays.toString(inputProcess);
				}
			}
			return inputProcess;
		}

		private static String[] merge(String[] inputProcess, MatchingPair matchingPair) {
			String[] mergedArray = new String[inputProcess.length - 1]; //final will have less 1 position
			for (int i = 0; i < inputProcess.length - 1; i++) {
				if (i == matchingPair.getIndex1()) {// if we have to replace this index in this position, lets find original, replace match string text with what it is on index2 (to merge)
					mergedArray[i] = inputProcess[matchingPair.getIndex1()].replace(
							matchingPair.getOverlap().getMatchString(), inputProcess[matchingPair.getIndex2()]);
					 
//					int whereToReplace = inputProcess[matchingPair.getIndex1()].lastIndexOf(matchingPair.getOverlap().getMatchString());
//					String cleanString = inputProcess[matchingPair.getIndex1()].substring(0, whereToReplace);
//					String finalValue=cleanString+inputProcess[matchingPair.getIndex2()];
//					mergedArray[i] =finalValue;
					
				} else if (i >= matchingPair.getIndex2()) {//when we arrive to the second string, we must remove that so it will be i +1
					mergedArray[i] = inputProcess[i + 1];
				} else { //if we are in the middle, the first ones continues first
					mergedArray[i] = inputProcess[i];
				}
			}
			return mergedArray;
		}

		private static MatchingPair findBestPair(String[] inputProcess) {
			MatchingPair match = null;

			for (int i = 0; i < inputProcess.length; i++) {
				//1st and after the next ones
				String currentString = inputProcess[i];
				if (i < inputProcess.length - 1) {
					for (int j = i + 1; j < inputProcess.length; j++) {
						//always next
						String compareString = inputProcess[j];
						Overlap overlap;
						if (currentString.length() < compareString.length()) {
							//lets do it for
							overlap = countOverlap(currentString, compareString, 1, false); //invert false, begin to end
							if (overlap.getOverlapCount() == 0) { //if not encounter in begin to end lets do it another way
								overlap = countOverlap(currentString, compareString, currentString.length() - 1, true);//from the end to backwars
							}
						} else {//maybe not necessary. Just know that current is bigger to compare
							overlap = countOverlap(compareString, currentString, 1, false);
							if (overlap.getOverlapCount() == 0) {
								overlap = countOverlap(compareString, currentString, compareString.length() - 1, true);
							}
						}
						if (match == null || overlap.getOverlapCount() > match.getOverlap().getOverlapCount()) {//keep the best pair, current vs old one, (first time always)
							match = new MatchingPair(i, j, overlap);
						}
					}
				}
			}
			return match;
		}

		public static Overlap countOverlap(String stringToIterate, String stringToCompare, int index,
				boolean inverted) {
			Overlap overlap = new Overlap(0, ""); //init our new score
			if (stringToIterate.length() > 1) {
				if (stringToCompare.contains(stringToIterate)) {//if contains is max score length
					overlap.setOverlapCount(stringToIterate.length());
					overlap.setMatchString(stringToIterate);//string needed to replace afterwards
					return overlap;
				} else {
					if (inverted) {// fallback, true remove end to begin
						int newIndex = index - 1;
						overlap = countOverlap(stringToIterate.substring(0, index), stringToCompare, newIndex,
								inverted);
					} else {//normal mode, remove on begining
						overlap = countOverlap(stringToIterate.substring(index), stringToCompare, index++, inverted);
					}
				}
			}
			return overlap;
		}
	}

	public static class MatchingPair {
		private int index1;//the one that will have more text
		private int index2;// to remove from the list
		private Overlap overlap;//scores, string details

		public MatchingPair(int index1, int index2, Overlap score) {
			super();
			this.index1 = index1;
			this.index2 = index2;
			this.overlap = score;
		}

		public int getIndex1() {
			return index1;
		}

		public void setIndex1(int index1) {
			this.index1 = index1;
		}

		public int getIndex2() {
			return index2;
		}

		public void setIndex2(int index2) {
			this.index2 = index2;
		}

		public Overlap getOverlap() {
			return overlap;
		}

		public void setOverlap(Overlap overlap) {
			this.overlap = overlap;
		}

		@Override
		public String toString() {
			return "MatchingPair [index1=" + index1 + ", index2=" + index2 + ", score=" + overlap + "]";
		}

	}

	public static class Overlap {
		private int overlapCount;//score
		private String matchString; //where the strings match

		public Overlap(int overlapCount, String matchString) {
			super();
			this.overlapCount = overlapCount;
			this.matchString = matchString;
		}

		public int getOverlapCount() {
			return overlapCount;
		}

		public void setOverlapCount(int overlapCount) {
			this.overlapCount = overlapCount;
		}

		public String getMatchString() {
			return matchString;
		}

		public void setMatchString(String matchString) {
			this.matchString = matchString;
		}

		@Override
		public String toString() {
			return "Overlap [overlapCount=" + overlapCount + ", matchString=" + matchString + "]";
		}
	}

}
/*
package fragment.submissions;

import org.junit.Assert;
import org.junit.Test;

import fragment.submissions.MiguelFerreira.Overlap;
import fragment.submissions.MiguelFerreira.Worker;

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

*/