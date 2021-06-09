package com.example.demo.lru;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruDemo extends LinkedHashMap {
    private int capacity;

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return super.size() > capacity;
    }

    public LruDemo(int initialCapacity) {
        super(initialCapacity, 0.75F, true);
        this.capacity = initialCapacity;
    }

    public static void main(String[] args) {
        LruDemo lruDemo = new LruDemo(3);
        lruDemo.put(1,"a");
        lruDemo.put(2,"b");
        lruDemo.put(3,"c");
        System.out.println(lruDemo.keySet());
        lruDemo.put(4,"d");
        lruDemo.put(5,"d");
        lruDemo.put(4,"d");
        System.out.println(lruDemo.keySet());

    }
}
