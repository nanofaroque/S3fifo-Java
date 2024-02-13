package com.firmitas.s3fifo;

public class Main {
    public static void main(String[] args) {
        S3Fifo<String, String> s3fifo = new S3Fifo(10);
        s3fifo.put("Omar", "Faroque");
        String value = s3fifo.get("Omar");
        System.out.println(value);
    }
}