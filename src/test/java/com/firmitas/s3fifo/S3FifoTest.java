package com.firmitas.s3fifo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class S3FifoTest {
    private S3Fifo s3Fifo;

    @BeforeEach
    public void init() {
        s3Fifo = new S3Fifo();
    }

    @Test
    public void happy() {
        s3Fifo.put("Omar", "Faroque");
        assertEquals(s3Fifo.get("Omar"), "Faroque");
        s3Fifo.put("Omar", 1);
        assertEquals(s3Fifo.get("Omar"), 1);
        s3Fifo.put(100, 1);
        assertEquals(s3Fifo.get(100), 1);
    }
}
