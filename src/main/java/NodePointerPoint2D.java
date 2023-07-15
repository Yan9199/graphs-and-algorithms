import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NodePointerPoint2D implements NodePointer<Double, Double> {
    private final HashMap<Point2D, NodePointerPoint2D> existingNodePointers;
    private final HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> existingArcPointers;
    private final Point2D point;
    final double x, y, l;
    private final Point2DCollection collection;
    private NodePointer<Double, Double> predecessor;
    private Double distance;

    /**
     * Erzeugt einen Verweis auf einen Punkt einer Punktsammlung.
     *
     * @param existingNodePointers Die bereits bestehenden NodePointer.
     * @param existingArcPointers  Die bereits bestehenden ArcPointer.
     * @param point                Der Punkt, auf den verwiesen wird.
     * @param collection           Die Punktsammlung, die den Punkt enthält.
     */
    public NodePointerPoint2D(HashMap<Point2D, NodePointerPoint2D> existingNodePointers, HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> existingArcPointers, Point2D point, Point2DCollection collection) {
        this.existingNodePointers = existingNodePointers;
        this.existingArcPointers = existingArcPointers;
        this.collection = collection;
        x = point.getX();
        y = point.getY();
        l = collection.getMaxArcLength();
        existingNodePointers.put(this.point = point, this);
    }

    @Override
    public Double getDistance() {
        return distance;
    }

    @Override
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public NodePointer<Double, Double> getPredecessor() {
        return predecessor;
    }

    @Override
    public void setPredecessor(NodePointer<Double, Double> predecessor) {
        this.predecessor = predecessor;
    }

    @Override
    public Iterator<ArcPointer<Double, Double>> outgoingArcs() {
        final ArrayList<ArcPointer<Double, Double>> list = new ArrayList<>();
        for (Point2D d : collection.getPoints()) {
            if (point.equals(d) || b(d)) continue;
            final ArcPointerPoint2D a = existingArcPointers.get(new Pair<>(point, d));
            list.add(a == null ? new ArcPointerPoint2D(existingNodePointers, existingArcPointers, point, d, collection) : a);
        }
        return list.iterator();
    }

    private boolean b(Point2D d) {
        return Double.compare(Math.sqrt(Math.pow(x - d.getX(), 2) + Math.pow(y - d.getY(), 2)), l) > 0;
    }

    public Point2D getPoint() {
        return point;
    }
}
