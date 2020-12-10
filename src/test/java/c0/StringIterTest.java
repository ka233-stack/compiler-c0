package test.java.c0;

import main.java.c0.tokenizer.StringIter;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StringIterTest {
    Scanner scanner;

    {
        try {
            scanner = new Scanner(new File("test1.txt"));
        } catch (FileNotFoundException e) {
        }
    }

    StringIter stringIter = new StringIter(scanner);

    @Test
    public void nextPosTest() {
        stringIter.readAll();
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
        System.out.println(stringIter.nextPos());
        System.out.println(stringIter.nextChar());
    }
}
