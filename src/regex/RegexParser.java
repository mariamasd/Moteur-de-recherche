package regex;

public class RegexParser {

    private String input;
    private int pos = 0;

    public RegexParser(String input) {
        this.input = input;
    }

    public RegexNode parse() {
        return parseUnion();
    }

    private RegexNode parseUnion() {
        RegexNode left = parseConcat();
        while (pos < input.length() && input.charAt(pos) == '|') {
            pos++;
            RegexNode right = parseConcat();
            RegexNode node = new RegexNode(RegexNode.Type.UNION);
            node.left = left;
            node.right = right;
            left = node;
        }
        return left;
    }

    private RegexNode parseConcat() {
        RegexNode left = parseStarPlusQuestion();
        while (pos < input.length() &&
                input.charAt(pos) != ')' &&
                input.charAt(pos) != '|') {
            RegexNode right = parseStarPlusQuestion();
            RegexNode node = new RegexNode(RegexNode.Type.CONCAT);
            node.left = left;
            node.right = right;
            left = node;
        }
        return left;
    }

    private RegexNode parseStarPlusQuestion() {
        RegexNode node = parseAtom();
        while (pos < input.length()) {
            char c = input.charAt(pos);
            if (c == '*') {
                pos++;
                RegexNode star = new RegexNode(RegexNode.Type.STAR);
                star.left = node;
                node = star;
            } else if (c == '+') {
                pos++;
                // a+ = a CONCAT a*
                RegexNode star = new RegexNode(RegexNode.Type.STAR);
                star.left = node;
                RegexNode concat = new RegexNode(RegexNode.Type.CONCAT);
                concat.left = node;
                concat.right = star;
                node = concat;
            } else if (c == '?') {
                pos++;
                // a? = a | epsilon
                RegexNode union = new RegexNode(RegexNode.Type.UNION);
                union.left = node;
                union.right = new RegexNode(RegexNode.Type.EPSILON);
                node = union;
            } else {
                break;
            }
        }
        return node;
    }

    private RegexNode parseAtom() {
        char c = input.charAt(pos);

        if (c == '(') {
            pos++;
            RegexNode node = parseUnion();
            pos++; // skip ')'
            return node;
        }

        if (c == '.') {
            pos++;
            return new RegexNode(RegexNode.Type.DOT);
        }

        pos++;
        return new RegexNode(c);
    }
}