package io.mercer.cuvinte;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CountWords {

    private static final String[] FILES = {
            "eminescu-poezii.txt",
            "creanga-amintiri-din-copilarie.txt",
            "iorga-adevarul-asupra-trecutului-si-prezentului-basarabiei.txt",
    };

    public static void main(String[] args) throws IOException {
        List<String> words = new ArrayList<>();
        for (String file : FILES) {
            words.addAll(scanWords(toScanner(file)));
        }

        Map<String, Integer> frequencyMap = words.stream()
                .collect(toMap(
                        s -> s,
                        s -> 1,
                        Integer::sum));

        List<String> top = words.stream()
                .sorted(comparing(frequencyMap::get).reversed())
                .distinct()
                .limit(1000)
                .collect(toList());

        Path file = Paths.get("build/output.txt");
        Files.write(file, top, Charset.forName("UTF-8"));
    }

    private static Scanner toScanner(String s) {
        return new Scanner(CountWords.class.getClassLoader().getResourceAsStream(s), "UTF-8").useDelimiter("[^a-zA-Z]+");
    }

    private static List<String> scanWords(Scanner file) {
        List<String> words = new ArrayList<>();
        while (file.hasNext()) {
            String next = file.next();
            words.add(next.toLowerCase());
        }
        return words;
    }
}
