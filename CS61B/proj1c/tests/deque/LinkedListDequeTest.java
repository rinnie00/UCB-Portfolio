package deque;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class LinkedListDequeTest {

    @Test
    void testEquals() {
        LinkedListDeque <Double> tom = new LinkedListDeque<>();
        LinkedListDeque <Double> jerry = new LinkedListDeque<>();
        tom.addFirst(10.0);
        tom.addFirst(55.0);
        tom.addFirst(10.0);
        tom.addFirst(55.0);
        tom.removeLast();
        jerry.addFirst(10.0);
        jerry.addFirst(55.0);
        jerry.removeFirst();
        assertEquals(tom.toString(), jerry.toString());
    }
    @Test
    void testToString() {
        LinkedListDeque <Double> jerry = new LinkedListDeque<>();
        jerry.addFirst(10.0);
        jerry.addFirst(55.0);
        assertEquals(jerry.toString(),"[55.0, 10.0]");
    }
    @Test
    void iterator() {
        Deque<String> cheese = new LinkedListDeque<>();
        Iterator cheeseiterator = cheese.iterator();
        cheese.addLast("front");
        cheese.addLast("middle");
        cheese.addLast("back");
        assertEquals("front", cheeseiterator.next());
        assertEquals("middle", cheeseiterator.next());
    }
}