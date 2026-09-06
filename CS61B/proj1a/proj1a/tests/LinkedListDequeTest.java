import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
//import LinkedListDeque;
import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

     @Test
     @DisplayName("LinkedListDeque has no fields besides nodes and primitives")
     void noNonTrivialFields() {
         Class<?> nodeClass = NodeChecker.getNodeClass(LinkedListDeque.class, true);
         List<Field> badFields = Reflection.getFields(LinkedListDeque.class)
                 .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(nodeClass) || f.isSynthetic()))
                 .toList();

         assertWithMessage("Found fields that are not nodes or primitives").that(badFields).isEmpty();
     }

     @Test
     /** In this test, we have three different assert statements that verify that addFirst works correctly. */
     public void addFirstTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addFirst("back"); // after this call we expect: ["back"]
         assertThat(lld1.toList()).containsExactly("back").inOrder();

         lld1.addFirst("middle"); // after this call we expect: ["middle", "back"]
         assertThat(lld1.toList()).containsExactly("middle", "back").inOrder();

         lld1.addFirst("front"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();

         /* Note: The first two assertThat statements aren't really necessary. For example, it's hard
            to imagine a bug in your code that would lead to ["front"] and ["front", "middle"] failing,
            but not ["front", "middle", "back"].
          */
     }

     @Test
     /** In this test, we use only one assertThat statement. IMO this test is just as good as addFirstTestBasic.
      *  In other words, the tedious work of adding the extra assertThat statements isn't worth it. */
     public void addLastTestBasic() {
         Deque<String> lld1 = new LinkedListDeque<>();

         lld1.addLast("front"); // after this call we expect: ["front"]
         lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
         lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
         assertThat(lld1.toList()).containsExactly("front", "middle", "back").inOrder();
     }

     @Test
     /** This test performs interspersed addFirst and addLast calls. */
     public void addFirstAndAddLastTest() {
         Deque<Integer> lld1 = new LinkedListDeque<>();

         /* I've decided to add in comments the state after each call for the convenience of the
            person reading this test. Some programmers might consider this excessively verbose. */
         lld1.addLast(0);   // [0]
         lld1.addLast(1);   // [0, 1]
         lld1.addFirst(-1); // [-1, 0, 1]
         lld1.addLast(2);   // [-1, 0, 1, 2]
         lld1.addFirst(-2); // [-2, -1, 0, 1, 2]

         assertThat(lld1.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
     }

     @Test
    public void customTester() {

        LinkedListDeque<Integer> ddl2 = new LinkedListDeque<>();

        // ASSERTIONS #1
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(0);
         assertWithMessage("actual is not expected")
                 .that(ddl2.isEmpty())
                 .isTrue();
        assertThat(ddl2.toList())
                .containsExactly(null)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(0))
                .isEqualTo(null);

        ddl2.addFirst(36);
        ddl2.addLast(11);
        ddl2.addFirst(54);
        ddl2.addLast(1005);
        ddl2.addLast(99);
        ddl2.addFirst(1);

        // ASSERTIONS #2
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(6);
        assertThat(ddl2.toList())
                .containsExactly(1, 54, 36, 11, 1005, 99)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(1))
                .isEqualTo(54);

         ddl2.removeFirst();
         ddl2.removeFirst();
         ddl2.removeFirst();
         ddl2.removeFirst();
         ddl2.removeFirst();
         ddl2.removeFirst();

         // ASSERTIONS #3
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(0);
         assertWithMessage("actual is not expected")
                 .that(ddl2.isEmpty())
                 .isTrue();
         assertThat(ddl2.toList())
                 .containsExactly(null)
                 .inOrder();
         assertWithMessage("actual is not expected")
                 .that(ddl2.getRecursive(0))
                 .isEqualTo(null);

         ddl2.addFirst(36);
         ddl2.addLast(11);
         ddl2.addFirst(54);
         ddl2.addLast(1005);
         ddl2.addLast(99);
         ddl2.addFirst(1);

         ddl2.removeLast();

        // ASSERTIONS #4
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(5);
        assertThat(ddl2.toList())
                .containsExactly(1, 54, 36, 11, 1005)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.get(3))
                .isEqualTo(11);
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(3))
                .isEqualTo(11);

         ddl2.removeLast();
         ddl2.removeLast();
         ddl2.removeLast();
         ddl2.removeLast();
         ddl2.removeLast();

         // ASSERTIONS #5
         assertWithMessage("actual is not expected")
                 .that(ddl2.isEmpty())
                 .isTrue();
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(0);
        assertThat(ddl2.toList())
                .containsExactly(null)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(0))
                .isEqualTo(null);

        ddl2.addFirst(36);
        ddl2.removeFirst();
        ddl2.addFirst(36);
        ddl2.removeLast();
        ddl2.addLast(36);
        ddl2.removeFirst();
        ddl2.addLast(36);
        ddl2.removeLast();
        ddl2.addFirst(36);
        ddl2.addLast(11);
        ddl2.addLast(1005);

        // ASSERTIONS #6
        assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(3);
        assertThat(ddl2.toList())
                .containsExactly(36, 11, 1005)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.get(2))
                .isEqualTo(1005);
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(2))
                .isEqualTo(1005);

        ddl2.removeLast();

        // ASSERTIONS #7
        assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(2);
        assertThat(ddl2.toList())
                .containsExactly(36, 11)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(1))
                .isEqualTo(11);

        ddl2.removeFirst();
        ddl2.removeFirst();

        // ASSERTIONS #8
         assertWithMessage("actual is not expected")
                 .that(ddl2.isEmpty())
                 .isTrue();
         assertWithMessage("actual is not expected")
                 .that(ddl2.size())
                 .isEqualTo(0);
        assertThat(ddl2.toList())
                .containsExactly(null)
                .inOrder();
        assertWithMessage("actual is not expected")
                .that(ddl2.getRecursive(0))
                .isEqualTo(null);

     }

}