package com.firmitas.s3fifo;

public class Main {
    public static void main(String[] args) {
        S3Fifo<Integer, Integer> s3fifo = new S3Fifo(10);
        for (int i = 0; i < 40; i++) {
            s3fifo.put(i,i+1);
        }
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(20));
        System.out.println(s3fifo.get(0));
        System.out.println(s3fifo.get(0));
    }
}