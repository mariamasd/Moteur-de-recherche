import javax.swing.SwingUtilities;

import app.RegexSearchApp;

public class Main {
    public static void main(String[] args) {
        // Activer l'anti-aliasing pour un rendu plus lisse
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Lancer l'interface graphique sur le thread Swing
        SwingUtilities.invokeLater(() -> {
            RegexSearchApp app = new RegexSearchApp();
            app.setVisible(true);
        });
    }
}