package io.mercer.cuvinte;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CountWords {
    public static void main(String[] args) throws FileNotFoundException {

        // from
        // https://stackoverflow.com/questions/13979317/how-to-count-the-number-of-occurrences-of-words-in-a-text

        InputStream in = CountWords.class.getClassLoader().getResourceAsStream("pg35323.txt");
        Scanner file = new Scanner(in).useDelimiter("[^a-zA-Z]+");

        List<String> words = new ArrayList<>();
        while (file.hasNext()) {
            words.add(file.next().toLowerCase());
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

        for (String key:top) {
            System.out.println("key: " + key + "\t\tvalue: " + frequencyMap.get(key));
        }
    }
}
