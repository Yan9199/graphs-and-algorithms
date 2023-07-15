import java.util.*;

public class PriorityQueueList<T> implements IPriorityQueue<T> {
    private final Comparator<T> priorityComparator;
    private final List<T> queue;

    /**
     * Erstellt eine Priority Queue basierend auf einer Liste, mit durch priorityComparator induzierter Ordnung.
     *
     * @param priorityComparator Die auf die Priority Queue induzierte Ordnung.
     */
    public PriorityQueueList(Comparator<T> priorityComparator) {
        this.priorityComparator = priorityComparator;
        queue = new LinkedList<>();
    }

    @Override
    public void add(T item) {
        int n = 0;
        for (T t : queue) {
            if (priorityComparator.compare(t, item) <= 0) {
                queue.add(n, item);
                return;
            }
            n++;
        }
        queue.add(item);
    }

    @Override
    public T delete(T item) {
        return queue.remove(item) ? item : null;
    }

    @Override
    public T getFront() {
        return queue.isEmpty() ? null : queue.get(0);
    }

    @Override
    public T deleteFront() {
        return queue.isEmpty() ? null : queue.remove(0);
    }

    @Override
    public int getPosition(T item) {
        final int n = queue.indexOf(item);
        return n == -1 ? -1 : n + 1;
    }

    @Override
    public boolean contains(T item) {
        return queue.contains(item);
    }

    @Override
    public Comparator<T> getPriorityComparator() {
        return priorityComparator;
    }

    @Override
    public void clear() {
        queue.clear();
    }

    /**
     * Gibt die zur Realisierung der Priority Queue genutzte Liste zurück.
     *
     * @return Die Liste, die zur Realisierung der Priority Queue genutzt wird.
     */
    public List<T> getInternalList() {
        return queue;
    }
}
