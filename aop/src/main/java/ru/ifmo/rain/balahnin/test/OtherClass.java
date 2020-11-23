package ru.ifmo.rain.balahnin.test;

import java.util.Random;

public class OtherClass {
    public void print17(){
        System.out.println("17");
    }

    public void heavyFunc() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        int n = 1000000;
        for (int i = 0; i < n; i++) {
            stringBuilder.append(random.nextInt());
        }
        System.out.println(stringBuilder.charAt(random.nextInt(n)));
    }

    public void printTimeMeasure() {
        long startMs = System.currentTimeMillis();
        print17();
        heavyFunc();
        long finishMs = System.currentTimeMillis();
        System.out.println(finishMs - startMs);
    }
}
