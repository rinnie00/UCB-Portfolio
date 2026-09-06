package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> implements Deque<T> {
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LinkedListDeque otherLLD) {
            if (this.size() != otherLLD.size()) {
                return false;
            }
            Iterator<T> itr = this.iterator();
            Iterator<T> otherItr = otherLLD.iterator();
            while (itr.hasNext()) {
                T t = itr.next();
                T otherT = otherItr.next();
                if (!t.equals(otherT)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private int wizPos;

        public LinkedListDequeIterator() {
            wizPos = 0;
        }

        public boolean hasNext() {
            return wizPos < size;
        }
        public T next() {
            T returnItem = get(wizPos);
            wizPos += 1;
            return returnItem;
        }
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class Node {
        private Node next;
        private Node prev;
        private T element;
        // NODE CONSTRUCTOR
        public Node(T element) {
            prev = this;
            next = this;
            this.element = element;
        }
        public Node getNext() {

            return next;
        }
        public void setNext(Node next) {
            this.next = next;
        }
        public Node getPrev() {
            return prev;
        }
        public void setPrev(Node prev) {
            this.prev = prev;
        }
        public T getElement() {
            return element;
        }
        public void setElement(T element) {
            this.element = element;
        }
    }
    // LINKED LIST DEQUE INSTANCE VARIABLES (FIELDS)
    private Node sentinel;
    private int size;
    // LINKED LIST DEQUE CONSTRUCTOR (which is special!)
    public LinkedListDeque() {
        sentinel = new Node(null);
        size = 0;
    }
    @Override
    public void addFirst(T x) {
        addLast(x);
        sentinel = sentinel.prev;
    }
    @Override
    public void addLast(T x) {
        if (isEmpty()) {
            sentinel.element = x;
            size = 1;
        }
        else {
            sentinel.getPrev().setNext(new Node(x));
            sentinel.getPrev().getNext().setPrev(sentinel.getPrev());
            sentinel.setPrev(sentinel.getPrev().getNext());
            sentinel.getPrev().setNext(sentinel);
            size++;
        }
    }
    @Override
    public List<T> toList() {
        if (isEmpty()) {
            return null;
        }
        List<T> toListAl = new ArrayList<>();

        Node p = sentinel;
        while (p.getNext() != sentinel) {
            toListAl.add(p.element);
            p = p.getNext();
        }
        toListAl.add(p.element);

        return toListAl;
    }
    @Override
    public boolean isEmpty() {
        if (size == 0) {
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
        sentinel = sentinel.getNext();
        return removeLast();
    }
    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (size == 1) {
            T returnElement = sentinel.element;
            sentinel.element = null;
            size = 0;
            return returnElement;
        }
        T returnElement = sentinel.getPrev().element;
        sentinel.getPrev().getPrev().setNext(sentinel);
        sentinel.setPrev(sentinel.getPrev().getPrev());
        size--;
        return returnElement;
    }
    @Override
    public T get(int index) {
        if (!isEmpty() && index >= size || index < 0) {
            return null;
        }
        Node p = sentinel;
        for (int i = 0; i < index; i++) {
            p = p.getNext();
        }
        return p.element;
    }
    @Override
    public T getRecursive(int index) {
        if (!isEmpty() && index >= size || index < 0) {
            return null;
        }
        T returnElement = sentinel.element;
        if (index != 0) {
            sentinel = sentinel.getNext();
            returnElement = getRecursive(index - 1);
            sentinel = sentinel.getPrev();
        }
        return returnElement;
    }
}


