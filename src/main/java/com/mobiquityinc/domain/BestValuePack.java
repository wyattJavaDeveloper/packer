package com.mobiquityinc.domain;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for storing the best pack determined by PackageProcessor
 */
public class BestValuePack {

    private List<Item> items;

    public BestValuePack(List<Item> items) {
        this.items = items;
    }

    /**
     * The toString method has been overridden by this implementation
     *
     * @return The method will return all the Item indexNumbers separated by a comma
     */
    public String toString() {
        return items.isEmpty() ? "-" : String.join(",",
                items.stream()
                        .map(i -> String.valueOf(i.getIndexNumber()))
                        .collect(Collectors.toList()));
    }

    public List<Item> getItems() {
        return items;
    }
}