package bg.sofia.uni.fmi.mjt.authorship.detection;

public class CustomPair {
    private String authorName;
    private LinguisticSignature signature;

    CustomPair(String authorName, LinguisticSignature signature) {
        this.authorName = authorName;
        this.signature = signature;
    }

    public String getAuthorName() {
        return authorName;
    }

    public LinguisticSignature getSignature() {
        return signature;
    }
}
