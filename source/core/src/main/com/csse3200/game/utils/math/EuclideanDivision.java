package com.csse3200.game.utils.math;

/**
 * Provides functionality for integer div/mod satisfying the Euclidean algorithm.
 * Note - Java's built in % and / do not satisfy this (although still satisfying a type of
 * division-remainder property).
 */
public class EuclideanDivision {
    private EuclideanDivision() {
        throw new IllegalStateException("Instantiating static util class");
    }

    /**
     * @param n the integer to take the modulus of
     * @param d the integer to modulus by
     * @return modulus r between [0,...,d-1] satisfying x = d * div(n, d) + r
     */
    public static int mod(int n, int d) {
        int res = n % d;
        return res < 0 ? res + d : res;
    }

    /**
     * @param n the integer to divide
     * @param d the integer to divide by
     * @return quotient q satisfying n = d * q + mod(n, d)
     */
    public static int div(int n, int d) {
        int res = n / d;
        return n % d < 0 ? res - 1 : res;
    }
}
