package com.twostarts;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

class TextAreaListener implements KeyListener {
    StringBuilder currentWord = new StringBuilder();
    Trie trie = new Trie();

    private JTextArea textArea;
    Highlighter highlighter;

    TextAreaListener(JTextArea textArea) {
        this.textArea = textArea;

        // Initialize the highlighter
        highlighter = textArea.getHighlighter();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String word = updateLastModifiedWord();
        boolean isSpelledCorrectly = trie.search(word);

        // Highlight the word if not spelled correctly
        if (!isSpelledCorrectly) {
            highlightIncorrectWord(word);
        } else {
            // Clear the highlight if the word is spelled correctly
            clearHighlight();
        }
    }

    private String updateLastModifiedWord() {
        String text = textArea.getText();
        int caretPosition = textArea.getCaretPosition();

        // Find the word that was last modified
        int start = caretPosition - 1;
        int end = caretPosition;

        while (start >= 0 && !Character.isWhitespace(text.charAt(start))) {
            start--;
        }

        while (end < text.length() && !Character.isWhitespace(text.charAt(end))) {
            end++;
        }

        return text.substring(start + 1, end).toLowerCase();
    }

    private void highlightIncorrectWord(String word) {
        try {
          //  int caretPosition = textArea.getCaretPosition();
            int start = textArea.getText().toLowerCase().indexOf(word);
            int end = start+word.length();
            highlighter.addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    private void clearHighlight() {
        highlighter.removeAllHighlights();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Do nothing on key press
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Do nothing on key release
    }
}
