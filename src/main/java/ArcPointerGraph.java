import java.util.HashMap;

public class ArcPointerGraph<L, D> implements ArcPointer<L, D> {
    private final HashMap<GraphNode<L>, NodePointerGraph<L, D>> existingNodePointers;
    private final HashMap<GraphArc<L>, ArcPointerGraph<L, D>> existingArcPointers;
    private final GraphArc<L> graphArc;
    private final L l;

    /**
     * Erzeugt einen Pointer auf eine Kante, für einen gegebenen Graphen.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param graphArc             Die Kante des Graphen, auf die der Pointer verweist.
     */
    public ArcPointerGraph(HashMap<GraphNode<L>, NodePointerGraph<L, D>> existingNodePointers, HashMap<GraphArc<L>, ArcPointerGraph<L, D>> existingArcPointers, GraphArc<L> graphArc) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        l = graphArc.getLength();
        existingArcPointers.put(this.graphArc = graphArc, this);
    }

    @Override
    public L getLength() {
        return l;
    }

    @Override
    public NodePointer<L, D> destination() {
        final GraphNode<L> g = graphArc.getDestination();
        final NodePointerGraph<L, D> n = existingNodePointers.get(g);
        return n == null ? new NodePointerGraph<>(existingNodePointers, existingArcPointers, g) : n;
    }
}
