package com.twostarts;

import javax.swing.UIManager;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class Main {
    public static void main(String[] args) throws Exception {
        Trie trie=new Trie();
        trie.loadWords();
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        new TextEditor();
    }
}