package org.ucb.c5.utils;

public class ChangeableString {
    String str;

    public ChangeableString(String str) {
        this.str = str;
    }

    public void changeTo(String newStr) {
        str = newStr;
    }

    public String toString() {
        return str;
    }
}
