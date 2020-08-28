package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class AuthorshipDetectorImpl implements AuthorshipDetector {
    public static final int MAX_FEATURE_TYPES = 5;
    public static final int MAX_STRING_BUILDER_CAPACITY = 5000;
    public static final String REGEX = "[!.?]+";
    public static final String LONG_THING =
            "^[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]+|[!.,:;\\-?<>#\\*\'\"\\[\\(\\]\\)\\n\\t\\\\]+$";
    private ArrayList<CustomPair> signaturesData;
    private double[] weights;

    public AuthorshipDetectorImpl(InputStream signaturesDataset, double[] weights) {
        if (signaturesDataset == null) {
            throw new IllegalArgumentException();
        }
        signaturesData = new ArrayList<>();
        this.weights = weights;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(signaturesDataset))) {
            String line;
            FeatureType[] featureTypes = FeatureType.values();
            while ((line = br.readLine()) != null) {
                HashMap<FeatureType, Double> features = new HashMap<>();
                String[] splitString = line.split(", ");
                for (int i = 0; i < MAX_FEATURE_TYPES; ++i) {
                    features.put(featureTypes[i], Double.valueOf(splitString[i + 1]));
                }
                signaturesData.add(new CustomPair(splitString[0],
                        new LinguisticSignature(features)));
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public LinguisticSignature calculateSignature(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }

        HashMap<FeatureType, Double> features = new HashMap<>();
        InputStreamReader input = new InputStreamReader(mysteryText);
        try (BufferedReader br = new BufferedReader(input)) {
            String line;
            StringBuilder streamString = new StringBuilder(MAX_STRING_BUILDER_CAPACITY);
            ArrayList<String> sentences = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                streamString.append(line);
                streamString.append(" ");
                if (line.contains("?") || line.contains("!") || line.contains(".")) {
                    String str = streamString.toString();
                    boolean check = false;
                    if (!str.endsWith("!") && !str.endsWith(".") && !str.endsWith("?")) {
                        check = true;
                    }
                    String[] tmp = str.split(REGEX);
                    int len = tmp.length;
                    for (int i = 0; i < tmp.length; ++i) {
                        if (check && i == len - 1) {
                            break;
                        }
                        if (!tmp[i].equals(" ") && !tmp[i].equals("")) {
                            sentences.add(tmp[i].trim());
                        }
                    }
                    streamString = new StringBuilder(MAX_STRING_BUILDER_CAPACITY);
                    if (check) {
                        streamString.append(tmp[len - 1]);
                        streamString.append(" ");
                    }
                }
            }
            sentences.add(streamString.toString());
            int lettersCounter = 0;
            int wordsCounter = 0;
            HashSet<String> wordsSet = new HashSet<>(); //Will store all different words
            HashSet<String> uniqueWords = new HashSet<>(); //Will store only the unique words
            for (String a : sentences) {
                String[] words = a.trim().split("\\s+");
                for (String b : words) {
                    String c = cleanUp(b);
                    if (!c.equals("")) {
                        wordsSet.add(c);
                        if (!uniqueWords.add(c)) { //If the word is already in the set
                            uniqueWords.remove(c); //Remove it
                        }
                        lettersCounter += c.length();
                        ++wordsCounter;
                    }
                }
            }
            Double averageWordLength = ((double) lettersCounter) / wordsCounter;
            features.put(FeatureType.AVERAGE_WORD_LENGTH, averageWordLength);
            Double typeTokenRatio = ((double) wordsSet.size()) / wordsCounter;
            features.put(FeatureType.TYPE_TOKEN_RATIO, typeTokenRatio);
            Double hapaxLegomenaRatio = ((double) uniqueWords.size()) / wordsCounter;
            features.put(FeatureType.HAPAX_LEGOMENA_RATIO, hapaxLegomenaRatio);
            Double averageWordsInSentence = ((double) wordsCounter) / sentences.size();
            features.put(FeatureType.AVERAGE_SENTENCE_LENGTH, averageWordsInSentence);
            features.put(FeatureType.AVERAGE_SENTENCE_COMPLEXITY, 0.0);
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return new LinguisticSignature(features);
    }

    public static String cleanUp(String word) {
        return word.toLowerCase()
                .replaceAll(LONG_THING, "");
    }


    @Override
    public double calculateSimilarity(LinguisticSignature firstSignature,
                                      LinguisticSignature secondSignature) {
        if (firstSignature == null || secondSignature == null) {
            throw new IllegalArgumentException();
        }

        Map<FeatureType, Double> features1 = firstSignature.getFeatures();
        Map<FeatureType, Double> features2 = secondSignature.getFeatures();

        FeatureType[] featureTypes = FeatureType.values();
        double sum = 0;
        for (int i = 0; i < MAX_FEATURE_TYPES; ++i) {
            double feature1 = features1.get(featureTypes[i]);
            double feature2 = features2.get(featureTypes[i]);
            sum += Math.abs(feature1 - feature2) * weights[i];
        }
        return sum;
    }

    @Override
    public String findAuthor(InputStream mysteryText) {
        if (mysteryText == null) {
            throw new IllegalArgumentException();
        }
        LinguisticSignature mysterySignature = calculateSignature(mysteryText);
        Iterator itr = signaturesData.iterator();
        CustomPair cp = (CustomPair) itr.next();
        LinguisticSignature ls = cp.getSignature();

        int num = 0;
        double bestMatch = calculateSimilarity(mysterySignature, ls);
        int counter = 0;
        while (itr.hasNext()) {
            ++counter;
            cp = (CustomPair) itr.next();
            ls = cp.getSignature();
            double matchNum = calculateSimilarity(mysterySignature, ls);
            if (matchNum < bestMatch) {
                bestMatch = matchNum;
                num = counter;
            }
        }
        return signaturesData.get(num).getAuthorName();
    }
}
