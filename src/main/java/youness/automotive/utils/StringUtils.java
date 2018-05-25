package youness.automotive.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringUtils {
    public static String capitalizeFirstLetters(String phrase) {
        return Arrays.stream(phrase.split(" "))
                .map(word -> (word.substring(0, 1).toUpperCase() + word.substring(1)))
                .collect(Collectors.joining(" "));
    }

    public static String pluralize(String word) {
        if (word.endsWith("s") || word.endsWith("x") || word.endsWith("o")
                || word.endsWith("ch")) {
            return word + "es";
        }

        if (word.endsWith("y")) {
            // Odd case to avoid StringIndexOutOfBounds later
            if (word.length() == 1) {
                return word;
            }

            // Check next-to-last letter
            char next2last = word.charAt(word.length() - 2);
            if ((next2last != 'a') && (next2last != 'e')
                    && (next2last != 'i') && (next2last != 'o')
                    && (next2last != 'u') && (next2last != 'y')) {
                return word.substring(0, word.length() - 1) + "ies";
            }
        }

        return word + "s";
    }

}
