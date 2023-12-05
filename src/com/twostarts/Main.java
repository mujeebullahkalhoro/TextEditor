package com.twostarts;
import javax.swing.UIManager;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

public class Main {
    public static void main(String[] args) throws Exception {
        SpellChecker.loadWords();
        UIManager.setLookAndFeel(new FlatMacDarkLaf());
        new TextEditor();
    }
}