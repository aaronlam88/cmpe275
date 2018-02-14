package util;

import java.util.ArrayList;
import java.util.Random;

public class PickMe {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random rand = new Random(System.currentTimeMillis());

		ArrayList<Integer> picks = new ArrayList<Integer>();
		for (int i = 0; picks.size() != 11; i++) {
			int n = (rand.nextInt(11) + 1);
			if ( picks.contains(n))
				continue;
			
			picks.add(n);
		}
		
		for (Integer p : picks)
			System.out.println(p);
	}
}
