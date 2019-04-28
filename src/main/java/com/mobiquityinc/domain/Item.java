package com.mobiquityinc.domain;

import java.math.BigDecimal;

/**
 * Class responsible for storing a item that can be placed into a pack or best value pack
 */
public class Item {

    private int indexNumber;
    private BigDecimal weight;
    private BigDecimal cost;

    public Item(int indexNumber, BigDecimal weight, BigDecimal cost) {
        this.indexNumber = indexNumber;
        this.weight = weight;
        this.cost = cost;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("indexNumber", indexNumber)
                .append("weight", weight)
                .append("cost", cost)
                .toString();
    }
}
