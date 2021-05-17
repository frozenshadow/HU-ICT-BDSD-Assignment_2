package com.company.MapReduce;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapReduceSentence implements MapReduce {

    /**
     * Map sentence
     *
     * @param input Sentence to map
     * @return      Mapped sentence
     */
    @Override
    public Multimap<String, Integer> map(String input) {
        // Transform sentence to lower case for sorting later
        input = input.toLowerCase();

        // Split up the sentence into individual word, ignoring punctuation.
        String[] words = input.replaceAll("[^a-z]", " ").trim().split("\\W+");

        // Map the words
        Multimap<String, Integer> mappedWords = LinkedListMultimap.create();
        for (String word : words) mappedWords.put(word, 1);

        // Sort the mapped words by key (word)
        mappedWords = MapReduce.sortByKey(mappedWords);

        return mappedWords;
    }

    /**
     * Reduce mapped sentence
     *
     * @param mappedText    Mapped sentence
     * @return              Reduced version of the mapped sentence
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
