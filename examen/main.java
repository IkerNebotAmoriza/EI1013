package examen;

public class main {
    public static void main(String[]args) {
        EDListGraph<String, Object> g = new EDListGraph<>();
        g.insertNode("A");
        g.insertNode("B");
        g.insertNode("C");
        g.insertEdge("A", "B");
        g.insertEdge("A", "C");

    }
}
