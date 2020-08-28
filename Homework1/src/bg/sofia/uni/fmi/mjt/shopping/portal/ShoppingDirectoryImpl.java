package bg.sofia.uni.fmi.mjt.shopping.portal;

import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.NoOfferFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.OfferAlreadySubmittedException;
import bg.sofia.uni.fmi.mjt.shopping.portal.exceptions.ProductNotFoundException;
import bg.sofia.uni.fmi.mjt.shopping.portal.offer.Offer;

import java.time.LocalDate;
import java.util.*;

public class ShoppingDirectoryImpl implements ShoppingDirectory {
    private HashMap<String, TreeSet<Offer>> offers;
    private HashMap<String, TreeMap<LocalDate, PriceStatistic>> priceStatistics;

    ShoppingDirectoryImpl() {
        offers = new HashMap<>();
        priceStatistics = new HashMap<>();
    }

    @Override
    public Collection<Offer> findAllOffers(String productName) throws ProductNotFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }
        if (!offers.containsKey(productName)) {
            throw new ProductNotFoundException();
        }
        return offers.get(productName);
    }

    @Override
    public Offer findBestOffer(String productName) throws ProductNotFoundException, NoOfferFoundException {
        if (productName == null) {
            throw new IllegalArgumentException();
        }
        if (!offers.containsKey(productName)) {
            throw new ProductNotFoundException();
        }
        TreeSet<Offer> offersSet = offers.get(productName);

        if (offersSet == null || offersSet.isEmpty()) {
            throw new NoOfferFoundException();
        }

        return offersSet.first();
    }

    @Override
    public Collection<PriceStatistic> collectProductStatistics(String productName) throws ProductNotFoundException {
        if (productName == null || productName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        if (!priceStatistics.containsKey(productName)) {
            throw new ProductNotFoundException();
        }

        return priceStatistics.get(productName).values();
    }

    @Override
    public void submitOffer(Offer offer) throws OfferAlreadySubmittedException {
        if (offer == null) {
            throw new IllegalArgumentException();
        }
        if (offers.containsKey(offer.getProductName())) {
            if (!offers.get(offer.getProductName()).add(offer)) {
                throw new OfferAlreadySubmittedException();
            }
        } else {
            TreeSet<Offer> tree = new TreeSet<>();
            tree.add(offer);
            offers.put(offer.getProductName(), tree);
        }
        addPriceStatistics(offer);
    }

    private void addPriceStatistics(Offer offer) {
        if (offer == null) {
            throw new IllegalArgumentException();
        }
        if (!priceStatistics.containsKey(offer.getProductName())) { //If there is no product with this name in the hashMap
            TreeMap<LocalDate, PriceStatistic> tree = new TreeMap<>(Comparator.reverseOrder()); //Making new treeMap for the product's stats and reversing the order
            PriceStatistic newStat = new PriceStatistic(offer.getProductName(), offer.getDate()); //Making new stat for the product
            newStat.setLowestPrice(offer.getTotalPrice()); //Setting the prices
            newStat.setAveragePrice(offer.getTotalPrice());
            tree.put(offer.getDate(), newStat); //Putting the stat in the tree
            priceStatistics.put(offer.getProductName(), tree); //And putting the tree in the hashMap
        } else if (!priceStatistics.get(offer.getProductName()).containsKey(offer.getDate())) { //Else if there is no offer in the treeMap with this date
            PriceStatistic newStat = new PriceStatistic(offer.getProductName(), offer.getDate());
            newStat.setLowestPrice(offer.getTotalPrice()); //Making new stat with the new date and setting the prices
            newStat.setAveragePrice(offer.getTotalPrice());
            priceStatistics.get(offer.getProductName()).put(offer.getDate(), newStat); //Putting the new stat in the tree
        } else { //Else there is product with this date
            PriceStatistic newStat = priceStatistics.get(offer.getProductName()).get(offer.getDate());
            newStat.setLowestPrice(offer.getTotalPrice()); //Just setting the new prices
            newStat.setAveragePrice(offer.getTotalPrice());
            priceStatistics.get(offer.getProductName()).put(offer.getDate(), newStat); //Putting the new stat in the tree
        }
    }
}
