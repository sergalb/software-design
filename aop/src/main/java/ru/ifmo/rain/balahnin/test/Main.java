package ru.ifmo.rain.balahnin.test;

import java.util.Scanner;

public class Main {
    public static String packageToProfile;
    public static void main(String[] args) {
        System.out.println("enter package to measure");
        Scanner scanner = new Scanner(System.in);
        packageToProfile = scanner.next();
        SomeClass someClass = new SomeClass();
        someClass.printFortyTwo();
        someClass.printArg("arg");
        OtherClass otherClass = new OtherClass();
        otherClass.print17();
        otherClass.heavyFunc();
        otherClass.printTimeMeasure();
    }
}
