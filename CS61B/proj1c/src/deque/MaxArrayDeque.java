package deque;

import java.util.Comparator;
import java.util.Iterator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    Comparator<T> c;
    public MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }
    public T max() {
       return max(c);
    }
    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;
        }
        Iterator<T> itr = iterator();
        T jerry = itr.next();
        while (itr.hasNext()) {
            T tom = itr.next();
            if (c.compare(jerry, tom) < 0) {
                jerry = tom;
            }
        }
        return jerry;
    }

}

