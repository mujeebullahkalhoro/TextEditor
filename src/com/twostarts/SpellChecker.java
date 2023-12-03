
package com.twostarts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

class Node {
    Node[] children;
    boolean eow;

    public Node() {
        children = new Node[26]; // a to z;
        for (int i = 0; i < children.length; i++) {
            children[i] = null;
        }
        eow = false;
    }
}

public class SpellChecker {

    private static Node root = new Node();

    public static void insert(String word) {
        Node current = root;

        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);

            // Check if the character is a lowercase letter
            if ('a' <= currentChar && currentChar <= 'z') {
                int index = currentChar - 'a';

                if (current.children[index] == null) {
                    // add new node;
                    current.children[index] = new Node();
                }

                if (i == word.length() - 1) {
                    current.children[index].eow = true;
                }

                current = current.children[index];
            }
        }
    }

    public static boolean search(String key) {
        Node current = root;
        for (int i = 0; i < key.length(); i++) {
            char currentChar = key.charAt(i);

            // Check if the character is a lowercase letter
            if ('a' <= currentChar && currentChar <= 'z') {
                int index = currentChar - 'a';
                Node node = current.children[index];

                if (node == null) {
                    return false;
                }

                if (i == key.length() - 1 && !node.eow) {
                    return false;
                }

                current = current.children[index];
            }
        }
        return true;
    }

    public static void loadWords() throws FileNotFoundException {
        File words = new File("src/com/twostarts/20k.txt");

        try (Scanner sc = new Scanner(words)) {
            while (sc.hasNextLine()) {
                String word = sc.nextLine();
                insert(word);
            }
        }
    }

    public static List<String> getSuggestions(String word) {
        if (word.isEmpty()) {
            return Collections.emptyList(); // Return an empty list if the input string is empty
        }
        PriorityQueue<String> suggestions = new PriorityQueue<>();
        collectSuggestions(root, word, "", suggestions);
        return new ArrayList<>(suggestions);
    }

    private static void collectSuggestions(Node node, String remainingWord, String currentPrefix,
            PriorityQueue<String> suggestions) {
        if (node == null) {
            return;
        }

        if (remainingWord.isEmpty() && node.eow) {
            suggestions.offer(currentPrefix);
            if (suggestions.size() > 4) {
                suggestions.poll(); // Remove the suggestion with the highest edit distance
            }
        }

        for (int i = 0; i < 26; i++) {
            char ch = (char) ('a' + i);
            if (!remainingWord.isEmpty() && ch == remainingWord.charAt(0)) {
                collectSuggestions(node.children[i], remainingWord.substring(1),
                        currentPrefix + ch, suggestions);
            } else {
                collectSuggestions(node.children[i], remainingWord,
                        currentPrefix + ch, suggestions);
            }
        }
    }
}
