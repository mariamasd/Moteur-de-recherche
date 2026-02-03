package automaton;

import java.util.*;

public class State {

    public int id;

    // transitions sur caractères
    public Map<Character, Set<State>> transitions;

    // transitions epsilon
    public Set<State> epsilon;

    // ✅ Constructeur avec ID
    public State(int id) {
        this.id = id;
        this.transitions = new HashMap<>();
        this.epsilon = new HashSet<>();
    }

    public void addTransition(char c, State to) {
        transitions.computeIfAbsent(c, k -> new HashSet<>()).add(to);
    }

    @Override
    public String toString() {
        return "S" + id;
    }
}