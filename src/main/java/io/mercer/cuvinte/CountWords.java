package io.mercer.cuvinte;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class CountWords {

    private static final String[] FILES = {
            "alecsandri-chirita.txt",
            "balzac-eugenie-grandet.txt",
            "bart-jurnal-de-bord.txt",
            "basme-culte-si-populare.txt",
            "biblia.txt",
            "caragiale-momente-si-schite.txt",
            "caragiale-teatru.txt",
            "creanga-amintiri-din-copilarie.txt",
            "creanga-povesti.txt",
            "eminescu-geniu-pustiu.txt",
            "eminescu-poesii.txt",
            "eminescu-scrisori.txt",
            "iorga-adevarul-asupra-trecutului-si-prezentului-basarabiei.txt",
            "ispirescu-basme.txt",
            "istrati-ciulinii-baraganului.txt",
            "odobescu-doamna-chiajna.txt",
            "rebreanu-adam-si-eva.txt",
            "rebreanu-calvarul.txt",
            "rebreanu-ciuleandra.txt",
            "rebreanu-ion.txt",
            "rebreanu-padurea-spanzuratilor.txt",
            "slavici-moara-cu-noroc.txt",
            "zamfirescu-viata-la-tara.txt",
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

        List<String> lines = words.stream()
                .sorted(comparing(frequencyMap::get).reversed())
                .distinct()
                .limit(1000)
                .map(s -> Word.word(s, frequencyMap.get(s)))
                .map(Word::toCsvLine)
                .collect(toList());

        Files.write(Paths.get("results/toti-autorii-impreuna.csv"), lines, Charset.forName("UTF-8"));
    }

    private static Scanner toScanner(String s) {
        return new Scanner(CountWords.class.getClassLoader().getResourceAsStream(s), "UTF-8")
                .useLocale(new Locale("ro", "RO"))
                .useDelimiter(Pattern.compile("[^\\p{L}]+", Pattern.UNICODE_CHARACTER_CLASS & Pattern.UNICODE_CASE));
    }

    private static List<String> scanWords(Scanner file) {
        List<String> words = new ArrayList<>();
        while (file.hasNext()) {
            String next = file.next();
            words.add(next.toLowerCase());
        }
        return words;
    }

    private static class Word {
        private String word;
        private Integer count;

        private Word(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        static Word word(String word, Integer count) {
            return new Word(word, count);
        }

        String toCsvLine() {
            return word + "," + count;
        }
    }
}
