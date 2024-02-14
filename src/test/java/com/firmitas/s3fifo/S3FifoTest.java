package com.firmitas.s3fifo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class S3FifoTest {
    private S3Fifo s3Fifo;

    @BeforeEach
    public void init() {
        s3Fifo = new S3Fifo(10);
    }

    @Test
    public void happy() {
        s3Fifo.put("Omar", "Faroque");
        assertEquals(s3Fifo.get("Omar"), "Faroque");
    }

    @Test
    public void happy_large() {
        S3Fifo s3Fifo = new S3Fifo(100000);
        for (int i = 1; i <= 100000; i++) {
            s3Fifo.put(i,i+1);
        }
        assertEquals(100001,s3Fifo.get(100000));
    }
}
