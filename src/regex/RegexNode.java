package regex;

public class RegexNode {

    public enum Type {
        CHAR, DOT, CONCAT, UNION, STAR, EPSILON
    }

    public Type type;
    public char value; // uniquement pour CHAR
    public RegexNode left;
    public RegexNode right;

    public RegexNode(Type type) {
        this.type = type;
    }

    public RegexNode(char value) {
        this.type = Type.CHAR;
        this.value = value;
    }
}