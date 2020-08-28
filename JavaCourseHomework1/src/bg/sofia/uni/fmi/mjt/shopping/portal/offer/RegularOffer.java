package bg.sofia.uni.fmi.mjt.shopping.portal.offer;

import java.time.LocalDate;
import java.util.Objects;

public class RegularOffer implements Offer {
    private String productName;
    private LocalDate date;
    private String description;
    private double price;
    private double shippingPrice;
    private double totalPrice;

    public RegularOffer(String productName, LocalDate date, String description, double price, double shippingPrice) {
        setProductName(productName);
        setDate(date);
        setDescription(description);
        setPrice(price);
        setShippingPrice(shippingPrice);
        setTotalPrice(price + shippingPrice);
    }

    @Override
    public String getProductName() {
        return productName;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getShippingPrice() {
        return shippingPrice;
    }

    @Override
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setProductName(String productName) {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.productName = productName;
    }

    public void setDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException();
        }
        this.date = date;
    }

    public void setDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
        this.price = price;
    }

    public void setShippingPrice(double shippingPrice) {
        if (shippingPrice < 0) {
            throw new IllegalArgumentException();
        }
        this.shippingPrice = shippingPrice;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException();
        }
        this.totalPrice = totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularOffer that = (RegularOffer) o;
        return Double.compare(that.totalPrice, totalPrice) == 0 &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, date, totalPrice);
    }

    @Override
    public int compareTo(Object other) {
        Double price = totalPrice;
        RegularOffer reOffer = (RegularOffer) other;
        return (price.compareTo(reOffer.totalPrice));
    }
}
