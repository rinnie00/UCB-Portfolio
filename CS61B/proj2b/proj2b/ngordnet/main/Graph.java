package ngordnet.main;
import java.util.Iterator;
import java.util.Set;

public interface Graph {
    public void addEdge(int from, int to);

    public int V();

    public int E();

    public default boolean isConnect(int from, int to) {
        return false;
    }

    void addNode();

    Set<Integer> getNodes();

    Set<Integer> getNeighbors(int node);

    // return an iterator from the @node and with the node exclusive.
    Iterator<Integer> iteratorFromex(int node);

    // return an iterator from the @node and with the node inclusive.
    Iterator<Integer> iteratorFromin(int node);

}
