package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class PremiumOffer extends RegularOffer {
    private final double MAX_DISCOUNT = 100.00;
    private final int MAX_DIGITS_AFTER_POINT = 2;
    private double discount;

    public PremiumOffer(String productName, LocalDate date, String description, double price, double shippingPrice, double discount) {
        super(productName, date, description, price, shippingPrice);
        setDiscount(discount);
        setTotalPrice((price + shippingPrice) - (price + shippingPrice) * (this.discount / MAX_DISCOUNT));
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        if (discount < 0.00 || discount > MAX_DISCOUNT) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(Double.toString(discount));
        bd = bd.setScale(MAX_DIGITS_AFTER_POINT, RoundingMode.HALF_UP); //Rounding to the last 2 digits after the point

        this.discount = bd.doubleValue();
    }
}
