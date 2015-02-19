package edu.aims.mitchell.ian.fortunecookie;

import java.util.Random;

public class Lotto {

	public static String[] random() {
		int[] n = new int[6];
		Random rn = new Random();
		boolean match;
		String[] ns = new String[6];

		for (int i = 0; i < 6; i++) {
			do {
				match = false;
				n[i] = rn.nextInt(49) + 1;

				for (int j = i - 1; j >= 0; j--) {
					if (n[i] == n[j]) {
						match = true;
					}
				}
			} while (match);
		}

		return sort(n);
	}

	public static String[] sort(int[] numbers) {
		String[] ns = new String[6];
		int tmp;

		for (int i = 0; i < 6; i++) {
			for (int j = i + 1; j < 6; j++) {
				if (numbers[i] > numbers[j]) {
					tmp = numbers[i];
					numbers[i] = numbers[j];
					numbers[j] = tmp;
				}
			}
		}

		for (int i = 0; i < 6; i++) {

			ns[i] = String.valueOf(numbers[i]);
		}
		return ns;
	}
}
