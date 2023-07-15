import java.util.Comparator;
import java.util.HashMap;

public class PriorityQueueHeap<T> implements IPriorityQueue<T> {
    private final Comparator<T> priorityComparator;
    private final HashMap<T, Integer> indexMap;
    private T[] heap;
    private int size;
    private final int len;

    /**
     * Erstellt eine Priority Queue basierend auf einem Heap, mit durch priorityComparator induzierter Ordnung.
     *
     * @param priorityComparator Die auf die Priority Queue induzierte Ordnung.
     * @param capacity           Die Kapazität der Queue.
     */
    @SuppressWarnings("unchecked")
    public PriorityQueueHeap(Comparator<T> priorityComparator, int capacity) {
        this.priorityComparator = priorityComparator;
        indexMap = new HashMap<>();
        heap = (T[]) new Object[len = capacity];
    }

    @Override
    public void add(T item) {
        if (size == len) return;
        indexMap.put(heap[size] = item, size);
        structureUp(size, item);
        size++;
    }

    @Override
    public T delete(T item) {
        if (size == 0) return null;
        final Integer i = indexMap.get(item);
        if (i == null) return null;
        if (i == 0) return deleteFrontHelper();
        final T t = heap[i];
        indexMap.remove(item);
        final int n = --size;
        if (i == n) {
            heap[i] = null;
            return t;
        }
        final T t2 = heap[n];
        heap[n] = null;
        indexMap.remove(heap[i] = t2);
        indexMap.put(t2, i);
        if (structureUp(i, t2)) structureDown(i);
        return t;
    }

    private T deleteFrontHelper() {
        final T t = heap[0];
        indexMap.remove(t);
        final int n = --size;
        if (n == 0) {
            heap[0] = null;
            return t;
        }
        final T t2 = heap[n];
        heap[n] = null;
        indexMap.remove(heap[0] = t2);
        indexMap.put(t2, 0);
        structureDown(0);
        return t;
    }

    /**
     * Tauscht die Position der Elemente
     *
     * @param index0 Die Position des ersten Elements.
     * @param index1 Die Position des zweiten Elements.
     */
    private void swap(int index0, int index1) {
        final T t0 = heap[index0], t1 = heap[index1];
        indexMap.remove(heap[index1] = t0);
        indexMap.remove(heap[index0] = t1);
        indexMap.put(t0, index1);
        indexMap.put(t1, index0);
    }

    @Override
    public T getFront() {
        return size == 0 ? null : heap[0];
    }

    @Override
    public T deleteFront() {
        if (size == 0) return null;
        final T t = heap[0];
        indexMap.remove(t);
        final int n = --size;
        if (n == 0) {
            heap[0] = null;
            return t;
        }
        final T t2 = heap[n];
        heap[n] = null;
        indexMap.remove(heap[0] = t2);
        indexMap.put(t2, 0);
        structureDown(0);
        return t;
    }

    private boolean structureUp(int n, T item) {
        if (n == 0) return true;
        final int i = (n - 1) / 2;
        final T t = heap[i];
        if (priorityComparator.compare(t, item) < 0) {
            swap(i, n);
            structureUp(i, item);
            return false;
        }
        return true;
    }

    private void structureDown(int i) {
        final int i1 = i * 2 + 1, i2, i3;
        if (i1 >= size) return;
        final T t = heap[i], t1 = heap[i1], t2, t3;
        if ((i2 = i1 + 1) == size) {
            if (priorityComparator.compare(t, t1) < 0) swap(i, i1);
            return;
        }
        if (priorityComparator.compare(t1, (t2 = heap[i2])) < 0) {
            t3 = t2;
            i3 = i2;
        } else {
            t3 = t1;
            i3 = i1;
        }
        if (priorityComparator.compare(t, t3) < 0) {
            swap(i, i3);
            structureDown(i3);
        }
    }

    @Override
    public int getPosition(T item) {
        final Integer i = indexMap.get(item);
        if (i == null) return -1;
        if (i == 0) return 1;
        int position = 1, n = 0;
        while (n < size) {
            final T t = heap[n];
            final int e = priorityComparator.compare(item, t);
            if (e < 0) position++;
            else if (e == 0) {
                if (item.equals(t)) return position + shift(n + 1, item);
                position++;
            }
            n++;
        }
        return position;
    }

    private int shift(int n, T item) {
        int shift = 0;
        for (; n < size; n++) if (priorityComparator.compare(item, heap[n]) < 0) shift++;
        return shift;
    }

    @Override
    public boolean contains(T item) {
        return indexMap.containsKey(item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        heap = (T[]) new Object[len];
        indexMap.clear();
        size = 0;
    }

    @Override
    public Comparator<T> getPriorityComparator() {
        return priorityComparator;
    }

    /**
     * Gibt die zugrundeliegende Heapstruktur zurück.
     *
     * @return Die zugrundeliegende Heapstruktur.
     */
    public Object[] getInternalHeap() {
        return heap;
    }
}
