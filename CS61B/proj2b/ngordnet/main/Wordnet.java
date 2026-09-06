package ngordnet.main;

import edu.princeton.cs.algs4.In;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.*;

public class Wordnet {
    private class Synset {

        String[] words;

        public Synset(String synset) {
            this.words = synset.split(" ");
        }

        public boolean contains(String word) {
            for (String wrd : words) {
                if (wrd.equals(word)) {
                    return true;
                }
            }
            return false;
        }

        public Set<String> toSet() {
            Set<String> result = new HashSet<>();
            for (String word : words) {
                result.add(word);
            }
            return result;
        }
    }

    private Graph wordnet;
    private Map<Integer, Synset> map;
    //    private Map<String, Double> popularity;

    public Wordnet(String synsetFile, String hyponymFile) {
        //this.wordnet = new MatrixGraph();
        this.wordnet = new ListGraph();
        this.map = new HashMap<>();
        //        this.popularity = new HashMap<>();
        In in = new In(synsetFile);
        String[] line;
        String synset;
        Synset sys;
        while (in.hasNextLine()) {
            line = in.readLine().split(",");
            if (line.length != 0) {
                synset = line[1];
                sys = new Synset(synset);
                map.put(Integer.parseInt(line[0]), sys);
                wordnet.addNode();
            }
        }
        in = new In(hyponymFile);
        int from;
        int to;
        while (in.hasNextLine()) {
            line = in.readLine().split(",");
            if (line.length != 0) {
                from = Integer.parseInt(line[0]);
                for (int i = 1; i < line.length; i++) {
                    to = Integer.parseInt(line[i]);
                    wordnet.addEdge(from, to);
                }
            }
        }
    }

    public String hyponyms(List<String> words, int startYear, int endYear, int k, NGramMap ngm) {
        if (words.size() != 0) {
            Set<Integer> nodes = findNode(words.get(0));
            Set<String> originalHyponymsJoint = findHyponyms(nodes);
            Set<String> originalHyponyms;
            for (int i = 1; i < words.size(); i++) {
                nodes = findNode(words.get(i));
                originalHyponyms = findHyponyms(nodes);
                originalHyponymsJoint.retainAll(originalHyponyms);
            }
            String result = arrange(originalHyponymsJoint, startYear, endYear, k, ngm);
            return result;
        }
        return "[]";
    }

    private Set<Integer> findNode(String words) {
        Set<Integer> nodes = new HashSet<>();
        for (int i = 0; i < wordnet.V(); i++) {
            if (map.get(i).contains(words)) {
                nodes.add(i);
            }
        }
        return nodes;
    }

    private Set<String> findHyponyms(Set<Integer> nodes) {
        Set<String> hyponyms = new HashSet<>();
        for (int i : nodes) {
            Iterator<Integer> iter = wordnet.iteratorFromin(i);
            while (iter.hasNext()) {
                int j = iter.next();
                Set<String> words = map.get(j).toSet();
                for (String word : words) {
                    hyponyms.add(word);
                }
            }
        }
        return hyponyms;
    }

    private String arrange(Set<String> hyponyms, int startYear, int endYear, int k, NGramMap ngm) {
        if (k != 0) {
            hyponyms = rearrange(hyponyms, startYear, endYear, k, ngm);
        }
        List<String> hyponymsList = new ArrayList<>(hyponyms);
        Collections.sort(hyponymsList);
        StringBuffer response = new StringBuffer();
        response.append("[");
        for (String word : hyponymsList) {
            response.append(word);
            response.append(", ");
        }
        if (hyponymsList.size() > 0) {
            response.deleteCharAt(response.length() - 1);
            response.deleteCharAt(response.length() - 1);
        }
        response.append("]");
        return response.toString();
    }

    private Set<String> rearrange(Set<String> hyponyms, int startYear, int endYear, int k, NGramMap ngm) {
        PriorityQueue<String> wordQueue = new PriorityQueue<>(new PopulrityComparator(startYear, endYear, ngm));
        for (String word : hyponyms) {
            if (getPopularity(word, startYear, endYear, ngm) != 0) {
                wordQueue.add(word);
            }
        }
        Set<String> sortedHyponym = new HashSet<>();
        int size = wordQueue.size();
        for (int i = 0; i < Math.min(k, size); i++) {
            sortedHyponym.add(wordQueue.remove());
        }
        return sortedHyponym;
    }

    private static double getPopularity(String o, int startYear, int endYear, NGramMap ngm) {
        TimeSeries timeSeries;
        timeSeries = ngm.countHistory(o, startYear, endYear);
        double popularity = 0.;
        for (int year : timeSeries.keySet()) {
            popularity += timeSeries.get(year);
        }
        return popularity;
    }

    public static class PopulrityComparator implements Comparator<String> {
        Map<String, Double> popularity;
        int startYear;
        int endYear;
        NGramMap ngm;

        public PopulrityComparator(int startYear, int endYear, NGramMap ngm) {
            this.popularity = new HashMap<>();
            this.startYear = startYear;
            this.endYear = endYear;
            this.ngm = ngm;
        }

        @Override
        public int compare(String o1, String o2) {
            double cmp;
            double o1Popularity;
            double o2Popularity;
            if (!popularity.containsKey(o1)) {
                o1Popularity = getPopularity(o1, startYear, endYear, ngm);
                popularity.put(o1, o1Popularity);
            } else {
                o1Popularity = popularity.get(o1);
            }
            if (!popularity.containsKey(o2)) {
                o2Popularity = getPopularity(o2, startYear, endYear, ngm);
                popularity.put(o2, o2Popularity);
            } else {
                o2Popularity = popularity.get(o2);
            }
            cmp = o1Popularity - o2Popularity;
            if (cmp > 0) {
                return -1;
            } else if (cmp < 0) {
                return 1;
            }
            return 0;
        }
    }
}
