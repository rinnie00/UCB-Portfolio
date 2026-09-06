package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 * <p>
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;
    HashMap<String, TimeSeries> wordMap;
    TimeSeries countMap;

    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {
        In wordsIn = new In(wordsFilename);
        In countsIn = new In(countsFilename);

        wordMap = new HashMap<>();
        while (!wordsIn.isEmpty()) {
            String[] oneLine = wordsIn.readLine().split("\\t");
            String word = oneLine[0];
            int year = Integer.parseInt(oneLine[1]);
            double count = Double.parseDouble(oneLine[2]);
            // when the word is the first
            if (!wordMap.containsKey(word)) {
                //start a new timeseries
                TimeSeries ts = new TimeSeries();
                ts.put(year, count);
                wordMap.put(word, ts);
            } else {
                wordMap.get(word).put(year, count);
            }
        }

        countMap = new TimeSeries();
        while (!countsIn.isEmpty()) {
            String[] oneLine = countsIn.readLine().split(",");
            int year = Integer.parseInt(oneLine[0]);
            double count = Double.parseDouble(oneLine[1]);
            countMap.put(year, count);
        }
    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    //
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        //wordcount in the history between startYear and endYear
        // when the word, Startyear and endyear is given return the value of that word occured
        // in btween two years
        // get the whole list of words? that are btween the years and then for loop through
        // if there is the word and then return corrosppoding value of timeseries
        // find the word between the years and make it in to timeseries
        TimeSeries ts = countHistory(word);

        if (ts.isEmpty()) {
            return new TimeSeries();
        }

        TimeSeries ts2 = new TimeSeries(ts, startYear, endYear);
        return ts2;
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy,
     * not a link to this NGramMap's TimeSeries. In other words, changes made
     * to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy".
     */
    public TimeSeries countHistory(String word) {
        //wordcount of in any one years
        // find the word in the wordmap and return the TimesSeries
        if (!wordMap.containsKey(word)) {
            return new TimeSeries();
        }
        return wordMap.get(word);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        //total count of all words
        // get the value of total word count from countfile
        return countMap;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        //specific year count / counthistory(have years)
        TimeSeries wh = weightHistory(word);
        TimeSeries wh2 = new TimeSeries(wh, startYear, endYear);
        return wh2;
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to
     * all words recorded in that year. If the word is not in the data files, return an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        //entire volume of specific word /  entire volume of all words
        //counthistory/totalcounthistory
        if (!wordMap.containsKey(word)) {
            TimeSeries empty = new TimeSeries();
            return empty;
        }
        return countHistory(word).dividedBy(totalCountHistory());

    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS
     * between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     * this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries sum = summedWeightHistory(words);
        TimeSeries sum2 = new TimeSeries(sum, startYear, endYear);
        return sum2;
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        //find the word in the weightistory
        TimeSeries summedTs = new TimeSeries();
        for (String word : words) {
            // add the corrospoding value together?
            summedTs = weightHistory(word).plus(summedTs);
        }

        return summedTs;
    }

    public TimeSeries get(String word) {
        return wordMap.get(word);
    }
}
