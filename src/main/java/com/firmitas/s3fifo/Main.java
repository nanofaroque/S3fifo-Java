package com.firmitas.s3fifo;

public class Main {
    public static void main(String[] args) {
        S3Fifo<String, String> s3fifo = new S3Fifo();
        s3fifo.put("Omar", "Faroque");
        String value = s3fifo.get("Omar");
        S3Fifo<Integer, String> is3Fifo = new S3Fifo();
        is3Fifo.put(1, "Faroque");
        System.out.println(value);
    }
}