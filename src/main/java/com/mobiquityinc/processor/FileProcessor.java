package com.mobiquityinc.processor;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.domain.Item;
import com.mobiquityinc.domain.Pack;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileProcessor {

    private static final int MAX_PACKAGE_WEIGHT = 100;
    private static final int MAX_PACKAGE_NUMBER_OF_ITEMS = 15;
    private static final int MAX_ITEM_WEIGHT = 100;
    private static final int MAX_ITEM_COST = 100;

    private static FileProcessor INSTANCE = null;

    /**
     * Private constructor prevents instantiation from other classes
     */
    private FileProcessor() {
    }

    /**
     * FileProcessorHolder is loaded on the first execution of FileProcessorHolder.getInstance()
     * or the first access to FileProcessorHolder.INSTANCE, not before.
     */
    private static class FileProcessorHolder {
        private static final FileProcessor INSTANCE = new FileProcessor();
    }

    /**
     * @return
     */
    public static FileProcessor getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new FileProcessor();
        }

        return FileProcessor.INSTANCE;
    }


    /**
     * @return All the packs in the file is collected and returned for processing
     * @throws APIException
     */
    public List<Pack> processFile(String filePath) throws APIException {

        File inputFile = new File(filePath);
        try {
            return Files.readAllLines(inputFile.toPath())
                    .stream()
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        } catch (IOException io) {
            throw new APIException(io.getMessage());
        }

    }

    /**
     * @param line
     * @return
     */
    private Pack parseLine(String line) throws APIException {

        int packageWeight;
        List<Item> packageItems = null;
        try {
            // Remove all spaces
            line = line.replace(" ", "");
            packageWeight = validatePackWeight(line);

            // Match all values between all parenthesis for the line
            Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(line);
            packageItems = new ArrayList<>();
            while (m.find()) {
                String[] lineItems = m.group(1).split("\\,");
                packageItems.add(new Item(Integer.valueOf(lineItems[0]), validateItemWeight(lineItems), validateItemCost(lineItems)));
            }

            validateNumberOfItems(packageItems.size());
        } catch (APIException apiException) {
            throw new APIException(String.format("Invalid line [%s] - %s", line, apiException.getMessage()));
        }
        return new Pack(packageWeight, packageItems);
    }

    /**
     * @param line
     * @return
     */
    private int validatePackWeight(String line) {

        Pattern pattern = Pattern.compile("^.+?(?=:)");
        Matcher matcher = pattern.matcher(line);

        if (!matcher.find()) {
            throw new APIException("Package weight not provided");
        }

        int weight = Integer.valueOf(matcher.group());

        if (weight > MAX_PACKAGE_WEIGHT) {
            throw new APIException(String.format("Package weight %s exceeds the maximum package weight of %S", weight, MAX_PACKAGE_WEIGHT));
        }

        return weight;
    }

    /**
     * @param value
     * @return
     */
    private BigDecimal validateItemWeight(String[] value) {

        if (value == null) {
            throw new APIException("Item weight not provided.");
        }

        BigDecimal itemWeight = new BigDecimal(value[1]);
        if (itemWeight.intValue() > MAX_ITEM_WEIGHT) {
            throw new APIException(String.format("Item weight %s exceeds the maximum item weight of %S", itemWeight, MAX_ITEM_WEIGHT));
        }

        return itemWeight;
    }

    /**
     * @param value
     * @return
     */
    private BigDecimal validateItemCost(String[] value) {

        if (value == null) {
            throw new APIException("Item weight not provided.");
        }

        BigDecimal itemCost = new BigDecimal(value[2].replaceAll("[^\\d]", ""));

        if (itemCost.intValue() > MAX_ITEM_COST) {
            throw new APIException(String.format("Item cost %s exceeds the maximum item cost of %S", itemCost, MAX_ITEM_COST));
        }

        return itemCost;
    }

    /**
     * @param value
     */
    private void validateNumberOfItems(int value) {

        if (value > MAX_PACKAGE_NUMBER_OF_ITEMS) {
            throw new APIException(String.format("Total number of items %s exceeds the maximum of %s", value, MAX_PACKAGE_NUMBER_OF_ITEMS));
        }
    }


}
