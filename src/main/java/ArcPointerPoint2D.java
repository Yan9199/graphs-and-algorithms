import java.util.HashMap;

public class ArcPointerPoint2D implements ArcPointer<Double, Double> {
    private final HashMap<Point2D, NodePointerPoint2D> existingNodePointers;
    private final HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> existingArcPointers;
    private final Point2D source, destination;
    private final Double l;
    private final Point2DCollection collection;

    /**
     * Erzeugt einen Pointer auf eine Kante, für eine gegebene Punktsammlung.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param source               Die Quelle der Kante.
     * @param destination          Das Ziel der Kante.
     * @param collection           Die Punktsammlung.
     */
    public ArcPointerPoint2D(HashMap<Point2D, NodePointerPoint2D> existingNodePointers, HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> existingArcPointers, Point2D source, Point2D destination, Point2DCollection collection) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        l = Math.sqrt(Math.pow(source.getX() - destination.getX(), 2) + Math.pow(source.getY() - destination.getY(), 2));
        this.collection = collection;
        existingArcPointers.put(new Pair<>(this.source = source, this.destination = destination), this);
    }

    @Override
    public Double getLength() {
        return l;
    }

    @Override
    public NodePointer<Double, Double> destination() {
        final NodePointerPoint2D n = existingNodePointers.get(destination);
        return n == null ? new NodePointerPoint2D(existingNodePointers, existingArcPointers, destination, collection) : n;
    }
}
