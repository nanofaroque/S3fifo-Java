package com.firmitas.s3fifo;

import java.util.*;

class Item<K, V> {
    private final K key;
    private final V value;
    private int freq;

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

    public void setFreq(int freq) {
        this.freq = freq;
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

    public S3Fifo(int size) {
        this.smallMap = new HashMap<>();
        this.mainMap = new HashMap<>();
        this.ghostMap = new HashMap<>();
        this.smallQueue = new LinkedList<>();
        this.mainQueue = new LinkedList<>();
        this.ghostQueue = new LinkedList<>();
        MAIN_QUEUE_SIZE = size;
    }

    /**
     *
     * 2: if 𝑥 in 𝑆 or 𝑥 in 𝑀 then ⊲ Cache Hit
     * 3:   𝑥.freq ← min(𝑥.freq + 1, 3) ⊲ Frequency is capped to 3
     * 4: else ⊲ Cache Miss
     * 5:   insert(𝑥)
     * 6:   𝑥.freq ← 0
     * */
    public V get(K key) {
        if(smallMap.containsKey(key)){
            Item<K,V> item = smallMap.get(key);
            int freq = item.getFreq();
            int uFreq = Math.min(freq+1,3);
            item.setFreq(uFreq);
            return item.getValue();
        }else if(mainMap.containsKey(key)){
            Item<K,V> item = mainMap.get(key);
            int freq = item.getFreq();
            int uFreq = Math.min(freq+1,3);
            item.setFreq(uFreq);
            return item.getValue();
        }else if(ghostMap.containsKey(key)){
            insertIntoMainQueue(key,ghostMap.get(key));
            return ghostMap.get(key).getValue();
        }else return null;
    }

    /**
     * while mainQueue is full do
     * evict()
     * if x in GhostQueue then
     * insert x to the head of MainQueue
     * else
     * insert x to the head of S
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

    /**
     * if SmallQueue.size >=.1*MAIN_QUEUE_SIZE
     *      evictFromSmallQueue();
     * else
     *      evictFromMainQueue()
     */
    private void evict() {
        if (smallQueue.size() >= 0.1 * MAIN_QUEUE_SIZE) {
            evictFromSmallQueue();
        } else {
            evictFromMainQueue();
        }
    }

    /**
     * 32: evicted ← false
     * 33: while not evicted and M.size > 0 do
     * 34:      𝑡 ← tail of M
     * 35:      if 𝑡.freq > 0 then
     * 36:          Insert 𝑡 to head of M
     * 37:          𝑡.freq ← 𝑡.freq-1
     * 38:      else
     * 39:          remove 𝑡 from M
     * 40:          evicted ← true
     * */
    private void evictFromMainQueue() {
        boolean evicted = false;
        while (!evicted && !mainQueue.isEmpty()) {
            Item<K, V> tail = mainQueue.getLast();
            if (tail.getFreq() > 0) {
                tail.setFreq(tail.getFreq() - 1);
                insertIntoMainQueue(tail.getKey(), tail);
            } else {
                mainQueue.removeLast();
                evicted = true;
            }
        }
    }

    /**
     * 20: evicted ← false
     * 21: while not evicted and S.size > 0 do
     * 22:      𝑡 ← tail of S
     * 23:      if 𝑡.freq > 1 then
     * 24:          insert 𝑡 to M
     * 25:          if M is full then
     * 26:              evictM()
     * 27:      else
     * 28:          insert 𝑡 to G
     * 29:          evicted ← true
     * 30:      remove 𝑡 from S
     */
    private void evictFromSmallQueue() {
        boolean evicted = false;
        while (!evicted && !smallQueue.isEmpty()) {
            Item<K, V> tail = smallQueue.getLast();
            if (tail.getFreq() > 1) {
                insertIntoMainQueue(tail.getKey(), tail);
                if (mainQueue.size() >= .9 * MAIN_QUEUE_SIZE) {
                    evictFromMainQueue();
                }
            } else {
                insertIntoGhostQueue(tail.getKey(), tail);
                evicted = true;
            }
            smallMap.remove(tail.getKey());
            smallQueue.removeLast();
        }
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
