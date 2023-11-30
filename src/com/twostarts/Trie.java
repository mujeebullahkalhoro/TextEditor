package com.twostarts;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

public class Trie {

    private static Node root = new Node();

    private static void insert(String word) {
        Node current = root;

        for (int i = 0; i < word.length(); i++) {
            int indx = word.charAt(i) - 'a';

            if (current.children[indx] == null) {
                // add new node;
                current.children[indx] = new Node();

            }
            if (i == word.length() - 1) {
                current.children[indx].eow = true;
            }
            current = current.children[indx];

        }
    }

    public boolean search(String key) {
        Node current = root;
        for (int i = 0; i < key.length(); i++) {
            int indx = key.charAt(i) - 'a';
            Node node = current.children[indx];

            if (node == null) {
                return false;

            }
            if (i == key.length() - 1 && node.eow == false) {
                return false;
            }
            current = current.children[indx];

        }
        return true;
    }

    public static void loadWords() throws FileNotFoundException {
        File words = new File("src/com/twostarts/20k.txt");

        Scanner sc = new Scanner(words);
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            insert(word);
        }
    }

   

    public static void main(String[] args) throws FileNotFoundException {
        Trie trie = new Trie();

        File words = new File("C:\\Users\\bhand\\Desktop\\dsaproject\\src\\com\\twostarts\\20k.txt");

        Scanner sc = new Scanner(words);
        while (sc.hasNextLine()) {
            String word = sc.nextLine();
            trie.insert(word);
        }
        String s = "would you t";
        s = s.trim();
        System.out.println(s);
        trie.loadWords();
        System.out.println(trie.search("would"));

    }
}
