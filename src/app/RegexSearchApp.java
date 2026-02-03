package app;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

import engine.FileSearcher;

import java.awt.*;
import java.io.File;

public class RegexSearchApp extends JFrame {

    private JTextField fileField, regexField;
    private JTextPane resultArea;
    private JButton browseBtn, searchBtn;

    public RegexSearchApp() {
        setTitle("Moteur de recherche offline - egrep");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(3, 1));

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.add(new JLabel("Fichier :"));
        fileField = new JTextField(50);
        browseBtn = new JButton("Parcourir");
        filePanel.add(fileField);
        filePanel.add(browseBtn);
        topPanel.add(filePanel);

        JPanel regexPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        regexPanel.add(new JLabel("Motif (ERE) :"));
        regexField = new JTextField(50);
        regexPanel.add(regexField);
        topPanel.add(regexPanel);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBtn = new JButton("Rechercher");
        searchPanel.add(searchBtn);
        topPanel.add(searchPanel);

        add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextPane();
        resultArea.setEditable(false);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        browseBtn.addActionListener(e -> chooseFile());
        searchBtn.addActionListener(e -> search());
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void search() {
        String filepath = fileField.getText();
        String pattern = regexField.getText();

        if (filepath.isEmpty() || pattern.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Fichier ou motif manquant");
            return;
        }

        resultArea.setText("");
        StyledDocument doc = resultArea.getStyledDocument();
        Style normal = resultArea.addStyle("normal", null);

        try {
            FileSearcher.search(new File(filepath), pattern, line -> {
                try {
                    doc.insertString(doc.getLength(), line + "\n", normal);
                } catch (BadLocationException ignored) {
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegexSearchApp().setVisible(true));
    }
}