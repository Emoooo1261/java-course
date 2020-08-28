package bg.sofia.uni.fmi.mjt.authorship.detection;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class AuthorshipDetectorTest {

    private static final double[] WEIGHTS = {11.0, 33.0, 50.0, 0.4, 4.0};
    private static final double VALUE = 3.9;
    private static final double VALUE1 = 0.769231;
    private static final double VALUE2 = 0.53846;
    private static final double VALUE3 = 6.5;
    private static final double VALUE4 = 0.0;
    private static final double VALUE5 = 4.4;
    private static final double VALUE6 = 0.1;
    private static final double VALUE7 = 0.05;
    private static final double VALUE8 = 10.0;
    private static final double VALUE9 = 2.0;
    private static final double VALUE10 = 4.3;
    private static final double VALUE11 = 0.1;
    private static final double VALUE12 = 0.04;
    private static final double VALUE13 = 16.0;
    private static final double VALUE14 = 4.0;
    private static final double EXPECTED = 12.0;
    private static final double EXPECTED1 = 12.0;
    private static final double EXPECTED2 = 0.0;
    private static final double DELTA = 0.1;
    private static final String RESOURCES_KNOWN_SIGNATURES_TXT = "resources/knownSignatures.txt";
    private static final String RESOURCES_MYSTERY_FILES_1_TXT = "resources/mysteryFiles/1.txt";
    private static final String RESOURCES_MYSTERY_FILES_MYSTERY_4_TXT = "resources/mysteryFiles/mystery4.txt";
    private static final String CUSTOM_SIGNATURE = "Custom Signature";
    private static final String AGATHA_CHRISTIE = "Agatha Christie";
    private static final FeatureType AVERAGE_WORD_LENGTH = FeatureType.AVERAGE_WORD_LENGTH;
    private static final FeatureType TYPE_TOKEN_RATIO = FeatureType.TYPE_TOKEN_RATIO;
    private static final FeatureType HAPAX_LEGOMENA_RATIO = FeatureType.HAPAX_LEGOMENA_RATIO;
    private static final FeatureType AVERAGE_SENTENCE_LENGTH = FeatureType.AVERAGE_SENTENCE_LENGTH;
    private static final FeatureType AVERAGE_SENTENCE_COMPLEXITY = FeatureType.AVERAGE_SENTENCE_COMPLEXITY;

    @Test
    public void calculateSignatureTests() throws FileNotFoundException {
        InputStream isSignatures = new FileInputStream(RESOURCES_KNOWN_SIGNATURES_TXT);
        AuthorshipDetectorImpl adi = new AuthorshipDetectorImpl(isSignatures, WEIGHTS);

        InputStream isMysteryText = new FileInputStream(RESOURCES_MYSTERY_FILES_1_TXT);
        HashMap<FeatureType, Double> features = new HashMap<>();
        features.put(AVERAGE_WORD_LENGTH, VALUE);
        features.put(TYPE_TOKEN_RATIO, VALUE1);
        features.put(HAPAX_LEGOMENA_RATIO, VALUE2);
        features.put(AVERAGE_SENTENCE_LENGTH, VALUE3);
        features.put(AVERAGE_SENTENCE_COMPLEXITY, VALUE4);
        LinguisticSignature ls = new LinguisticSignature(features);

        LinguisticSignature calculated = adi.calculateSignature(isMysteryText);
        HashMap<FeatureType, Double> calculatedFeatures =
                (HashMap<FeatureType, Double>) calculated.getFeatures();

        assertEquals(features.get(AVERAGE_WORD_LENGTH),
                calculatedFeatures.get(AVERAGE_WORD_LENGTH), DELTA);
        assertEquals(features.get(TYPE_TOKEN_RATIO),
                calculatedFeatures.get(TYPE_TOKEN_RATIO), DELTA);
        assertEquals(features.get(HAPAX_LEGOMENA_RATIO),
                calculatedFeatures.get(HAPAX_LEGOMENA_RATIO), DELTA);
        assertEquals(features.get(AVERAGE_SENTENCE_LENGTH),
                calculatedFeatures.get(AVERAGE_SENTENCE_LENGTH), DELTA);
        assertEquals(features.get(AVERAGE_SENTENCE_COMPLEXITY),
                calculatedFeatures.get(AVERAGE_SENTENCE_COMPLEXITY), DELTA);
    }

    @Test
    public void calculateSimilarityTests() throws FileNotFoundException {
        HashMap<FeatureType, Double> features1 = new HashMap<>();
        features1.put(AVERAGE_WORD_LENGTH, VALUE5);
        features1.put(TYPE_TOKEN_RATIO, VALUE6);
        features1.put(HAPAX_LEGOMENA_RATIO, VALUE7);
        features1.put(AVERAGE_SENTENCE_LENGTH, VALUE8);
        features1.put(AVERAGE_SENTENCE_COMPLEXITY, VALUE9);
        LinguisticSignature ls1 = new LinguisticSignature(features1);

        HashMap<FeatureType, Double> features2 = new HashMap<>();
        features2.put(AVERAGE_WORD_LENGTH, VALUE10);
        features2.put(TYPE_TOKEN_RATIO, VALUE11);
        features2.put(HAPAX_LEGOMENA_RATIO, VALUE12);
        features2.put(AVERAGE_SENTENCE_LENGTH, VALUE13);
        features2.put(AVERAGE_SENTENCE_COMPLEXITY, VALUE14);
        LinguisticSignature ls2 = new LinguisticSignature(features2);

        InputStream isSignatures = new FileInputStream(RESOURCES_KNOWN_SIGNATURES_TXT);
        AuthorshipDetectorImpl adl = new AuthorshipDetectorImpl(isSignatures, WEIGHTS);

        assertEquals(EXPECTED, adl.calculateSimilarity(ls1, ls2), DELTA);
        assertEquals(EXPECTED1, adl.calculateSimilarity(ls2, ls1), DELTA);
        assertEquals(EXPECTED2, adl.calculateSimilarity(ls1, ls1), DELTA);
    }

    @Test
    public void findAuthorTests() throws FileNotFoundException {
        InputStream isSignatures = new FileInputStream(RESOURCES_KNOWN_SIGNATURES_TXT);
        AuthorshipDetectorImpl adl = new AuthorshipDetectorImpl(isSignatures, WEIGHTS);

        InputStream isMysteryText = new FileInputStream(RESOURCES_MYSTERY_FILES_1_TXT);
        assertEquals(CUSTOM_SIGNATURE, adl.findAuthor(isMysteryText));

        InputStream isMysteryText2 = new FileInputStream(RESOURCES_MYSTERY_FILES_MYSTERY_4_TXT);
        assertEquals(AGATHA_CHRISTIE, adl.findAuthor(isMysteryText2));
    }
}
