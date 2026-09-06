import jh61b.utils.Reflection;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
// import java.util.ArrayDeque;  <---  No no
import java.util.Collections;
import java.util.List;


import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    public void customTester() {
        ArrayDeque<Integer> sam = new ArrayDeque<>();
        assertWithMessage("actual is not expected")
                .that(sam.size())
                .isEqualTo(0);
        assertWithMessage("actual is not expected")
                .that(sam.isEmpty())
                .isTrue();
        sam.addFirst(15);
        assertWithMessage("actual is not expected")
                .that(sam.size())
                .isEqualTo(1);
        assertWithMessage("actual is not expected")
                .that(sam.isEmpty())
                .isFalse();
        assertThat(sam.toList())
                .containsExactly(15)
                .inOrder();
        sam.addFirst(30);
        assertThat(sam.toList())
                .containsExactly(30,15)
                .inOrder();
        sam.addFirst(45);
        assertThat(Collections.unmodifiableList(sam.toList()))
                .containsExactly(45,30,15)
                .inOrder();
        sam.addFirst(60);
        assertThat(sam.toList())
                .containsExactly(60,45,30,15)
                .inOrder();
        sam.addLast(12);
        assertThat(sam.toList())
                .containsExactly(60,45,30,15,12)
                .inOrder();
        sam.removeFirst();
        assertThat(sam.toList())
                .containsExactly(45,30,15,12)
                .inOrder();
        sam.addFirst(45);
        sam.addFirst(33);
        sam.addFirst(15);
        sam.addLast(45);
        sam.addLast(30);
        sam.addLast(11);
        sam.addFirst(45);
        sam.addFirst(33);
        sam.addFirst(15);
        sam.addLast(45);
        sam.addLast(30);
        sam.addLast(11);
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        sam.removeFirst();
        assertThat(sam.isEmpty())
                .isTrue();
        assertThat(sam.size())
                .isEqualTo(0);
        sam.addFirst(45);
        sam.addFirst(45);
        sam.addFirst(33);
        assertWithMessage("actual is not expected")
                .that(sam.get(1))
                .isEqualTo(45);
        assertWithMessage("actual is not expected")
                .that(sam.get(-1))
                .isEqualTo(null);
        assertWithMessage("actual is not expected")
                .that(sam.get(3))
                .isEqualTo(33);
        sam.addLast(11);
        sam.addFirst(33);
        sam.addFirst(15);
        sam.addFirst(15);
        sam.addLast(45);
        sam.addLast(30);
        sam.addLast(45);
        sam.addLast(30);
        sam.addLast(11);
        sam.addLast(11);
        sam.addFirst(33);
        sam.addFirst(15);
        sam.addFirst(15);
        sam.addLast(45);
        assertThat(sam.size())
                .isEqualTo(17);
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        sam.removeLast();
        assertThat(sam.isEmpty())
                .isTrue();
        assertThat(sam.size())
                .isEqualTo(0);

        sam.addLast(10);
        assertThat(sam.toList())
                .containsExactly(10)
                .inOrder();
        sam.addLast(30);
        assertThat(sam.toList())
                .containsExactly(10,30)
                .inOrder();
        sam.removeFirst();
        sam.removeFirst();
        assertThat(sam.toList())
                .containsExactly()
                .inOrder();

        sam.addLast(1);
        sam.addLast(2);
        assertThat(sam.toList())
                .containsExactly(1, 2)
                .inOrder();
        sam.removeLast();
        assertThat(sam.toList())
                .containsExactly(1)
                .inOrder();


    }

}

