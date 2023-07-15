import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestDijkstra {

    @Nested
    class TestGraphAndMatrix {
        private static final int count = 5;
        private static final Comparator<Double> cmp = (d1, d2) -> Double.compare(d2, d1);
        private static final BiFunction<Double, Double, Double> dFunc = Double::sum;
        private static final Function<Comparator<NodePointer<Double, Double>>, IPriorityQueue<NodePointer<Double, Double>>> qF1 = PriorityQueueList::new, qF2 = a -> new PriorityQueueHeap<>(a, 10);
        private static final Graph<Double> graph = createGraph();
        private static final AdjacencyMatrix<Double> adjacencyMatrix = new AdjacencyMatrix<>(graph), aMatrix = createMatrix();
        private static final HashMap<Integer, NodePointerAdjacencyMatrix<Double, Double>> eNP = new HashMap<>();
        private static final HashMap<Integer, NodePointerAdjacencyMatrix<Double, Double>> eNP2 = new HashMap<>();
        private static final HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<Double, Double>> eAP = new HashMap<>();
        private static final HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<Double, Double>> eAP2 = new HashMap<>();
        private static final NodePointerAdjacencyMatrix<Double, Double> startNode = new NodePointerAdjacencyMatrix<>(eNP, eAP, adjacencyMatrix, 0), firstNode = new NodePointerAdjacencyMatrix<>(eNP2, eAP2, aMatrix, 0);

        static {
            startNode.setDistance(0d);
            firstNode.setDistance(0d);
        }

        private static final PathFinder<Double, Double> pathFinder = new PathFinder<>();

        @Test
        void checkSameGraphMatrix() {
            checkGraph();
            System.out.println();
            checkGraph(new Graph<>(adjacencyMatrix));
        }

        @Test
        void checkMatrix1() {
            final Object[][] matrix = adjacencyMatrix.getMatrix();
            char c = 'A';
            for (int i = 0; i < 5; i++) {
                System.out.print("Node " + c++ + ": ");
                for (int j = 0; j < 5; j++) System.out.print(matrix[i][j] + (j < 4 ? ", " : ""));
                System.out.println();
                System.out.println();
            }
        }

        @Test
        void checkMatrix2() {
            final Object[][] matrix = aMatrix.getMatrix();
            char c = 'A';
            for (int i = 0; i < 5; i++) {
                System.out.print("Node " + c++ + ": ");
                for (int j = 0; j < 5; j++) System.out.print(matrix[i][j] + (j < 4 ? ", " : ""));
                System.out.println();
                System.out.println();
            }
        }

        void checkGraph() {
            char i = 'A';
            for (GraphNode<Double> graphNode : graph.getNodes()) {
                System.out.println("Node " + i++ + ": ");
                printArcs(graphNode.getOutgoingArcs());
                System.out.println();
            }
        }

        void checkGraph(Graph<Double> graph) {
            char i = 'A';
            for (GraphNode<Double> graphNode : graph.getNodes()) {
                System.out.println("Node " + i++ + ": ");
                printArcs(graphNode.getOutgoingArcs());
                System.out.println();
            }
        }

        void printArcs(List<GraphArc<Double>> outgoingArcs) {
            int i = 1;
            for (GraphArc<Double> graphArc : outgoingArcs)
                System.out.println("Arc #" + i++ + ": " + graphArc.getLength());
        }

        @Test
        void runTests() {
            testDijkstraWithList1();
            System.out.println();
            testDijkstraWithList2();
            System.out.println();
            testDijkstraWithHeap1();
            System.out.println();
            testDijkstraWithHeap2();
        }

        void testDijkstraWithList1() {
            for (var v : createDijkstra(true, true)) System.out.println(pathFinder.apply(v));
        }

        void testDijkstraWithList2() {
            for (var v : createDijkstra(true, false)) System.out.println(pathFinder.apply(v));
        }

        void testDijkstraWithHeap1() {
            for (var v : createDijkstra(false, true)) System.out.println(pathFinder.apply(v));
        }

        void testDijkstraWithHeap2() {
            for (var v : createDijkstra(false, false)) System.out.println(pathFinder.apply(v));
        }

        List<NodePointer<Double, Double>> createDijkstra(boolean b, boolean b2) {
            final Dijkstra<Double, Double> dijkstra = new Dijkstra<>(cmp, dFunc, b ? qF1 : qF2);
            if (b2) {
                eNP.clear();
                eAP.clear();
                eNP.put(0, startNode);
                dijkstra.initialize(startNode);
            } else {
                eNP2.clear();
                eAP2.clear();
                eNP2.put(0, firstNode);
                dijkstra.initialize(firstNode);
            }
            return dijkstra.run();
        }

        @Test
        void testDijkstra() {
            final ArrayList<GraphNode<Double>> nodes = new ArrayList<>(10);
            final Graph<Double> graph = new Graph<>(nodes);
            for (int i = 0; i < 10; i++) nodes.add(new GraphNode<>());
            final ArrayList<GraphArc<Double>>
                    o0 = new ArrayList<>(),
                    o1 = new ArrayList<>(),
                    o2 = new ArrayList<>(),
                    o3 = new ArrayList<>(),
                    o4 = new ArrayList<>(),
                    o5 = new ArrayList<>(),
                    o6 = new ArrayList<>(),
                    o7 = new ArrayList<>(),
                    o8 = new ArrayList<>(),
                    o9 = new ArrayList<>();
            o0.add(new GraphArc<>(6d, nodes.get(1)));
            o0.add(new GraphArc<>(1d, nodes.get(3)));
            o0.add(new GraphArc<>(24d, nodes.get(7)));
            o0.add(new GraphArc<>(30d, nodes.get(8)));
            nodes.get(0).setOutgoingArcs(o0);
            o1.add(new GraphArc<>(6d, nodes.get(0)));
            o1.add(new GraphArc<>(5d, nodes.get(2)));
            o1.add(new GraphArc<>(2d, nodes.get(3)));
            o1.add(new GraphArc<>(2d, nodes.get(4)));
            o1.add(new GraphArc<>(12d, nodes.get(6)));
            o1.add(new GraphArc<>(17d, nodes.get(7)));
            nodes.get(1).setOutgoingArcs(o1);
            o2.add(new GraphArc<>(5d, nodes.get(1)));
            o2.add(new GraphArc<>(5d, nodes.get(4)));
            o2.add(new GraphArc<>(1d, nodes.get(5)));
            o2.add(new GraphArc<>(11d, nodes.get(6)));
            nodes.get(2).setOutgoingArcs(o2);
            o3.add(new GraphArc<>(1d, nodes.get(0)));
            o3.add(new GraphArc<>(2d, nodes.get(1)));
            o3.add(new GraphArc<>(1d, nodes.get(4)));
            o3.add(new GraphArc<>(27d, nodes.get(8)));
            o3.add(new GraphArc<>(30d, nodes.get(9)));
            nodes.get(3).setOutgoingArcs(o3);
            o4.add(new GraphArc<>(2d, nodes.get(1)));
            o4.add(new GraphArc<>(5d, nodes.get(2)));
            o4.add(new GraphArc<>(1d, nodes.get(3)));
            o4.add(new GraphArc<>(7d, nodes.get(5)));
            o4.add(new GraphArc<>(50d, nodes.get(9)));
            nodes.get(4).setOutgoingArcs(o4);
            o5.add(new GraphArc<>(1d, nodes.get(2)));
            o5.add(new GraphArc<>(7d, nodes.get(4)));
            o5.add(new GraphArc<>(8d, nodes.get(6)));
            o5.add(new GraphArc<>(3d, nodes.get(9)));
            nodes.get(5).setOutgoingArcs(o5);
            o6.add(new GraphArc<>(12d, nodes.get(1)));
            o6.add(new GraphArc<>(11d, nodes.get(2)));
            o6.add(new GraphArc<>(8d, nodes.get(5)));
            o6.add(new GraphArc<>(5d, nodes.get(7)));
            nodes.get(6).setOutgoingArcs(o6);
            o7.add(new GraphArc<>(24d, nodes.get(0)));
            o7.add(new GraphArc<>(17d, nodes.get(1)));
            o7.add(new GraphArc<>(5d, nodes.get(6)));
            nodes.get(7).setOutgoingArcs(o7);
            o8.add(new GraphArc<>(30d, nodes.get(0)));
            o8.add(new GraphArc<>(27d, nodes.get(3)));
            o8.add(new GraphArc<>(15d, nodes.get(9)));
            nodes.get(8).setOutgoingArcs(o8);
            o9.add(new GraphArc<>(30d, nodes.get(3)));
            o9.add(new GraphArc<>(3d, nodes.get(5)));
            o9.add(new GraphArc<>(50d, nodes.get(4)));
            o9.add(new GraphArc<>(15d, nodes.get(8)));
            nodes.get(9).setOutgoingArcs(o9);

            AdjacencyMatrix<Double> aM = new AdjacencyMatrix<>(graph);
            final HashMap<Integer, NodePointerAdjacencyMatrix<Double, Double>> HM1 = new HashMap<>();
            final HashMap<Pair<Integer, Integer>, ArcPointerAdjacencyMatrix<Double, Double>> HM2 = new HashMap<>();

            for (int row = 0; row < 10; row++) {
                //final int endRow = ((row + 3) * 2) % 10;
                final NodePointerAdjacencyMatrix<Double, Double> n = new NodePointerAdjacencyMatrix<>(HM1, HM2, aM, row)/*, n2 = endRow == row ? n : new NodePointerAdjacencyMatrix<>(HM1, HM2, aM, endRow)*/;
                final Predicate<NodePointer<Double, Double>> predicate = (NodePointer<Double, Double> p) -> /*p.equals(n2)*/ false;
                n.setDistance(0d);
                //System.out.println("Row: " + row + ", endRow: " + endRow);
                for (int i = 0; i < 2; i++) {
                    HM1.clear();
                    HM2.clear();
                    HM1.put(row, n); /*if (row != endRow) HM1.put(endRow, n2);*/

                    final Dijkstra<Double, Double> dijkstra = new Dijkstra<>(cmp, dFunc, i == 0 ? qF1 : qF2);
                    dijkstra.initialize(n, predicate);
                    for (var v : dijkstra.run()) System.out.println(pathFinder.apply(v));
                    System.out.println();
                    System.out.println();
                }
                System.out.println();
                System.out.println();
            }
        }

        static Graph<Double> createGraph() {
            final ArrayList<GraphNode<Double>> nodes = new ArrayList<>(count);
            final Graph<Double> graph = new Graph<>(nodes);
            for (int i = 0; i < 5; i++) nodes.add(new GraphNode<>());
            final ArrayList<GraphArc<Double>> outgoingArcs0 = new ArrayList<>(), outgoingArcs1 = new ArrayList<>(), outgoingArcs2 = new ArrayList<>(), outgoingArcs3 = new ArrayList<>(), outgoingArcs4 = new ArrayList<>();
            outgoingArcs0.add(new GraphArc<>(6d, nodes.get(1)));
            outgoingArcs0.add(new GraphArc<>(1d, nodes.get(3)));
            nodes.get(0).setOutgoingArcs(outgoingArcs0);
            outgoingArcs1.add(new GraphArc<>(6d, nodes.get(0)));
            outgoingArcs1.add(new GraphArc<>(5d, nodes.get(2)));
            outgoingArcs1.add(new GraphArc<>(2d, nodes.get(3)));
            outgoingArcs1.add(new GraphArc<>(2d, nodes.get(4)));
            nodes.get(1).setOutgoingArcs(outgoingArcs1);
            outgoingArcs2.add(new GraphArc<>(5d, nodes.get(1)));
            outgoingArcs2.add(new GraphArc<>(5d, nodes.get(4)));
            nodes.get(2).setOutgoingArcs(outgoingArcs2);
            outgoingArcs3.add(new GraphArc<>(1d, nodes.get(0)));
            outgoingArcs3.add(new GraphArc<>(2d, nodes.get(1)));
            outgoingArcs3.add(new GraphArc<>(1d, nodes.get(4)));
            nodes.get(3).setOutgoingArcs(outgoingArcs3);
            outgoingArcs4.add(new GraphArc<>(2d, nodes.get(1)));
            outgoingArcs4.add(new GraphArc<>(5d, nodes.get(2)));
            outgoingArcs4.add(new GraphArc<>(1d, nodes.get(3)));
            nodes.get(4).setOutgoingArcs(outgoingArcs4);
            return graph;
        }

        static AdjacencyMatrix<Double> createMatrix() {
            Double[][] matrix = new Double[count][count];
            matrix[0][1] = 6d;
            matrix[0][3] = 1d;
            matrix[1][0] = 6d;
            matrix[1][2] = 5d;
            matrix[1][3] = 2d;
            matrix[1][4] = 5d;
            matrix[2][1] = 5d;
            matrix[2][4] = 5d;
            matrix[3][0] = 1d;
            matrix[3][1] = 2d;
            matrix[3][4] = 1d;
            matrix[4][1] = 2d;
            matrix[4][2] = 5d;
            matrix[4][3] = 1d;
            return new AdjacencyMatrix<>(matrix);
        }
    }

    @Nested
    class TestPointer2D {
        private static final Comparator<Double> cmp = (d1, d2) -> Double.compare(d2, d1);
        private static final BiFunction<Double, Double, Double> dFunc = Double::sum;
        private static final Function<Comparator<NodePointer<Double, Double>>, IPriorityQueue<NodePointer<Double, Double>>> qF1 = PriorityQueueList::new;
        private static final Comparator<NodePointer<Double, Double>> cmpPoint2D = Comparator.comparingDouble((NodePointer<Double, Double> p) -> ((NodePointerPoint2D) p).getPoint().getX());
        private static final PathFinder<Double, Double> pathFinder = new PathFinder<>();

        @Nested
        class Test1 {
            private static final double maxArcLength = 20;
            private static final int pointCount = 10;
            private static final Point2D from = new Point2D(-10, -10), to = new Point2D(10, 10);
            private static final Point2DCollection collection = new Point2DCollection(pointCount, from, to, maxArcLength);
            private static final Point2D point2D = new Point2D(0, 0);

            static {
                collection.getPoints().add(point2D);
            }

            private static final HashMap<Point2D, NodePointerPoint2D> eNP = new HashMap<>();
            private static final HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> eAP = new HashMap<>();
            private static final NodePointerPoint2D startNode = new NodePointerPoint2D(eNP, eAP, point2D, collection);

            static {
                startNode.setDistance(0d);
            }

            private static final Function<Comparator<NodePointer<Double, Double>>, IPriorityQueue<NodePointer<Double, Double>>> qF2 = a -> new PriorityQueueHeap<>(a, pointCount);

            @Test
            void testDijkstraWithListAndHeap() {
                final List<NodePointer<Double, Double>> list = createDijkstra(true);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();

                final List<NodePointer<Double, Double>> list2 = createDijkstra(false);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list2) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i2 = 0;
                for (NodePointer<Double, Double> n : list2) System.out.println(i2++ + ": " + n);
                System.out.println();
            }

            @Test
            void testDijkstraWithList() {
                final List<NodePointer<Double, Double>> list = createDijkstra(true);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();
            }

            @Test
            void testDijkstraWithHeap() {
                final List<NodePointer<Double, Double>> list = createDijkstra(false);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();
            }

            // override toString method
            /*@Test
            void testSame() {
                final List<NodePointer<Double, Double>> list1 = createDijkstra(true);
                final List<NodePointer<Double, Double>> list2 = createDijkstra(false);
                list1.sort(cmpPoint2D); list2.sort(cmpPoint2D);
                assertEquals(list1.toString(), list2.toString());
                final PathFinder<Double, Double> pathFinder = new PathFinder<>();
                final Iterator<NodePointer<Double, Double>> it1 = list1.iterator(), it2 = list2.iterator();
                while (it1.hasNext() && it2.hasNext()) assertEquals(pathFinder.apply(it1.next()).toString(), pathFinder.apply(it2.next()).toString());
                assertTrue(!it1.hasNext() && !it2.hasNext());
            }*/

            List<NodePointer<Double, Double>> createDijkstra(boolean b) {
                eNP.clear();
                eAP.clear();
                eNP.put(point2D, startNode);
                final Dijkstra<Double, Double> dijkstra = new Dijkstra<>(cmp, dFunc, b ? qF1 : qF2);
                dijkstra.initialize(startNode);
                return dijkstra.run();
            }
        }

        @Nested
        class Test2 {
            private static final int pointCount = 100;
            private static final double maxArcLength = 3;
            private static final Point2D startPoint = new Point2D(0, 0);
            private static final Point2DCollection collection = makeCollection();
            private static final HashMap<Point2D, NodePointerPoint2D> eNP = new HashMap<>();
            private static final HashMap<Pair<Point2D, Point2D>, ArcPointerPoint2D> eAP = new HashMap<>();
            private final static NodePointerPoint2D startNode = new NodePointerPoint2D(eNP, eAP, startPoint, collection);

            static {
                startNode.setDistance(0d);
            }

            private static final Function<Comparator<NodePointer<Double, Double>>, IPriorityQueue<NodePointer<Double, Double>>> qF2 = a -> new PriorityQueueHeap<>(a, pointCount);

            @Test
            void testDijkstraWithListAndHeap() {
                final List<NodePointer<Double, Double>> list = createDijkstra(true);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();

                final List<NodePointer<Double, Double>> list2 = createDijkstra(false);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list2) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i2 = 0;
                for (NodePointer<Double, Double> n : list2) System.out.println(i2++ + ": " + n);
                System.out.println();
            }

            @Test
            void testDijkstraWithList() {
                final List<NodePointer<Double, Double>> list = createDijkstra(true);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();
            }

            @Test
            void testDijkstraWithHeap() {
                final List<NodePointer<Double, Double>> list = createDijkstra(false);
                System.out.println(collection);
                System.out.println();
                printArcsLength(startNode);
                System.out.println();
                for (NodePointer<Double, Double> n : list) System.out.println(pathFinder.apply(n));
                System.out.println();
                System.out.println("list:");
                int i = 0;
                for (NodePointer<Double, Double> n : list) System.out.println(i++ + ": " + n);
                System.out.println();
            }

            // Override toString method
            /*@Test
            void testSame() {
                final List<NodePointer<Double, Double>> list1 = createDijkstra(true), list2 = createDijkstra(false);
                list1.sort(cmpPoint2D); list2.sort(cmpPoint2D);
                assertEquals(list1.toString(), list2.toString());
                final PathFinder<Double, Double> pathFinder = new PathFinder<>();
                final Iterator<NodePointer<Double, Double>> it1 = list1.iterator(), it2 = list2.iterator();
                while (it1.hasNext() && it2.hasNext()) assertEquals(pathFinder.apply(it1.next()).toString(), pathFinder.apply(it2.next()).toString());
                assertTrue(!it1.hasNext() && !it2.hasNext());
            }*/

            List<NodePointer<Double, Double>> createDijkstra(boolean b) {
                eNP.clear();
                eAP.clear();
                eNP.put(startPoint, startNode);
                final Dijkstra<Double, Double> dijkstra = new Dijkstra<>(cmp, dFunc, b ? qF1 : qF2);
                dijkstra.initialize(startNode);
                return dijkstra.run();
            }

            static Point2DCollection makeCollection() {
                final ArrayList<Point2D> list = new ArrayList<>(pointCount);
                list.add(new Point2D(0, 0));
                list.add(new Point2D(0, 3));
                list.add(new Point2D(0, 6));
                list.add(new Point2D(0, 9));
                list.add(new Point2D(0, 12));
                list.add(new Point2D(0, 15));
                return new Point2DCollection(list, maxArcLength);
            }
        }

        void printArcsLength(NodePointerPoint2D n) {
            int i = 0;
            System.out.println("______");
            final Iterator<ArcPointer<Double, Double>> it = n.outgoingArcs();
            while (it.hasNext()) {
                System.out.println("Arc #" + ++i + ": " + it.next().getLength());
            }
            System.out.println("------");
        }
    }
}
