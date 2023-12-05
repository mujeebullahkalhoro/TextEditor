package com.twostarts;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import fonts.ExternalFonts;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener {

    // Menu Bar
    JMenuBar menuBar;

    JMenu file;
    JMenuItem openFile;
    JMenuItem saveFile;
    JMenuItem printFile;
    JMenuItem exit;

    JMenu edit;
    JMenuItem cut;
    JMenuItem copy;    
    JMenuItem paste;
    JMenuItem selectAll;

    JMenu format;
    JMenuItem lineWrap;

    JMenu theme;
    JMenuItem light;
    JMenuItem dark;
    // ---------------------- //


    // top bar;             
    private JPanel topPanel;
    private JComboBox<String> allFonts;
    private JSpinner fontSizeSpinner;
    private JButton colorButton;

    // Text content;
    JScrollPane scrollPane;
    JTextPane textPane;

    JFileChooser fileChooser;

    String fonts[];

    FileWriter writer;
    FileReader reader;

    public TextEditor() throws FileNotFoundException {
        this.setTitle("Text Editor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(600, 500));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //setBackground(Color.DARK_GRAY);
            

        this.setLayout(new BorderLayout());

        textPane = new JTextPane();
        // textPane.setTabSize(4);
        // textPane.setLineWrap(true);
        // textPane.addKeyListener(new TextAreaListener(textPane));
        textPane.getDocument().addDocumentListener(new TextPaneDocumentListener(textPane));
        

        scrollPane = new JScrollPane(textPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        this.add(scrollPane, BorderLayout.CENTER);

        setMenuBar();
        this.setJMenuBar(menuBar);

        setTopPanel();

        fileChooser = new JFileChooser();

        textPane.setFont(ExternalFonts.getJetBrainsFont(16));
        this.setVisible(true);
    }

    private void setMenuBar() {

        menuBar = new JMenuBar();
         
        // file Menu
        file = new JMenu("File");
        file.setMnemonic('F');

        openFile = new JMenuItem("Open File");
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openFile.addActionListener(this::openFileAction);
        file.add(openFile);

        saveFile = new JMenuItem("Save File");
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveFile.addActionListener(this::saveFileAction);
        file.add(saveFile);

        printFile = new JMenuItem("Print File");
        printFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        printFile.addActionListener(this::printFileAction); // Add action listener
        file.add(printFile);

          
        exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exit.addActionListener(this::exitAction); // link to the exit method
        file.add(exit);


        menuBar.add(file);
        // -----------------------------------------------

        // Edit Menu
        edit = new JMenu("Edit");
        edit.setMnemonic('E');

        cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cut.addActionListener(e -> textPane.cut());
        edit.add(cut);

        copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copy.addActionListener(e -> textPane.copy());
        edit.add(copy);

        paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        paste.addActionListener(e -> textPane.paste());
        edit.add(paste);

        selectAll = new JMenuItem("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));

        selectAll.addActionListener(e -> textPane.selectAll());
        edit.add(selectAll);

        menuBar.add(edit);
        // -----------------------------------------------

        // Format Menu
        format = new JMenu("Format");

        lineWrap = new JMenuItem("Word Wrap");
        lineWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK));
        lineWrap.addActionListener(e -> {
            // if (textPane.getLineWrap()) {
            //     textPane.setLineWrap(false);
            // } else {
            //     textPane.setLineWrap(true);
            // }
        });
        format.add(lineWrap);

        menuBar.add(format);
        // -----------------------------------------------

        // Theme;
        theme = new JMenu("Theme");
        theme.setMnemonic('T');

        light = new JMenuItem("Light");
        light.addActionListener(this::lightThemeAction);
        light.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        theme.add(light);

        dark = new JMenuItem("Dark");
        dark.addActionListener(this::darkThemeAction);
        dark.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK));
        theme.add(dark);

        menuBar.add(theme);
        // -----------------------------------------------
    }
    public void printFileAction(ActionEvent e) {
        try {
            textPane.print(); // Print the text area
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    public void darkThemeAction(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
            SwingUtilities.updateComponentTreeUI(TextEditor.this);
        } catch (UnsupportedLookAndFeelException ex) {}
    }

    public void lightThemeAction(ActionEvent e) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
            SwingUtilities.updateComponentTreeUI(TextEditor.this);
        } catch (UnsupportedLookAndFeelException ex) {}
    }
    

    public void exitAction(ActionEvent e) {
        //if(e.getSource()==exit)
        System.exit(0); // Exit the application
    }
    
    public void openFileAction(ActionEvent e) {

        int action = fileChooser.showOpenDialog(this);

        if(action != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();

        StringBuilder text = new StringBuilder("");
        try {
            reader = new FileReader(selectedFile);
            int ch=0;
            while((ch=reader.read()) != -1) {
                text.append((char)ch);
            }

            textPane.setText(text.toString());
            reader.close();
        }

        catch (FileNotFoundException ex) {}
        catch (IOException ex) {}
  }
    
     
    public void saveFileAction(ActionEvent e) {
        
        File selectedFile;
        if( (selectedFile=fileChooser.getSelectedFile()) == null ) {
            int respond = fileChooser.showSaveDialog(this);

            if(respond != JFileChooser.APPROVE_OPTION) {
                return;
            }
        }

        try {
            selectedFile = fileChooser.getSelectedFile();
            writer = new FileWriter(selectedFile);
            writer.write(textPane.getText());
            writer.close();
        }
        
        catch (IOException e1) {}
    }
    

    private void setTopPanel() {
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 3));
        
        // getting all fonts of the system;
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts = ge.getAvailableFontFamilyNames();

        // setting all fonts in combo box;
        allFonts = new JComboBox<>();
        allFonts.setFont(ExternalFonts.getJetBrainsFont(12));
        allFonts.addItem("JetBrains Mono");
        for(String font: fonts) allFonts.addItem(font);
        allFonts.setPreferredSize(new Dimension(150, 25));
        allFonts.addActionListener(this::fontStyleAction);
        allFonts.setSelectedItem("JetBrains Mono");
        topPanel.add(allFonts);

        // font size setup;
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(16, 5, 50, 1));
        fontSizeSpinner.setFont(ExternalFonts.getJetBrainsFont(12));
        fontSizeSpinner.setPreferredSize(new Dimension(100, 25));
        fontSizeSpinner.addChangeListener(this::fontSizeAction);
        topPanel.add(fontSizeSpinner);

        // color button setup;
        colorButton = new JButton("Text Color");
        colorButton.setFont(ExternalFonts.getJetBrainsFont(12));
        colorButton.addActionListener(this);
        colorButton.setPreferredSize(new Dimension(150, 25));
        topPanel.add(colorButton);
        
        this.add(topPanel, BorderLayout.NORTH);
    }

    public void fontStyleAction(ActionEvent e) {
        Font currFont = textPane.getFont();
        String fontName = (String) allFonts.getSelectedItem();

        if(fontName.equals("JetBrains Mono")) {
            textPane.setFont(ExternalFonts.getJetBrainsFont(currFont.getSize()));
        }

        textPane.setFont(new Font(fontName, Font.PLAIN, currFont.getSize()));
    }
   
    public void fontSizeAction(ChangeEvent e) {
        int fontSize = (int) fontSizeSpinner.getValue();
        Font currFont = textPane.getFont();

        textPane.setFont(new Font(currFont.getName(), Font.PLAIN, fontSize));
    }

    
    @Override
  public void actionPerformed(ActionEvent e){

        if(e.getSource()==colorButton){
        Color color = JColorChooser.showDialog(null, "Pick a Color", Color.BLACK);
           textPane.setForeground(color);
      }
  }
}