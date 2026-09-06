package ngordnet.main;
import java.util.*;
public class ListGraph implements Graph {
    private int v;
    private int e;
    private List<Integer>[] adjList = new ArrayList[ORIGINALSIZE];
    private static final int ORIGINALSIZE = 10;
    private static final double RESIZEFACTOR = 1.5;

    public ListGraph() {
        v = 0;
        e = 0;
    }

    @Override
    public void addEdge(int from, int to) {
        if (from < v && to < v) {
            if (adjList[from] == null) {
                adjList[from] = new ArrayList<>();
            }
            adjList[from].add(to);
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
        if (v + 1 == adjList.length) {
            resize((int) (adjList.length * RESIZEFACTOR));
        }
    }

    private void resize(int newSize) {
        List<Integer>[] newAdjList = new ArrayList[newSize];
        for (int i = 0; i < v; i++) {
            newAdjList[i] = adjList[i];
        }
        adjList = newAdjList;
    }

    @Override
    public Set<Integer> getNodes() {
        return null;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> result;
        if (adjList[node] != null) {
            result = new HashSet<>(adjList[node]);
        } else {
            result = new HashSet<>();
        }
        return result;
    }

    @Override
    public Iterator<Integer> iteratorFromex(int node) {
        return null;
    }

    @Override
    public Iterator<Integer> iteratorFromin(int node) {
        return new FrominIterator(node);
    }

    private class FrominIterator implements Iterator<Integer> {
        private Queue<Integer> queue;
        private boolean[] mark;

        public FrominIterator(int node) {
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
}
