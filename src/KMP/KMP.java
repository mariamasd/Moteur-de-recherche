package KMP;

public class KMP {

    // Calcul de la table LPS (Longest Prefix Suffix)
    public static int[] computeLPS(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    // Recherche KMP classique
    public static boolean kmpSearch(String text, String pattern, int[] lps) {
        int i = 0; // index texte
        int j = 0; // index motif

        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if (j == pattern.length()) {
                return true; // motif trouvé
            }

            if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    // ✅ Méthode unifiée appelée par PatternDispatcher
    public static boolean search(String text, String pattern) {
        if (pattern.isEmpty())
            return true;
        int[] lps = computeLPS(pattern);
        return kmpSearch(text, pattern, lps);
    }
}