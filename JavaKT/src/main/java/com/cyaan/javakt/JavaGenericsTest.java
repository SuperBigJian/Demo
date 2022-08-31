package com.cyaan.demo.breakpad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        List<Apple> apples = new ArrayList<>();
        List<Banana> bananas = new ArrayList<>();
        ArrayList<Fruit> fruits = new ArrayList<>();
        Apple apple = new Apple();
        Banana banana = new Banana();
//        test.<Apple>addList(apple,fruits);
        test.addList2(new Runnable(){
            @Override
            public void run() {

            }
        });
    }


    <E extends Fruit & Runnable> void addList(E e, List<? super E> list){
        list.add(e);
    }
    <E extends Runnable & Serializable> void addList1(E e){

    }
    <E extends Runnable > void addList2(E e){

    }
    <E> void addList2(E e, List<? super E> list){

    }

}

interface Fruit{}

class Apple implements Fruit {

}

class Banana implements Fruit{}