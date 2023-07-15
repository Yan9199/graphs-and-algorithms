import java.util.ArrayList;
import java.util.List;

public class Graph<L> {
    private final List<GraphNode<L>> nodes;

    /**
     * Speichert die gegebenen Knoten als Graphen ab.
     *
     * @param nodes Die Knoten des Graphen.
     */
    public Graph(List<GraphNode<L>> nodes) {
        this.nodes = nodes;
    }

    /**
     * Erzeugt anhand einer Adjazenzmatrix einen Graphen.
     *
     * @param adjacencyMatrix Die Adjazenzmatrix, die zu einem Graphen konvertiert werden soll.
     */
    public Graph(AdjacencyMatrix<L> adjacencyMatrix) {
        nodes = new ArrayList<>();
        final L[][] matrix = adjacencyMatrix.getMatrix();
        final int n = matrix.length;
        if (n == 0) return;
        for (int i = 0; i < n; i++) nodes.add(new GraphNode<>());
        for (int i = 0; i < n; i++) {
            final List<GraphArc<L>> list = nodes.get(i).getOutgoingArcs();
            for (int j = 0; j < n; j++) {
                final L length = matrix[i][j];
                if (length == null) continue;
                list.add(new GraphArc<>(length, nodes.get(j)));
            }
        }
    }

    /**
     * Gibt die Knoten des Graphen zurück.
     *
     * @return Die Knoten des Graphen.
     */
    public List<GraphNode<L>> getNodes() {
        return nodes;
    }
}
