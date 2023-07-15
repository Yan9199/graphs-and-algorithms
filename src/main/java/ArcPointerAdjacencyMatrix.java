import java.util.HashMap;

public class ArcPointerAdjacencyMatrix<L, D> implements ArcPointer<L, D> {
    private final AdjacencyMatrix<L> adjacencyMatrix;
    private final HashMap<Integer, NodePointerAdjacencyMatrix<L, D>> existingNodePointers;
    private final HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<L, D>> existingArcPointers;
    private final int row, column;
    private final L l;

    /**
     * Erzeugt einen Pointer auf eine Kante eines Graphen, gegeben durch eine Adjazenzmatrix.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param adjacencyMatrix      Die Adjazenzmatrix, die den Graphen bildet.
     * @param row                  Die Zeile der Matrix (Quelle der Kante).
     * @param column               Die Spalte der Matrix (Ziel der Kante).
     */
    public ArcPointerAdjacencyMatrix(HashMap<Integer, NodePointerAdjacencyMatrix<L, D>> existingNodePointers, HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<L, D>> existingArcPointers, AdjacencyMatrix<L> adjacencyMatrix, int row, int column) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        l = (this.adjacencyMatrix = adjacencyMatrix).getMatrix()[row][column];
        existingArcPointers.put(new Pair<>(this.row = row, this.column = column), this);
    }

    @Override
    public L getLength() {
        return l;
    }

    @Override
    public NodePointer<L, D> destination() {
        final NodePointerAdjacencyMatrix<L, D> n = existingNodePointers.get(column);
        return n == null ? new NodePointerAdjacencyMatrix<>(existingNodePointers, existingArcPointers, adjacencyMatrix, column) : n;
    }
}
