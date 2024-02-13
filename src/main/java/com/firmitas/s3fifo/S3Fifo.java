package com.firmitas.s3fifo;

import java.util.*;

class Item<K, V> {
    private final K key;
    private final V value;
    private final int freq;

    public Item(K key, V value) {
        this.key = key;
        this.value = value;
        this.freq = 0;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public int getFreq() {
        return freq;
    }
}

public class S3Fifo<K, V> {
    private final Map<K, Item<K, V>> smallMap;
    private final Map<K, Item<K, V>> mainMap;
    private final Map<K, Item<K, V>> ghostMap;
    private final LinkedList<Item<K, V>> smallQueue;
    private final LinkedList<Item<K, V>> mainQueue;
    private final LinkedList<Item<K, V>> ghostQueue;
    private static int MAIN_QUEUE_SIZE;

    public S3Fifo() {
        this.smallMap = new HashMap<>();
        this.mainMap = new HashMap<>();
        this.ghostMap = new HashMap<>();
        this.smallQueue = new LinkedList<>();
        this.mainQueue = new LinkedList<>();
        this.ghostQueue = new LinkedList<>();
        MAIN_QUEUE_SIZE = 2;
    }

    public V get(K key) {
        return (V) this.smallMap.get(key).getValue();
    }

    /**
     * while mainQueue is full do
     *      evict()
     * if x in GhostQueue then
     *      insert x to the head of MainQueue
     * else
     *      insert x to the head of S
     */
    public void put(K key, V value) {
        System.out.println("Key: " + key);
        System.out.println("Value: " + value);
        Item<K, V> i = new Item<>(key, value);
        if (mainQueue.size() == MAIN_QUEUE_SIZE) {
            evict();
        }
        if (ghostMap.containsKey(key)) {
            insertIntoMainQueue(key, i);
        } else {
            insertIntoSmallQueue(key, i);
        }
        System.out.println(smallQueue.size());
    }

    private void evict() {
        // to be implemented
    }

    private void insertIntoSmallQueue(K key, Item<K, V> item) {
        this.smallQueue.offerFirst(item);
        this.smallMap.put(key, item);
    }

    private void insertIntoMainQueue(K key, Item<K, V> item) {
        this.mainQueue.offerFirst(item);
        this.mainMap.put(key, item);
    }

    private void insertIntoGhostQueue(K key, Item<K, V> item) {
        this.ghostQueue.offerFirst(item);
        this.ghostMap.put(key, item);
    }
}
