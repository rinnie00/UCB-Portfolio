import java.util.ArrayList;
import java.util.List;
//import java.lang.IndexOutOfBoundsException;

public class LinkedListDeque<T> implements Deque<T> {

    // NESTED PRIVATE NODE CLASS
    private class Node {

        // NODE INSTANCE VARIABLES (FIELDS)
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
        if(isEmpty()) {
            return null;
        }
        List<T> toList_AL = new ArrayList<>();

        Node p = sentinel;
        while (p.getNext() != sentinel) {
            toList_AL.add(p.element);
            p = p.getNext();
        }
        toList_AL.add(p.element);

        return toList_AL;
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
    public T removeFirst(){
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
        // Will this help allow garbage collection?
//        sentinel.getPrev().getNext() = null;
        sentinel.getPrev().getPrev().setNext(sentinel);
        // Will this help allow garbage collection?
//        sentinel.getNext().getNext() = null;
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
        for (int i = 0; i < index; i++){
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
        if (index != 0){
            sentinel = sentinel.getNext();
            returnElement = getRecursive(index - 1);
            sentinel = sentinel.getPrev();
        }

        return returnElement;
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> ddl1 = new LinkedListDeque<>();
        LinkedListDeque<Integer> ddl2 = new LinkedListDeque<>();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.getRecursive(0));
        ddl2.addFirst(36);
        ddl2.addLast(11);
        ddl2.addFirst(54);
        ddl2.addLast(1005);
        ddl2.addLast(99);
        ddl2.addFirst(1);
        System.out.println(ddl2.toList());
        System.out.println(ddl2.getRecursive(1));
        ddl2.removeFirst();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.get(3));
        System.out.println(ddl2.getRecursive(3));
        ddl2.removeFirst();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.get(0));
        System.out.println(ddl2.getRecursive(0));
        ddl2.removeLast();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.get(2));
        System.out.println(ddl2.getRecursive(2));
        ddl2.removeLast();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.getRecursive(1));
        ddl2.removeFirst();
        ddl2.removeFirst();
        System.out.println(ddl2.toList());
        System.out.println(ddl2.getRecursive(0));

    }

}
