import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Dijkstra<L, D> {
    private final Comparator<D> comparator;
    private final BiFunction<D, L, D> distanceFunction;
    private final IPriorityQueue<NodePointer<L, D>> queue;
    private Predicate<NodePointer<L, D>> predicate;

    /**
     * Erzeugt eine Instanz von Dijkstra, welche den Dijkstra Algorithmus ausführt.
     *
     * @param comparator       Ein Vergleichsoperator, welcher die Priorität, in der die Knoten in Dijkstra abgearbeitet werden, vorgibt.
     * @param distanceFunction Eine Distanzfunktion, welche für eine gegebene Quellknotendistanz und eine Kantenlänge die Zielknotendistanz ermittelt.
     * @param queueFactory     Erzeugt für einen gegebenen Vergleichsoperator eine PriorityQueue, welche nach diesem Vergleichskriterium die Knoten sortiert.
     */
    public Dijkstra(Comparator<D> comparator, BiFunction<D, L, D> distanceFunction, Function<Comparator<NodePointer<L, D>>, IPriorityQueue<NodePointer<L, D>>> queueFactory) {
        this.comparator = comparator;
        this.distanceFunction = distanceFunction;
        queue = queueFactory.apply((n1, n2) -> comparator.compare(n1.getDistance(), n2.getDistance()));
    }

    /**
     * Initialisiert den Algorithmus von Dijkstra.
     *
     * @param startNode Der Startknoten, von dem der Algorithmus aus die Suche startet.
     */
    public void initialize(NodePointer<L, D> startNode) {
        queue.clear();
        queue.add(startNode);
    }

    /**
     * Initialisiert den Algorithmus von Dijkstra, erhält zusätzlich ein Prädikat, welches beim Eintreffen die Suche vorzeitig beendet.
     *
     * @param startNode Der Startknoten, von dem der Algorithmus aus die Suche startet.
     * @param predicate Das Prädikat, welches beim Eintreffen die Suche vorzeitig beendet.
     */
    public void initialize(NodePointer<L, D> startNode, Predicate<NodePointer<L, D>> predicate) {
        queue.clear();
        queue.add(startNode);
        this.predicate = predicate;
    }

    /**
     * Startet den Algorithms von Dijkstra.
     *
     * @return Alle ermittelten Knoten, ausgenommen den Startknoten.
     */
    public List<NodePointer<L, D>> run() {
        final ArrayList<NodePointer<L, D>> list = new ArrayList<>();
        final NodePointer<L, D> startNode = queue.deleteFront();
        if (predicate != null && predicate.test(startNode)) return list;
        assert startNode != null;
        expandNode(startNode);
        while (true) {
            final NodePointer<L, D> currentNode = queue.deleteFront();
            if (currentNode == null) return list;
            list.add(currentNode);
            if (predicate != null && predicate.test(currentNode)) return list;
            expandNode(currentNode);
        }
    }

    /**
     * Expandiert den aktuellen Knoten, wie aus Dijkstra bekannt.
     *
     * @param currentNode Zu expandierender Knoten.
     */
    private void expandNode(NodePointer<L, D> currentNode) {
        final Iterator<ArcPointer<L, D>> it = currentNode.outgoingArcs();
        while (it.hasNext()) {
            final ArcPointer<L, D> a = it.next();
            final NodePointer<L, D> n = a.destination();
            final D d = distanceFunction.apply(currentNode.getDistance(), a.getLength()), dist = n.getDistance();
            if (dist == null) {
                n.setDistance(d);
                n.setPredecessor(currentNode);
                queue.add(n);
            } else if (comparator.compare(d, dist) > 0) {
                n.setDistance(d);
                n.setPredecessor(currentNode);
                queue.add(queue.delete(n));
            }
        }
    }

    /**
     * Überprüft, ob der Algorithmus von Dijkstra terminiert.
     *
     * @param currentNode Der Knoten, anhand dessen überprüft wird, ob der Algorithmus terminiert.
     * @return true, falls der Algorithmus terminiert.
     */
    private boolean finished(NodePointer<L, D> currentNode) {
        return currentNode == null || predicate != null && predicate.test(currentNode);
    }
}
