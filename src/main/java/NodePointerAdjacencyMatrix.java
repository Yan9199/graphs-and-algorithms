import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NodePointerAdjacencyMatrix<L, D> implements NodePointer<L, D> {
    private final AdjacencyMatrix<L> adjacencyMatrix;
    private final HashMap<Integer, NodePointerAdjacencyMatrix<L, D>> existingNodePointers;
    private final HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<L, D>> existingArcPointers;
    private final int row;
    private D distance;
    private NodePointer<L, D> predecessor;

    /**
     * Erzeugt einen Verweis auf eine Kante eines Graphen, gegeben durch eine Adjazenzmatrix.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param adjacencyMatrix      Die Adjazenzmatrix.
     * @param row                  Die Zeile der Matrix (Knoten des Graphen).
     */
    public NodePointerAdjacencyMatrix(HashMap<Integer, NodePointerAdjacencyMatrix<L, D>> existingNodePointers, HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<L, D>> existingArcPointers, AdjacencyMatrix<L> adjacencyMatrix, int row) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        this.adjacencyMatrix = adjacencyMatrix;
        existingNodePointers.put(this.row = row, this);
    }

    @Override
    public D getDistance() {
        return distance;
    }

    @Override
    public void setDistance(D distance) {
        this.distance = distance;
    }

    @Override
    public NodePointer<L, D> getPredecessor() {
        return predecessor;
    }

    @Override
    public void setPredecessor(NodePointer<L, D> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public Iterator<ArcPointer<L, D>> outgoingArcs() {
        final ArrayList<ArcPointer<L, D>> list = new ArrayList<>();
        final L[] m = adjacencyMatrix.getMatrix()[row];
        for (int i = 0, len = m.length; i < len; i++) {
            if (m[i] == null) continue;
            final ArcPointerAdjacencyMatrix<L, D> a = existingArcPointers.get(new Pair<>(row, i));
            list.add(a == null ? new ArcPointerAdjacencyMatrix<>(existingNodePointers, existingArcPointers, adjacencyMatrix, row, i) : a);
        }
        return list.iterator();
    }
}
