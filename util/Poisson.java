package util;

import java.util.concurrent.ThreadLocalRandom;

public class Poisson {

    public long poisson(double lambda, long limit) {
        double l = Math.pow(Math.E, -lambda);
        long k = 0;
        double p = 1;

        do {
            k = k + 1;
            double u = ThreadLocalRandom.current().nextDouble();
            p = p * u;
        } while (p > l && k < limit);
        return k - 1;
    }

    // Test...
    public static void main(String[] args) {
        Poisson p = new Poisson();

        int[] counts = new int[10];
        for (int i = 0; i < 100000; i++) {
            counts[(int)p.poisson(2, counts.length)]++;
        }

        for (int i = 0; i < counts.length; i++) {
            System.out.println(i + ": " + counts[i]);
        }
    }
}
