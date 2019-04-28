package com.mobiquityinc.processor;

import com.mobiquityinc.domain.BestValuePack;
import com.mobiquityinc.domain.Item;
import com.mobiquityinc.domain.Pack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for solving the BestValuePack for a Pack capacity and list of items
 */
public class PackageProcessor {

    private static PackageProcessor INSTANCE = null;

    /**
     * Private constructor prevents instantiation from other classes
     */
    private PackageProcessor() {
    }

    /**
     * PackageProcessorHolder is loaded on the first execution of PackageProcessor.getInstance()
     * or the first access to PackageProcessorHolder.INSTANCE, not before.
     */
    private static class PackageProcessorHolder {
        private static final PackageProcessor INSTANCE = new PackageProcessor();
    }

    /**
     * @return
     */
    public static PackageProcessor getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new PackageProcessor();
        }

        return PackageProcessor.INSTANCE;
    }

    /**
     * To calculate the best possible pack given a list of items and capacity we using Dynamic Programming.
     *@See https://en.wikipedia.org/wiki/Knapsack_problem
     * @return
     */
    public BestValuePack calculateBestValuePack(Pack pack) {
        int numberOfItems = pack.getItems().size();
        //a matrix to store the max value at each n-th item
        int[][] matrix = new int[numberOfItems + 1][pack.getCapacity() + 1];

        // first line is initialized to 0
        for (int i = 0; i <= pack.getCapacity(); i++) {
            matrix[0][i] = 0;
        }

        // we iterate on items
        for (int i = 1; i <= numberOfItems; i++) {
            // we iterate on each capacity
            for (int j = 0; j <= pack.getCapacity(); j++) {
                if (pack.getItems().get(i - 1).getWeight().intValue() > j)
                    matrix[i][j] = matrix[i - 1][j];
                else
                    // we maximize value at this rank in the matrix
                    matrix[i][j] = Math.max(matrix[i - 1][j], matrix[i - 1][j - pack.getItems().get(i - 1).getWeight().intValue()]
                            + pack.getItems().get(i - 1).getCost().intValue());
            }
        }

        int res = matrix[numberOfItems][pack.getCapacity()];
        int w = pack.getCapacity();
        List<Item> itemsSolution = new ArrayList<>();

        for (int i = numberOfItems; i > 0 && res > 0; i--) {
            if (res != matrix[i - 1][w]) {
                itemsSolution.add(pack.getItems().get(i - 1));
                // we remove items value and weight
                res -= pack.getItems().get(i - 1).getCost().intValue();
                w -= pack.getItems().get(i - 1).getWeight().intValue();
            }
        }

        return new BestValuePack(itemsSolution);
    }
}
