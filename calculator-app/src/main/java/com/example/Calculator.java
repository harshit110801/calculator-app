// Tested by Harshit

package com.example;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public double divide(int a, int b) {
        if (b == 0)
            throw new ArithmeticException("Cannot divide by zero");
        return (double) a / b;
    }

    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("Sum: " + calc.add(5, 3));
        System.out.println("Difference: " + calc.subtract(5, 3));
        System.out.println("Product: " + calc.multiply(5, 3));
        System.out.println("Quotient: " + calc.divide(10, 2));
    }
}
