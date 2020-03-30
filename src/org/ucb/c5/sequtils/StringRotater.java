package org.ucb.c5.sequtils;

public class StringRotater {
    public void initiate() throws Exception {
    }

    // Rotate a string to the desired offset
    // An origin of 0 indicates the offset index will be the beginning of the string
    // An origin of 1 indicates the offset index will be the end of the string
    public String run(String input, int offset) throws Exception {
        offset = offset % input.length();
        if (offset < 0) {
            offset = input.length() + offset;
        }
        return input.substring(offset) + input.substring(0, offset);
    }
    public static void main(String[] args) throws Exception {
        StringRotater rotate = new StringRotater();
        String test = "abcdef";
        String beg = rotate.run(test, 1+3);
        String end = rotate.run(test, 1);
        System.out.println(beg);
        System.out.println(end);
    }
}
