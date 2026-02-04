package app;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;

import engine.FileSearcher;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;

public class RegexSearchApp extends JFrame {

    private JTextField fileField, regexField;
    private JTextPane resultArea;
    private JButton browseBtn, searchBtn;
    
    // Palette de couleurs moderne et sophistiquée
    private static final Color BACKGROUND_COLOR = new Color(15, 23, 42);        // Slate-900
    private static final Color PANEL_COLOR = new Color(30, 41, 59);            // Slate-800
    private static final Color CARD_COLOR = new Color(51, 65, 85);             // Slate-700
    private static final Color ACCENT_COLOR = new Color(59, 130, 246);         // Blue-500
    private static final Color ACCENT_HOVER = new Color(37, 99, 235);          // Blue-600
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);         // Green-500
    private static final Color SUCCESS_HOVER = new Color(22, 163, 74);         // Green-600
    private static final Color TEXT_COLOR = new Color(241, 245, 249);          // Slate-100
    private static final Color TEXT_MUTED = new Color(148, 163, 184);          // Slate-400
    private static final Color INPUT_COLOR = new Color(248, 250, 252);         // Slate-50
    private static final Color INPUT_BORDER = new Color(203, 213, 225);        // Slate-300
    private static final Color INPUT_FOCUS = new Color(59, 130, 246);          // Blue-500
    private static final Color SHADOW_COLOR = new Color(0, 0, 0, 25);          // Ombre subtile

    public RegexSearchApp() {
        setTitle("🔍 Moteur de Recherche - ERE Engine");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null); // Centrer la fenêtre
        
        // Configuration moderne de la fenêtre
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // Création du header avec gradient
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Panel principal avec les contrôles
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Card pour sélection de fichier
        JPanel fileCard = createModernCard("📁", "Sélection du fichier");
        JPanel fileContent = new JPanel(new BorderLayout(15, 0));
        fileContent.setOpaque(false);
        
        fileField = new JTextField();
        fileField.setPreferredSize(new Dimension(400, 45));
        styleModernTextField(fileField, "Chemin vers le fichier...");
        
        browseBtn = new JButton("📂 Parcourir");
        styleModernButton(browseBtn, ACCENT_COLOR, ACCENT_HOVER);
        browseBtn.setPreferredSize(new Dimension(140, 45));
        
        fileContent.add(fileField, BorderLayout.CENTER);
        fileContent.add(browseBtn, BorderLayout.EAST);
        fileCard.add(fileContent, BorderLayout.CENTER);
        
        mainPanel.add(fileCard);
        mainPanel.add(Box.createVerticalStrut(20));

        // Card pour expression régulière
        JPanel regexCard = createModernCard("🔍", "Expression régulière (ERE)");
        JPanel regexContent = new JPanel(new BorderLayout());
        regexContent.setOpaque(false);
        
        regexField = new JTextField();
        regexField.setPreferredSize(new Dimension(400, 45));
        styleModernTextField(regexField, "Entrez votre motif RegEx...");
        
        regexContent.add(regexField, BorderLayout.CENTER);
        regexCard.add(regexContent, BorderLayout.CENTER);
        
        mainPanel.add(regexCard);
        mainPanel.add(Box.createVerticalStrut(25));

        // Bouton de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setOpaque(false);
        
        searchBtn = new JButton("🚀 Lancer la recherche");
        styleHeroButton(searchBtn);
        searchPanel.add(searchBtn);
        mainPanel.add(searchPanel);
        
        add(mainPanel, BorderLayout.NORTH);

        // Zone de résultats
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsContainer.setBackground(BACKGROUND_COLOR);
        resultsContainer.setBorder(new EmptyBorder(0, 40, 40, 40));
        
        JPanel resultsHeader = new JPanel(new BorderLayout());
        resultsHeader.setOpaque(false);
        resultsHeader.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel resultsLabel = new JLabel("📊 Résultats de la recherche");
        resultsLabel.setForeground(TEXT_COLOR);
        resultsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultsHeader.add(resultsLabel, BorderLayout.WEST);
        
        resultsContainer.add(resultsHeader, BorderLayout.NORTH);
        
        resultArea = new JTextPane();
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(30, 41, 59));
        resultArea.setForeground(TEXT_MUTED);
        resultArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        resultArea.setBorder(new EmptyBorder(20, 25, 20, 25));
        resultArea.setCaretColor(ACCENT_COLOR);
        
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(CARD_COLOR, 2));
        scrollPane.setBackground(CARD_COLOR);
        customizeScrollBar(scrollPane);
        
        resultsContainer.add(scrollPane, BorderLayout.CENTER);
        add(resultsContainer, BorderLayout.CENTER);

        browseBtn.addActionListener(e -> chooseFile());
        searchBtn.addActionListener(e -> search());
        
        // Message de bienvenue
        showWelcomeMessage();
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient de fond
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(59, 130, 246),
                    getWidth(), getHeight(), new Color(147, 51, 234)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 80));
        
        JLabel titleLabel = new JLabel("Moteur de Recherche ERE", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        header.add(titleLabel, BorderLayout.CENTER);
        
        return header;
    }
    
    private JPanel createModernCard(String icon, String title) {
        JPanel card = new JPanel(new BorderLayout(0, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Ombre portée
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(2, 2, getWidth()-2, getHeight()-2, 16, 16);
                
                // Fond de la card
                g2d.setColor(CARD_COLOR);
                g2d.fillRoundRect(0, 0, getWidth()-2, getHeight()-2, 16, 16);
                
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Header de la card
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 12));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        header.add(iconLabel);
        header.add(titleLabel);
        
        card.add(header, BorderLayout.NORTH);
        
        return card;
    }

    private void styleModernTextField(JTextField textField, String placeholder) {
        textField.setBackground(INPUT_COLOR);
        textField.setForeground(new Color(51, 65, 85));
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(INPUT_BORDER, 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        textField.setCaretColor(ACCENT_COLOR);
        
        // Placeholder text
        textField.setText(placeholder);
        textField.setForeground(Color.GRAY);
        
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(new Color(51, 65, 85));
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(INPUT_FOCUS, 2),
                    new EmptyBorder(8, 15, 8, 15)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(Color.GRAY);
                }
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(INPUT_BORDER, 1),
                    new EmptyBorder(8, 15, 8, 15)
                ));
            }
        });
    }
    
    private void styleModernButton(JButton button, Color bgColor, Color hoverColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor, 1),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(hoverColor, 1),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor, 1),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }
        });
    }
    
    private void styleHeroButton(JButton button) {
        button.setPreferredSize(new Dimension(250, 55));
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
            new EmptyBorder(15, 25, 15, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_HOVER);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SUCCESS_HOVER, 2),
                    new EmptyBorder(15, 25, 15, 25)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SUCCESS_COLOR, 2),
                    new EmptyBorder(15, 25, 15, 25)
                ));
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(21, 128, 61)); // Green-700
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_HOVER);
            }
        });
    }
    
    private void customizeScrollBar(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(100, 116, 139);
                this.trackColor = new Color(51, 65, 85);
            }
            
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
    }
    
    private void showWelcomeMessage() {
        StyledDocument doc = resultArea.getStyledDocument();
        Style welcome = resultArea.addStyle("welcome", null);
        StyleConstants.setForeground(welcome, TEXT_MUTED);
        StyleConstants.setItalic(welcome, true);
        StyleConstants.setFontSize(welcome, 14);
        
        try {
            doc.insertString(0, "👋 Bienvenue dans le Moteur de Recherche ERE\n\n", welcome);
            doc.insertString(doc.getLength(), "📁 Sélectionnez un fichier texte\n", welcome);
            doc.insertString(doc.getLength(), "🔍 Entrez votre expression régulière\n", welcome);
            doc.insertString(doc.getLength(), "🚀 Lancez la recherche pour voir les résultats ici\n", welcome);
        } catch (BadLocationException ignored) {}
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            fileField.setText(chooser.getSelectedFile().getAbsolutePath());
            fileField.setForeground(new Color(51, 65, 85));
        }
    }

    private void search() {
        String filepath = fileField.getText();
        String pattern = regexField.getText();
        
        // Vérifier les placeholders
        if (filepath.isEmpty() || filepath.equals("Chemin vers le fichier...") ||
            pattern.isEmpty() || pattern.equals("Entrez votre motif RegEx...")) {
            showModernMessage("⚠️ Veuillez sélectionner un fichier et entrer un motif de recherche", "Paramètres manquants", JOptionPane.WARNING_MESSAGE);
            return;
        }

        resultArea.setText("");
        StyledDocument doc = resultArea.getStyledDocument();
        
        // Styles pour les résultats
        Style header = resultArea.addStyle("header", null);
        StyleConstants.setForeground(header, ACCENT_COLOR);
        StyleConstants.setBold(header, true);
        StyleConstants.setFontSize(header, 16);
        
        Style normal = resultArea.addStyle("normal", null);
        StyleConstants.setForeground(normal, TEXT_MUTED);
        StyleConstants.setFontFamily(normal, "Courier New");
        
        Style highlight = resultArea.addStyle("highlight", null);
        StyleConstants.setForeground(highlight, new Color(34, 197, 94));
        StyleConstants.setBold(highlight, true);
        
        Style lineNumber = resultArea.addStyle("lineNumber", null);
        StyleConstants.setForeground(lineNumber, new Color(156, 163, 175));
        StyleConstants.setFontSize(lineNumber, 12);

        try {
            // Header des résultats
            doc.insertString(doc.getLength(), "🔍 Recherche de: '" + pattern + "'\n", header);
            doc.insertString(doc.getLength(), "📁 Dans: " + new File(filepath).getName() + "\n\n", header);
            
            final int[] lineCount = {0};
            final int[] matchCount = {0};
            
            FileSearcher.search(new File(filepath), pattern, line -> {
                try {
                    lineCount[0]++;
                    matchCount[0]++;
                    
                    // Numéro de ligne
                    doc.insertString(doc.getLength(), String.format("[%03d] ", lineCount[0]), lineNumber);
                    // Icône de match
                    doc.insertString(doc.getLength(), "✓ ", highlight);
                    // Contenu de la ligne
                    doc.insertString(doc.getLength(), line + "\n", normal);
                } catch (BadLocationException ignored) {}
            });
            
            if (matchCount[0] == 0) {
                Style noResult = resultArea.addStyle("noResult", null);
                StyleConstants.setForeground(noResult, new Color(239, 68, 68));
                StyleConstants.setItalic(noResult, true);
                StyleConstants.setFontSize(noResult, 14);
                doc.insertString(doc.getLength(), "🚫 Aucun résultat trouvé pour le motif '" + pattern + "'\n\n", noResult);
                doc.insertString(doc.getLength(), "💡 Suggestions:\n", noResult);
                doc.insertString(doc.getLength(), "  • Vérifiez l'orthographe du motif\n", noResult);
                doc.insertString(doc.getLength(), "  • Essayez un motif plus général\n", noResult);
                doc.insertString(doc.getLength(), "  • Vérifiez que le fichier contient du texte\n", noResult);
            } else {
                Style summary = resultArea.addStyle("summary", null);
                StyleConstants.setForeground(summary, SUCCESS_COLOR);
                StyleConstants.setBold(summary, true);
                doc.insertString(doc.getLength(), "\n🎉 " + matchCount[0] + " correspondance(s) trouvée(s)\n", summary);
            }
            
        } catch (Exception e) {
            showModernMessage("💥 Erreur lors de la recherche: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showModernMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", PANEL_COLOR);
        UIManager.put("Panel.background", PANEL_COLOR);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        UIManager.put("Button.background", ACCENT_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        
        JOptionPane.showMessageDialog(this, message, title, messageType);
        
        // Restaurer les couleurs par défaut
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("OptionPane.messageForeground", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegexSearchApp().setVisible(true));
    }
}