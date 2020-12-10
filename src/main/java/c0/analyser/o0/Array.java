package main.java.c0.analyser.o0;

import java.util.ArrayList;

public class Array<E> {
    // 数组的长度
    private int count; // u32
    // 数组所有元素的无间隔排列
    private final ArrayList<E> items;// items:T[]

    public Array() {
        this.count = 0;
        this.items = new ArrayList<>();
    }

    public Array(int count, ArrayList<E> items) {
        this.count = count;
        this.items = items;
    }

    public int add(E e) {
        this.items.add(e);
        return this.count++;
    }

    public ArrayList<E> getItems() {
        return this.items;
    }

    public int getCount() {
        return count;
    }
}
