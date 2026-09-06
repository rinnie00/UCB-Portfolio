import java.util.ArrayList;
import java.util.List;

public class ArrayDeque<T> implements Deque<T> {

    private int size;
    private T[] items;
    private int nextFirst;
    private int nextLast;
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = (items.length / 2) - 1;
        nextLast = items.length / 2;

    }

    @Override
    public void addFirst(T x) {
        if (size >= items.length / 2) {
            resize((int) (items.length * 2));
        }
        items[nextFirst] = x;
        size++;
        nextFirst--;
    }

    @Override
    public void addLast(T x) {
        if (size >= items.length / 2) {
            resize((int) (items.length * 2));
        }
        items[nextLast] = x;
        size++;
        nextLast++;
    }

    private void resize(int capacity) {
        T[] tempArray = (T[]) new Object[capacity];
        int newNextFirst = (capacity / 4);

        System.arraycopy(items, nextFirst + 1, tempArray, newNextFirst + 1, size);
        items = tempArray;

        nextFirst = newNextFirst;
        nextLast = nextFirst + 1 + size;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        int indexIterator = nextFirst + 1;
        // Iterate until nextLast is reached, marking no more elements.
        while (indexIterator != nextLast) {
            // Add Iterating Index
            returnList.add(items[indexIterator]);
            // Increment -- end of array loops back to index 0.
            indexIterator++;
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T returnValue = items[nextFirst + 1];
        items[nextFirst + 1] = null;
        nextFirst++;
        size--;
        double usageRatio = 0;
        if (size > 1) {
            usageRatio = ((double) size / (double) items.length);
            if (usageRatio < 0.25) {
                resize((int) (items.length / 2));
            }
        }
        return returnValue;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T returnValue = items[nextLast - 1];
        items[nextLast - 1] = null;
        nextLast--;
        size--;
        double usageRatio = 0;
        if (size > 1) {
            usageRatio = ((double) size / (double) items.length);
            if (usageRatio < 0.25) {
                resize((int) (items.length / 2));
            }
        }
        return returnValue;
    }

    @Override
    public T get(int index) {
        if (isEmpty() || index < 0) {
            return null;
        }
        int realIndex = index % size + nextFirst + 1;
        return items[realIndex];
    }

    public int getLength() {
        return items.length;
    }

}
