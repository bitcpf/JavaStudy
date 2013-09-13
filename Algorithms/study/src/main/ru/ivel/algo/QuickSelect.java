package ru.ivel.algo;

/**
 * QuickSelect
 * 
 * @author Игорь Елькин (ielkin@nvision-group.com)
 */
public class QuickSelect {

	private int[] numbers;

	public int select(int[] a, int k) {
		this.numbers = a;
		int lo = 0;
		int hi = a.length - 1;
		while (lo < hi) {
			int j = partition(lo, hi);
			if (j < k) {
				lo = j + 1;
			} else if (j > k) {
				hi = j - 1;
			} else {
				return this.numbers[j];
			}
		}
        return this.numbers[lo];
	}

	private int partition(int lo, int hi) {
		int p = numbers[lo];
		int j = hi + 1, i = lo;
		while (i < j) {
			while (numbers[++i] < p)
				if (i == hi)
					break;

			while (numbers[--j] > p)
				if (j == lo)
					break;

			if (i < j) {
				exchange(i, j);
			}
		}
		exchange(j, lo);
		return j;
	}

	private void exchange(int i, int j) {
		int temp = numbers[i];
		numbers[i] = numbers[j];
		numbers[j] = temp;
	}

}
