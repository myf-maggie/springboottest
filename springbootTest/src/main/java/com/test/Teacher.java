package com.test;

/**
 * Created by zxc on 2020/3/26.
 */
public class Teacher extends Person{
        public void printValue() {/*...*/ }
        public void printValue(int i) {
            System.out.print("Teacher");
        }
         public static void main(String args[]){
        Person t = new Teacher();
        t.printValue(10);
         }
        }

class Person {
    public void printValue(int i, int j) {/*…*/ }
    public void printValue(int i){
        System.out.print("Person");
    }
}