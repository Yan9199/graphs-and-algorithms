import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NodePointerGraph<L, D> implements NodePointer<L, D> {
    private final HashMap<GraphNode<L>, NodePointerGraph<L, D>> existingNodePointers;
    private final HashMap<GraphArc<L>, ArcPointerGraph<L, D>> existingArcPointers;
    private final GraphNode<L> graphNode;
    private D distance;
    private NodePointer<L, D> predecessor;

    /**
     * Erzeugt einen Verweis auf einen Knoten eines Graphen.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param graphNode            Der Knoten des Graphen, auf den der Verweis erzeugt werden soll.
     */
    public NodePointerGraph(HashMap<GraphNode<L>, NodePointerGraph<L, D>> existingNodePointers, HashMap<GraphArc<L>, ArcPointerGraph<L, D>> existingArcPointers, GraphNode<L> graphNode) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        existingNodePointers.put(this.graphNode = graphNode, this);
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
        for (GraphArc<L> arc : graphNode.getOutgoingArcs()) {
            final ArcPointerGraph<L, D> g = existingArcPointers.get(arc);
            list.add(g == null ? new ArcPointerGraph<>(existingNodePointers, existingArcPointers, arc) : g);
        }
        return list.iterator();
    }
}
