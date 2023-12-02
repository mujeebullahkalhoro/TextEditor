package com.twostarts;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;

class TextPaneDocumentListener implements DocumentListener {
    StringBuilder currentWord = new StringBuilder();
    Trie trie = new Trie();

    private JTextPane textPane;

    TextPaneDocumentListener(JTextPane textPane) {
        this.textPane = textPane;

    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateUnderline(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateUnderline(e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Plain text components do not fire these events
    }

    // private void updateUnderline(DocumentEvent e) {
    // SwingUtilities.invokeLater(() -> {
    // StyledDocument doc = textPane.getStyledDocument();

    // // Remove underlining from the entire document
    // doc.setCharacterAttributes(0, doc.getLength(), new SimpleAttributeSet(),
    // true);

    // try {
    // String text = doc.getText(0, doc.getLength());
    // int start = 0;

    // while (start < text.length()) {
    // int index = text.indexOf(" ", start);
    // if (index == -1) {
    // index = text.length();
    // }

    // String word = text.substring(start, index);
    // AttributeSet attrs = doc.getCharacterElement(start).getAttributes();

    // if (!StyleConstants.isUnderline(attrs) && !trie.search(word)) {
    // // Only underline if it's not already underlined and the word is misspelled
    // doc.setCharacterAttributes(start, index - start, getUnderlineAttribute(),
    // false);
    // doc.setCharacterAttributes(start, index - start, getUnderlineAttribute(),
    // true);
    // }

    // start = index + 1;
    // }
    // } catch (BadLocationException ex) {
    // ex.printStackTrace();
    // }
    // });
    // }

    private SimpleAttributeSet getUnderlineAttribute() {
        SimpleAttributeSet underline = new SimpleAttributeSet();
        StyleConstants.setForeground(underline, Color.RED);
        StyleConstants.setForeground(underline, Color.RED);
        StyleConstants.setUnderline(underline, true);
        return underline;
    }

    private void updateUnderline(DocumentEvent e) {
        SwingUtilities.invokeLater(() -> {
            StyledDocument doc = textPane.getStyledDocument();

            // Remove underlining from the entire document
            doc.setCharacterAttributes(0, doc.getLength(), new SimpleAttributeSet(), true);

            try {
                String text = doc.getText(0, doc.getLength());
                int start = 0;

                while (start < text.length()) {
                    int index = text.indexOf(" ", start);
                    if (index == -1) {
                        index = text.length();
                    }

                    String word = text.substring(start, index);
                    AttributeSet attrs = doc.getCharacterElement(start).getAttributes();

                    if (!StyleConstants.isUnderline(attrs) && !trie.search(word)) {
                        // Only underline if it's not already underlined and the word is misspelled
                        doc.setCharacterAttributes(start, index - start, getUnderlineAttribute(), false);
                        doc.setCharacterAttributes(start, index - start, getUnderlineAttribute(), true);

                        // Add a right-click listener to show the suggestion popup
                        textPane.addMouseListener(new SuggestionMouseListener(word));
                    }

                    start = index + 1;
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });
    }

    private class SuggestionMouseListener extends MouseAdapter {
        private final String misspelledWord;

        public SuggestionMouseListener(String misspelledWord) {
            this.misspelledWord = misspelledWord;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                showSuggestionPopup(e.getComponent(), e.getX(), e.getY());
            }
        }

        private void showSuggestionPopup(Component component, int x, int y) {
            JPopupMenu popupMenu = new JPopupMenu();
            ArrayList<String> suggestions = (ArrayList<String>) trie.getSuggestions(misspelledWord);

            for (String suggestion : suggestions) {
                JMenuItem menuItem = new JMenuItem(suggestion);
                menuItem.addActionListener(actionEvent -> {
                    replaceMisspelledWord(misspelledWord, suggestion);
                    popupMenu.setVisible(false);
                });
                popupMenu.add(menuItem);
            }

            popupMenu.show(component, x, y);
        }

        private void replaceMisspelledWord(String misspelledWord, String replacement) {
            try {
                StyledDocument doc = textPane.getStyledDocument();
                int start = doc.getText(0, doc.getLength()).indexOf(misspelledWord);
                int end = start + misspelledWord.length();

                doc.remove(start, misspelledWord.length());
                doc.insertString(start, replacement, null);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

}