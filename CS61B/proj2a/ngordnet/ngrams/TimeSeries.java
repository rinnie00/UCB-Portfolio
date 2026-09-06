package ngordnet.ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    private static final int MIN_YEAR = 1400;
    private static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int i = startYear; i <= endYear; i++) {
            if (ts.containsKey(i)) {
                this.put(i, ts.get(i));
            }
        }

    }

    /**
     * Returns all years for this TimeSeries (in any order).
     */
    public List<Integer> years() {
        ArrayList<Integer> l = new ArrayList<>();
        for (int e : keySet()) {
            l.add(e);
        }
        return l;
    }

    /**
     * Returns all data for this TimeSeries (in any order).
     * Must be in the same order as years().
     */
    public List<Double> data() {
        ArrayList<Double> l = new ArrayList<>();
        for (int e : keySet()) {
            l.add(this.get(e));
        }
        return l;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     * <p>
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        // combine the years of this timeseries and ts timerseies
        // check if each year is in this timeseries and ts timeseires
        // and add the two data if both of them have it and if only one have it
        // return the data that only has it and if both dont have it return empty

        TimeSeries plusTs = new TimeSeries();
        List<Integer> years1 = this.years();
        List<Integer> years2 = ts.years();
        ArrayList<Integer> allYears = new ArrayList<>();

        allYears.addAll(years1);
        allYears.addAll(years2);

        if (allYears.isEmpty()) {
            return plusTs;
        }

        for (Integer year : allYears) {
            // from now on during the first loop, year is 1991
            // check if the year is in TS
            double countSum = 0;
            if (this.containsKey(year)) {
                countSum = countSum + this.get(year);
            }
            if (ts.containsKey(year)) {
                countSum = countSum + ts.get(year);
            }
            plusTs.put(year, countSum);
        }
        // from now on, plusTs is what we awnta to return
        return plusTs;

    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     * <p>
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {

        TimeSeries divideTs = new TimeSeries();

        List<Integer> years1 = this.years();

        for (Integer year : years1) {
            // years in both this timeserise and TS divide them
            if (ts.containsKey(year)) {
                // when both has the year

                double divideRatio = this.get(year) / ts.get(year);
                divideTs.put(year, divideRatio);
            } else {
                // throw exception error
                throw new IllegalArgumentException("TS doesn't have the year");
            }
        }
        return divideTs;
    }
}
