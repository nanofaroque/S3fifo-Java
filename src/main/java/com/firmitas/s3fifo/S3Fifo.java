package com.firmitas.s3fifo;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

class Item<K,V>{
    private final K key;
    private final V value;
    private final int freq;

    public Item(K key, V value) {
        this.key = key;
        this.value = value;
        this.freq=0;
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
public class S3Fifo<K,V> {
    private final Map<K, Item<K,V>> smallMap;
    private final Map<K, Item<K,V>> mainMap;
    private final Map<K, Item<K,V>> ghostMap;
    private final Queue<Item<K,V>> smallQueue;
    private final Queue<Item<K,V>> mainQueue;
    private final Queue<Item<K,V>> ghostQueue;

    public S3Fifo() {
        this.smallMap = new HashMap<>();
        this.mainMap = new HashMap<>();
        this.ghostMap = new HashMap<>();
        this.smallQueue = new ArrayDeque<>();
        this.mainQueue = new ArrayDeque<>();
        this.ghostQueue = new ArrayDeque<>();
    }

    public V get(K key) {
        return (V) this.smallMap.get(key).getValue();
    }

    public void put(K key, V value) {
        System.out.println("Key: " + key);
        System.out.println("Value: " + value);
        Item<K,V> i = new Item<>(key,value);
        insertIntoSmallQueue(key,i);
        return;
    }

    private void insertIntoSmallQueue(K key, Item<K,V> item){
        this.smallQueue.offer(item);
        this.smallMap.put(key,item);
    }

    private void insertIntoMainQueue(K key, Item<K,V> item){
        this.mainQueue.offer(item);
        this.mainMap.put(key,item);
    }

    private void insertIntoGhostQueue(K key, Item<K,V> item){
        this.ghostQueue.offer(item);
        this.ghostMap.put(key,item);
    }
}
