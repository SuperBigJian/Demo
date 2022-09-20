package com.cyaan.javakt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

interface Fruit {
}

public class JavaGenericsTest {

    public static void main(String[] args) {
        JavaGenericsTest test = new JavaGenericsTest();
        List<Apple> apples = new ArrayList<>();
        List<Banana> bananas = new ArrayList<>();
        ArrayList<Fruit> fruits = new ArrayList<>();
        Apple apple = new Apple();
        Banana banana = new Banana();
//        test.<Apple>addList(apple,fruits);
        test.addList2(new Runnable() {
            @Override
            public void run() {

            }
        });
    }


    <E extends Fruit & Runnable> void addList(E e, List<? super E> list) {
        list.add(e);
    }

    <E extends Runnable & Serializable> void addList1(E e) {

    }

    <E extends Runnable> void addList2(E e) {

    }

    <E> void addList2(E e, List<? super E> list) {

    }

}

class Apple implements Fruit {

}

class Banana implements Fruit {
}