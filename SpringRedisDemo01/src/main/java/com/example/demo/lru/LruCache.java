package com.example.demo.lru;

import java.util.HashMap;
import java.util.Map;

public class LruCache {
    class Node<k,v>{
        k key;
        v value;
        Node<k,v> prev;
        Node<k,v> next;

        public Node(){
            this.prev = this.next = null;
        }
        public Node(k key, v value) {
            this.key = key;
            this.value = value;
            this.prev = this.next = null;
        }
    }
    class DoubleLinkedList<k,v>{
        Node<k,v> head;
        Node<k,v> tail;
        public DoubleLinkedList(){
            this.head = new Node<>();
            this.tail = new Node<>();
            head.next = tail;
            tail.prev = head;
        }
        public void addHead(Node<k,v> node){
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        public void remove(Node<k,v> node){
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }
        public Node<k,v> getLast(){
            return tail.prev;
        }
    }

    private int capacity;
    Map<Integer,Node<Integer,Integer>> map;
    DoubleLinkedList<Integer,Integer> doubleLinkedList;

    public LruCache(int capacity){
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.doubleLinkedList = new DoubleLinkedList<>();
    }
    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }
        Node<Integer,Integer> node = map.get(key);
        doubleLinkedList.remove(node);
        doubleLinkedList.addHead(node);

        return node.value;
    }
    public void put(int key, int value){
        if(map.containsKey(key)){
            Node<Integer,Integer> node = map.get(key);
            node.value = value;
            map.remove(key);
            map.put(key,node);

            doubleLinkedList.remove(node);
            doubleLinkedList.addHead(node);
        }else{
            if (map.size() == capacity){
                final Node<Integer, Integer> last = doubleLinkedList.getLast();
                map.remove(last.key);
                doubleLinkedList.remove(last);
            }
            Node<Integer, Integer> newNode = new Node<>(key,value);
            map.put(key,newNode);
            doubleLinkedList.addHead(newNode);
        }
    }
    public static void main(String[] args) {
        LruCache lruCache = new LruCache(3);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        System.out.println(lruCache.map.keySet());
        lruCache.put(4,1);
        System.out.println(lruCache.map.keySet());

        lruCache.put(3,1);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,1);
        System.out.println(lruCache.map.keySet());
        lruCache.put(3,1);
        System.out.println(lruCache.map.keySet());
        lruCache.put(5,1);
        System.out.println(lruCache.map.keySet());
    }
}
