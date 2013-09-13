package ru.ivel.algo;

/**
 * Test
 *
 * @author Игорь Елькин (ielkin@nvision-group.com)
 */
public class Test {

    public static void main(String[] args) {
        int N = 2;
        int sum = 0;
        for (int i = 0; i*i < N; i++)
            for (int j = 0; j*j < N*N*N; j++)
                System.out.println(i + " " + j);
                sum++;

        System.out.println(sum);
        Integer [] a = {1, 2, 3};
        Double [] d = {1.1, 2.1, 3.1};
        System.out.println(max(a));
        System.out.println(max(d));
    }

    public static <T extends Comparable<? super T>> T max(T a[]) {
        T max = a[0];
        for (T i : a) {
            if (i.compareTo(max) > 0) {
                max = i;
            }
        }
        return max;
    }
}
