package ru.ifmo.rain.balahnin.test;

public class SomeClass {
    public void printFour() {
        System.out.print("4");
    }

    private void printTwo() {
        System.out.println("2");
    }

    void printFortyTwo() {
        printFour();
        printTwo();
    }

    void printArg(String arg) {
        System.out.println(arg);
    }

}
