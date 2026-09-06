package ngordnet.proj2b_testing;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests the most basic case for Hyponyms where the list of words is one word long, and k = 0.
 */
public class TestOneWordK0Hyponyms {
    // this case doesn't use the NGrams dataset at all, so the choice of files is irrelevant
    public static final String WORDS_FILE = "data/ngrams/top_14377_words.csv";
    public static final String TOTAL_COUNTS_FILE = "data/ngrams/total_counts.csv";
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    public static final String LARGE_SYNSET_FILE = "data/wordnet/synsets1000-subgraph.txt";
    public static final String LARGE_HYPONYM_FILE = "data/wordnet/hyponyms1000-subgraph.txt";
    public static final String FULL_SYNSET_FILE = "data/wordnet/synsets.txt";
    public static final String FULL_HYPONYM_FILE = "data/wordnet/hyponyms.txt";

    @Test
    public void testActK0() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, FULL_SYNSET_FILE, FULL_HYPONYM_FILE);
        List<String> words = List.of("act");

        NgordnetQuery nq = new NgordnetQuery(words, 0, 0, 0);
        String actual = studentHandler.handle(nq);
        String expected = "[act, action, change, demotion, human_action, human_activity, variation]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void test1000K5() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
                WORDS_FILE, TOTAL_COUNTS_FILE, LARGE_SYNSET_FILE, LARGE_HYPONYM_FILE);
        List<String> words = List.of("oil");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 5);
        String actual = studentHandler.handle(nq);
        String expected = "[crude, grease, oil, petroleum]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("petroleum");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[crude, oil, petroleum]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("grease", "oil");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[grease]";
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testFullKn() {
        NgordnetQueryHandler studentHandler = AutograderBuddy.getHyponymHandler(
            WORDS_FILE, TOTAL_COUNTS_FILE, FULL_SYNSET_FILE, FULL_HYPONYM_FILE);
        List<String> words = List.of("oil");

        NgordnetQuery nq = new NgordnetQuery(words, 2000, 2020, 5);
        String actual = studentHandler.handle(nq);
        String expected = "[crude, grease, oil, petroleum]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("petroleum");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[crude, oil, petroleum]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("grease", "oil");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[grease]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("grease");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[dirt, grease, soil, stain]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("fuck");
        nq = new NgordnetQuery(words, 1900, 2020, 5);
        actual = studentHandler.handle(nq);
        expected = "[ass, fuck, fucking, screw]";
        assertThat(actual).isEqualTo(expected);

        words = List.of("fuck");
        nq = new NgordnetQuery(words, 1400, 1500, 5);
        actual = studentHandler.handle(nq);
        expected = "[ass, screw]";
        assertThat(actual).isEqualTo(expected);
    }
}
