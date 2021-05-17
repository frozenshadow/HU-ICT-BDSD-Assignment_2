package com.company.MapReduce;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface MapReduce {

    /**
     * Mapper
     *
     * @param input Text to map
     * @return      Mapped text
     */
    Multimap<String, Integer> map(String input);

    /**
     * Reduce mapped text
     *
     * @param mappedText    Mapped text
     * @return              Reduced version of the mapped text input
     */
    HashMap<String, Integer> reduce(Multimap<String, Integer> mappedText);

    /**
     * Sort result of the mapper by key
     *
     * @param unsortedMap   The map to be sorted by key
     * @return              Sorted by key version of the input map
     */
    static Multimap<String, Integer> sortByKey(Multimap<String, Integer> unsortedMap) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entries());

        // Sort the list using a lambda expression
        list.sort(Map.Entry.comparingByKey());

        // Put the result from the sorted list back into a multimap
        Multimap<String, Integer> sortedMap = LinkedListMultimap.create();
        for (Map.Entry<String, Integer> e : list) sortedMap.put(e.getKey(), e.getValue());

        return sortedMap;
    }

    /*public void printer(HashMap<String, Integer> input, String word) {
        Table st = new Table();
        st.setShowVerticalLines(true);

        // Create Header
        List<String> header = new ArrayList<>();
        header.add("");
        header.add("");

        for (Map.Entry<String, Integer> e : input.entrySet()) header.add(e.getKey());
        header.add("Totaal");

        st.setHeaders(header.toArray(new String[0]));

        // Create Rows
        List<String> row = new ArrayList<>();
        for (Map.Entry<String, Integer> l : input.entrySet()) {
            int totalRowOccurrence = 0;

            row.add("");
            row.add(l.getKey());

            List<Integer> a = findWord(word, l.getKey());

            for (Map.Entry<String, Integer> e : input.entrySet()) {
                int rekensommetje = 0;

                for (Integer i : a) {
                    if (i+1 < word.length()) {
                        char successiveCharacter = word.charAt(i + 1); // B
                        if (Character.toString(successiveCharacter).equals(e.getKey())) rekensommetje++;
                    }
                }

                totalRowOccurrence += rekensommetje;
                row.add(String.valueOf(rekensommetje));
            }

            row.add(String.valueOf(totalRowOccurrence));
            st.addRow(row.toArray(new String[0]));
            row.clear();
        }

        // Create Footer


        //st.addRow("", "e", "1", "0", "0", "0", "1");
        st.addRow("Totaal", "", "-", "-", "-", "-", "");

        st.print();
    }

    public List<Integer> findWord(String textString, String word) {
        List<Integer> indexes = new ArrayList<Integer>();
        String lowerCaseTextString = textString.toLowerCase();
        String lowerCaseWord = word.toLowerCase();

        int index = 0;
        while (index != -1) {
            index = lowerCaseTextString.indexOf(lowerCaseWord, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }*/
}
