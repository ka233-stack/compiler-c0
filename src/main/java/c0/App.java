package main.java.c0;

import main.java.c0.analyser.Analyser;
import main.java.c0.analyser.o0.O0;
import main.java.c0.tokenizer.StringIter;
import main.java.c0.tokenizer.Tokenizer;

import java.io.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = null;
        PrintStream output = null;
        try {
            scanner = new Scanner(new File(args[0]));
            output = new PrintStream(new FileOutputStream(args[1]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        StringIter stringIter = new StringIter(scanner);
        Tokenizer tokenizer = new Tokenizer(stringIter);
        Analyser analyser = new Analyser(tokenizer);
        O0 binCodeFile = null;
        try {
            binCodeFile = analyser.analyse();
        } catch (Exception e) {
            System.err.println(e);
        }
        String str = binCodeFile.toHexCode();
        int size = str.length();
        for (int i = 0; i < size; i += 2) {
            int x = Integer.parseInt("" + str.charAt(i) + str.charAt(i + 1), 16);
            System.out.println(x);
            output.write(x);
        }
        if (output != null)
            output.close();
    }
}

