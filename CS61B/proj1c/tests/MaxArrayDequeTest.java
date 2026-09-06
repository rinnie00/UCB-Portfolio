import deque.MaxArrayDeque;
import org.junit.jupiter.api.*;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class MaxArrayDequeTest {
    @Test
    public void checkMax (){
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(String::compareTo);
        mad.addFirst("z");
        mad.addFirst("b");
        assertThat(mad.max()).isEqualTo("z");
        assertWithMessage("Max is actually")
                .that(mad.max())
                .isEqualTo("z");
    }



    }