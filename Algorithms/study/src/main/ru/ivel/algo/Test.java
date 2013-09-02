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
    }
}
