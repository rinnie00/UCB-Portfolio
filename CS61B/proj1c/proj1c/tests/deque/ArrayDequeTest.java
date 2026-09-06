package deque;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class ArrayDequeTest {

    @Test
    void testEquals() {
        ArrayDeque <Double> tom = new ArrayDeque<>();
        ArrayDeque <Double> jerry = new ArrayDeque<>();
        tom.addFirst(10.0);
        tom.addFirst(55.0);
        jerry.addFirst(10.0);
        jerry.addFirst(55.0);
        System.out.println(tom.equals(jerry));
    }

    @Test
    void testToString() {
        ArrayDeque <Double> tom = new ArrayDeque<>();
        tom.addFirst(10.0);
        tom.addFirst(55.0);
        assertEquals(tom.toString(),"[55.0, 10.0]");
    }

    @Test
    void iterator() {
        Deque<String> cheese = new ArrayDeque<>();
        Iterator cheeseiterator = cheese.iterator();
        cheese.addLast("front");
        cheese.addLast("middle");
        cheese.addLast("back");
        assertEquals("front", cheeseiterator.next());
        assertEquals("middle", cheeseiterator.next());
        }
    }