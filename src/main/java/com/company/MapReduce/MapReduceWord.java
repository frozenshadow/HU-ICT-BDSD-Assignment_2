package com.company.MapReduce;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapReduceWord implements MapReduce {

    /**
     * Map text
     *
     * @param input Text to map
     * @return      Mapped text
     */
    @Override
    public Multimap<String, Integer> map(String input) {
        // Transform word to lower case for sorting later
        input = input.toLowerCase();

        // Split up the word up into individual characters
        String[] characters = input.split("");

        // Map the characters
        Multimap<String, Integer> mappedCharacters = LinkedListMultimap.create();
        for (String character : characters) mappedCharacters.put(character, 1);

        // Sort the mapped characters by key (character)
        mappedCharacters = MapReduce.sortByKey(mappedCharacters);

        return mappedCharacters;
    }

    /**
     * Reduce mapped word
     *
     * @param mappedText    Mapped word
     * @return              Reduced version of the mapped word
     */
    @Override
    public HashMap<String, Integer> reduce(Multimap<String, Integer> mappedText) {
        String prevWord = null;
        int sameWordCount = 0;
        HashMap<String, Integer> result = new LinkedHashMap<>();

        // Iterate over every word in the sentence
        for (Map.Entry<String, Integer> e : mappedText.entries()) {
            String word = e.getKey();
            int count = e.getValue();

            // Check if the current iteration word is the same as the previous iteration.
            // Increment the word count if this is the case.
            // Otherwise it is the first or last time the word occurs.
            if (prevWord != null && prevWord.equals(word)) {
                sameWordCount += count;
            } else {
                if (prevWord != null) result.put(prevWord, sameWordCount);
                sameWordCount = count;
                prevWord = word;
            }
        }

        // Let's not forget the last word... poor fella
        result.put(prevWord, sameWordCount);

        return result;
    }
}
