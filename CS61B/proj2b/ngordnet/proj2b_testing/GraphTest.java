package ngordnet.proj2b_testing;

import ngordnet.main.Wordnet;
import org.junit.Test;

public class GraphTest {
    public static final String SMALL_SYNSET_FILE = "data/wordnet/synsets16.txt";
    public static final String SMALL_HYPONYM_FILE = "data/wordnet/hyponyms16.txt";
    @Test
    public void graphInputTest(){

    }
    @Test
    public void wordnetsimpleTest(){
//        Wordnet wn = new Wordnet(SMALL_SYNSET_FILE,SMALL_HYPONYM_FILE);
//        System.out.println(wn.hyponyms("act",0,0,0));
    }

    public static void main(String[] args) {
        boolean[][] test = new boolean[70000][70000];
    }
}
