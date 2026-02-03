package automaton;

import java.util.*;

public class DFA {

    private Set<State> start;
    private Set<State> acceptStates;
    private Map<Set<State>, Map<Character, Set<State>>> transitions;

    public DFA(NFA nfa) {
        transitions = new HashMap<>();
        acceptStates = new HashSet<>();

        start = epsilonClosure(Set.of(nfa.start));
        build(nfa);
    }

    private void build(NFA nfa) {
        Queue<Set<State>> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Set<State> current = queue.poll();
            transitions.putIfAbsent(current, new HashMap<>());

            for (char c = 32; c < 127; c++) {
                Set<State> move = new HashSet<>();
                for (State s : current) {
                    if (s.transitions.containsKey(c))
                        move.addAll(s.transitions.get(c));
                }
                Set<State> closure = epsilonClosure(move);
                if (!closure.isEmpty()) {
                    transitions.get(current).put(c, closure);
                    if (!transitions.containsKey(closure))
                        queue.add(closure);
                }
            }

            if (current.contains(nfa.end))
                acceptStates.addAll(current);
        }
    }

    private Set<State> epsilonClosure(Set<State> states) {
        Stack<State> stack = new Stack<>();
        Set<State> closure = new HashSet<>(states);
        stack.addAll(states);

        while (!stack.isEmpty()) {
            State s = stack.pop();
            for (State e : s.epsilon) {
                if (!closure.contains(e)) {
                    closure.add(e);
                    stack.push(e);
                }
            }
        }
        return closure;
    }

    public boolean matches(String text) {
        for (int i = 0; i < text.length(); i++) {
            Set<State> current = start;
            for (int j = i; j < text.length(); j++) {
                char c = text.charAt(j);
                Map<Character, Set<State>> map = transitions.getOrDefault(current, Collections.emptyMap());
                if (!map.containsKey(c))
                    break;
                current = map.get(c);
                for (State s : current) {
                    if (acceptStates.contains(s))
                        return true;
                }
            }
        }
        return false;
    }
}