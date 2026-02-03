package automaton;

import regex.RegexNode;
import java.util.concurrent.atomic.AtomicInteger;

public class NFA {

    public State start;
    public State end;

    private static AtomicInteger ID = new AtomicInteger();

    public static NFA fromRegex(RegexNode node) {
        switch (node.type) {

            case CHAR:
                State s1 = new State(ID.getAndIncrement());
                State s2 = new State(ID.getAndIncrement());
                s1.addTransition(node.value, s2);
                return new NFA(s1, s2);

            case DOT:
                State d1 = new State(ID.getAndIncrement());
                State d2 = new State(ID.getAndIncrement());
                for (char c = 32; c < 127; c++) {
                    d1.addTransition(c, d2);
                }
                return new NFA(d1, d2);

            case EPSILON:
                State e1 = new State(ID.getAndIncrement());
                State e2 = new State(ID.getAndIncrement());
                e1.epsilon.add(e2);
                return new NFA(e1, e2);

            case CONCAT:
                NFA left = fromRegex(node.left);
                NFA right = fromRegex(node.right);
                left.end.epsilon.add(right.start);
                return new NFA(left.start, right.end);

            case UNION:
                State u1 = new State(ID.getAndIncrement());
                State u2 = new State(ID.getAndIncrement());
                NFA a = fromRegex(node.left);
                NFA b = fromRegex(node.right);
                u1.epsilon.add(a.start);
                u1.epsilon.add(b.start);
                a.end.epsilon.add(u2);
                b.end.epsilon.add(u2);
                return new NFA(u1, u2);

            case STAR:
                State st1 = new State(ID.getAndIncrement());
                State st2 = new State(ID.getAndIncrement());
                NFA sub = fromRegex(node.left);
                st1.epsilon.add(sub.start);
                st1.epsilon.add(st2);
                sub.end.epsilon.add(sub.start);
                sub.end.epsilon.add(st2);
                return new NFA(st1, st2);
        }
        return null;
    }

    private NFA(State s, State e) {
        start = s;
        end = e;
    }
}