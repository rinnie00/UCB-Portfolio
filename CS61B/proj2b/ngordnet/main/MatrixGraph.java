package ngordnet.main;

import java.util.*;

public class MatrixGraph implements Graph {
    private static final int ORIGINALSIZE = 10;
    private static final double RESIZEFACTOR = 1.5;
    private int v;
    private int e;
    boolean[][] adjcmatrix;

    public MatrixGraph() {
        v = 0;
        e = 0;
        adjcmatrix = new boolean[ORIGINALSIZE][ORIGINALSIZE];
    }

    @Override
    public void addEdge(int from, int to) {
        if (from < v && to < v) {
            adjcmatrix[from][to] = true;
            e += 1;
        }
    }

    @Override
    public int V() {
        return v;
    }

    @Override
    public int E() {
        return e;
    }

    @Override
    public void addNode() {
        v += 1;
        if (v + 1 == adjcmatrix.length) {
            resize((int) (adjcmatrix.length * RESIZEFACTOR));
        }
    }

    private void resize(int newSize) {
        boolean[][] newMartix = new boolean[newSize][newSize];
        for (int i = 0; i <= V(); i++) {
            for (int j = 0; j <= V(); j++) {
                newMartix[i][j] = adjcmatrix[i][j];
            }
        }
        adjcmatrix = newMartix;
    }

    @Override
    public Set<Integer> getNodes() {
        return null;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> result = new HashSet<>();
        for (int i = 0; i < E(); i++) {
            if (adjcmatrix[node][i]) {
                result.add(i);
            }
        }
        return result;
    }

    @Override
    public Iterator<Integer> iteratorFromex(int node) {
        return new FromexIterator(node);
    }

    @Override
    public Iterator<Integer> iteratorFromin(int node) {
        return new FrominIterator(node);
    }

    private class FrominIterator implements Iterator<Integer> {
        private int node;
        private Queue<Integer> queue;
        private boolean[] mark;

        public FrominIterator(int node) {
            this.node = node;
            this.queue = new LinkedList<Integer>();
            queue.add(node);
            this.mark = new boolean[V()];
            mark[node] = true;
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Integer next() {
            int result;
            result = queue.remove();
            Set<Integer> neighbors = getNeighbors(result);
            if (!neighbors.isEmpty()) {
                for (int i : neighbors) {
                    if (!mark[i]) {
                        queue.add(i);
                        mark[i] = true;
                    }
                }
            }
            return result;
        }
    }

    private class FromexIterator implements Iterator<Integer> {
        private int node;
        private Queue<Integer> queue;
        private boolean[] mark;

        public FromexIterator(int node) {
            this.node = node;
            this.queue = new LinkedList<Integer>();
            queue.add(node);
            this.mark = new boolean[V()];
            mark[node] = true;
        }

        @Override
        public boolean hasNext() {
            return !queue.isEmpty();
        }

        @Override
        public Integer next() {
            int result;
            do {
                result = queue.remove();
                Set<Integer> neighbors = getNeighbors(result);
                if (!neighbors.isEmpty()) {
                    for (int i : neighbors) {
                        if (!mark[i]) {
                            queue.add(i);
                            mark[i] = true;
                        }
                    }
                }
            } while (result == node);
            return result;
        }
    }
}
