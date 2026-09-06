package ngordnet.main;

import ngordnet.browser.NgordnetQuery;
import ngordnet.browser.NgordnetQueryHandler;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;
import ngordnet.ngrams.NGramMap;

import java.util.ArrayList;


public class HistoryHandler extends NgordnetQueryHandler {
    NGramMap ngm;

    public HistoryHandler(NGramMap map) {
        ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (String word : q.words()) {
            TimeSeries ts = ngm.weightHistory(word, q.startYear(), q.endYear());

            labels.add(word);
            lts.add(ts);
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
