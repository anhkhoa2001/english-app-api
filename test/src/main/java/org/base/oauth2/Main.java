package org.base.oauth2;

import java.util.ArrayList;
import java.util.List;

public class Main {

    final static List<String> list = new ArrayList<>();
    final static int i = 1;

    public static void main(String[] args) {
        list.add("abc");
        String ta = "A";
        ta = ta.concat("B");
        String tb = "C ";
        ta = ta.concat(tb);
        ta.replace('C', 'D');
        ta = ta.concat(tb);
        System.out.println(ta);
    }
}


