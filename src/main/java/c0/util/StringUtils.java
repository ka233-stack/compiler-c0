package main.java.c0.util;

public class StringUtils {
    public static String intToBin(int size, int number) {
        return String.format("%" + size + "s", Integer.toBinaryString(number)).replace(" ", "0");
    }

    public static String intToHex(int size, int number) {
        return String.format("%" + size + "s", Integer.toHexString(number)).replace(" ", "0");
    }
}
