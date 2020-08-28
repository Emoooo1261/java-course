package bg.sofia.uni.fmi.mjt.authorship.detection;

import java.util.HashMap;
import java.util.Map;

public class LinguisticSignature {
    private HashMap<FeatureType, Double> features;

    public LinguisticSignature(Map<FeatureType, Double> features) {
        this.features = (HashMap<FeatureType, Double>) features;
    }

    public Map<FeatureType, Double> getFeatures() {
        return features;
    }
}
