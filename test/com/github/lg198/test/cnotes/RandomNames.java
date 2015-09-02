package com.github.lg198.test.cnotes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RandomNames {

    public static List<String> nameMappings = new ArrayList<>();
    public static List<String> names;

    public static void main(String[] args) throws Exception {
        names = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Layne\\Documents\\randomNames.txt"));
        for (String line = ""; (line = br.readLine()) != null;) {
            if (line.isEmpty()) {
                continue;
            }
            names.add(line.trim());
        }
        br.close();

        for (int i = 1; !names.isEmpty() && i < 30; i++) {
            checkNames(i);
        }
        names.stream().forEach(s -> System.out.println(s));
    }

    public static void checkNames(int len) {
        List<String> violations = new ArrayList<>();
        System.out.println("Checking names... length " + len);
        nameMappings.clear();
        ListIterator<String> li = names.listIterator();
        while (li.hasNext()) {
            String name = li.next();
            String key = Arrays.stream(name.split("\\s+"))
                    .map((Function<String, String>) s -> {
                        if (len > s.length()) {
                            return s.toUpperCase();
                        }
                        return s.substring(0, len).toUpperCase();
                    })
                    .collect(Collectors.joining());
            if (nameMappings.contains(key)) {
                System.out.println("Processing " + name + "... crosses with " + key);
                violations.add(name);
            } else {
                nameMappings.add(key);
            }
        }
        names = violations;
    }
}
