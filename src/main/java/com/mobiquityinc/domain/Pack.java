package com.mobiquityinc.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Responsible for storing a pack that needs to be processed.
 */
public class Pack {

    private int capacity;
    private List<Item> items;

    /**
     * @param capacity the total size the pack can contain
     * @param items    available to select from
     */
    public Pack(int capacity, List<Item> items) {
        this.capacity = capacity;
        this.items = items;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Item> getItems() {
        return items;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("capacity", capacity)
                .append("items", items)
                .toString();
    }
}
