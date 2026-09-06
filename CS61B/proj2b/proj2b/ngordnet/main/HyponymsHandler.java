package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

import java.util.List;

public class HyponymsHandler extends NgordnetQueryHandler {
    private Wordnet wordnet;
    private NGramMap nGramMap;

    public HyponymsHandler(Wordnet wordnet, NGramMap nGramMap) {
        super();
        this.wordnet = wordnet;
        this.nGramMap = nGramMap;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        String result = wordnet.hyponyms(words, startYear, endYear, k, nGramMap);
        return result;
    }
}
