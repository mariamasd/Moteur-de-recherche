package engine;

import automaton.DFA;
import automaton.NFA;
import regex.RegexNode;
import regex.RegexParser;
import KMP.KMP;

public class PatternDispatcher {

    // Test si le motif est une simple concaténation
    // (aucun opérateur Regex)
    public static boolean isConcatOnly(String pattern) {
        return pattern.matches("[a-zA-Z0-9]+");
    }

    // Méthode principale appelée par FileSearcher
    public static boolean match(String line, String pattern) {

        // Cas 1 : concaténation → KMP
        if (isConcatOnly(pattern)) {
            return KMP.search(line, pattern);
        }

        // Cas 2 : vraie RegEx → Automates
        RegexParser parser = new RegexParser(pattern);
        RegexNode tree = parser.parse();

        NFA nfa = NFA.fromRegex(tree);
        DFA dfa = new DFA(nfa);

        return dfa.matches(line);
    }
}