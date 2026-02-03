import javax.swing.SwingUtilities;

import app.RegexSearchApp;

public class Main {
    public static void main(String[] args) {
        // Lancer l'interface graphique sur le thread Swing
        SwingUtilities.invokeLater(() -> {
            RegexSearchApp app = new RegexSearchApp();
            app.setVisible(true);
        });
    }
}