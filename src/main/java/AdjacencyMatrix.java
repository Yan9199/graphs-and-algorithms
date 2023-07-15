import java.util.List;

public class AdjacencyMatrix<L> {
    private final L[][] matrix;

    /**
     * Initialisiert die Adjazenzmatrix über die gegebene Matrix.
     *
     * @param matrix Die Matrix, die als Adjazenzmatrix genutzt werden soll.
     */
    public AdjacencyMatrix(L[][] matrix) {
        this.matrix = matrix;
    }

    /**
     * Erzeugt aus dem gegebenen Graph eine Adjazenzmatrix.
     *
     * @param graph Der zu konvertierende Graph.
     */
    @SuppressWarnings("unchecked")
    public AdjacencyMatrix(Graph<L> graph) {
        final List<GraphNode<L>> nodes = graph.getNodes();
        final int n = nodes.size();
        matrix = (L[][]) new Object[n][n];
        for (int i = 0; i < n; i++)
            for (GraphArc<L> arc : nodes.get(i).getOutgoingArcs())
                matrix[i][nodes.indexOf(arc.getDestination())] = arc.getLength();
    }

    /**
     * Gibt die Adjazenzmatrix zurück.
     *
     * @return Die Adjazenzmatrix.
     */
    public L[][] getMatrix() {
        return matrix;
    }
}
