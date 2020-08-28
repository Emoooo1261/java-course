package bg.sofia.uni.fmi.mjt.shopping.portal;

import java.time.LocalDate;

public class PriceStatistic {
    private String productName;
    private LocalDate date;
    private double lowestPrice;
    private double averagePrice;
    private int offersCounter;

    public PriceStatistic(String productName, LocalDate date) {
        setProductName(productName);
        setDate(date);
        setLowestPrice(0); //When the statistic is empty there is no lowest price
        setAveragePrice(0);
        offersCounter = 0;
    }

    /**
     * Returns the date for which the statistic is
     * collected.
     */
    public LocalDate getDate() {
        if (date == null) {
            throw new UnsupportedOperationException();
        }
        return date;
    }

    /**
     * Returns the lowest total price from the offers
     * for this product for the specific date.
     */
    public double getLowestPrice() {
        if (lowestPrice < 0) {
            throw new UnsupportedOperationException();
        }
        return lowestPrice;
    }

    /**
     * Return the average total price from the offers
     * for this product for the specific date.
     */
    public double getAveragePrice() {
        if (averagePrice < 0) {
            throw new UnsupportedOperationException();
        }
        return averagePrice;
    }

    public void setProductName(String productName) {
        if(productName == null || productName.isEmpty()) {
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

    public void setLowestPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
        if(lowestPrice == 0) {
            lowestPrice = price;
        } else if (lowestPrice > price) {
            lowestPrice = price;
        }
    }

    public void setAveragePrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException();
        }
        averagePrice = ((averagePrice * offersCounter) + price) / (++offersCounter);
    }

}
