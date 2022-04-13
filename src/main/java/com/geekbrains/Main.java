package com.geekbrains;

public class Main {
    static final int size = 10000000;
    static final int h = size / 2;
    static float[] arr = new float[size];

    public static void main(String[] args) {

        for(int i = 0; i < size; i++){
            arr[i] = 1;
        }

        long singleTime = singleThread(arr);
        long multiTime = multiThread(arr);

        increase(singleTime, multiTime);
    }

    private static long singleThread(float[] arr){
        long start = System.currentTimeMillis();

        for(int i = 0; i < size; i++){
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
        }

        long singleTime = System.currentTimeMillis() - start;

        System.out.printf("single thread time: %d%n", singleTime);
        return singleTime;
    }

    private static long multiThread(float[] arr){
        float[] a1 = new float[h];
        float[] a2 = new float[h];

        long start = System.currentTimeMillis();

        System.arraycopy(arr, 0, a1,0, h);
        System.arraycopy(arr, h, a2, 0, h);

        MyThread t1 = new MyThread("a1", a1);
        MyThread t2 = new MyThread("a2", a2);

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        a1 = t1.getArr();
        a2 = t2.getArr();

        System.arraycopy(a1, 0, arr, 0, h);
        System.arraycopy(a2, 0, arr, a1.length, a2.length);

        long multiTime = System.currentTimeMillis() - start;

        System.out.printf("multi thread time: %d%n", multiTime);

        return multiTime;
    }

    private static void increase(long singleTime, long multiTime){
        double diff = ((double) singleTime / (double) multiTime) - 1;
        int increase = (int) (diff * 100);
        System.out.printf("increase: %d%%%n", increase);
    }
}
