package com.griddynamics.deming.data.generator.strategies.random;

import java.util.*;

/**
 * @author Max Morozov
 */
public class Pattern<ID> {

    //List of product IDs
    private final List<ID> items = new ArrayList<ID>();

    //List of categories IDs
    private final List<ID> categories = new ArrayList<ID>();

    //probability that this pattern is chosen
    private double probability;
    private double cumulativeProbability;

    //probability that this string is corrupted
    private double confidence;

    public Pattern() {
    }

    public Pattern(double probability, double confidence) {
        this.probability = probability;
        this.confidence = confidence;
    }

    public Pattern(Collection<ID> items, double probability, double confidence) {
        this.items.addAll(items);
        this.probability = probability;
        this.confidence = confidence;
    }

    public Pattern(Collection<ID> items, Collection<ID> categories, double probability, double confidence) {
        this.items.addAll(items);
        this.categories.addAll(categories);
        this.probability = probability;
        this.confidence = confidence;
    }

    /**
     * Adds the specified product ID to the pattern
     *
     * @param itemId product ID
     */
    public void addItem(ID itemId) {
        items.add(itemId);
    }

    public List<ID> getItems() {
        return items;
    }

    public void addCategory(ID categoryId) {
        categories.add(categoryId);
    }

    public List<ID> getCategories() {
        return categories;
    }

    public int getSize() {
        return items.size();
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public List<ID> shuffle(int size) {
        List<ID> result = new ArrayList<ID>(items);
        Collections.shuffle(result);
        return result.subList(0, size);
    }

    public List<ID> shuffle(int size, Random random) {
        List<ID> result = new ArrayList<ID>(items);
        Collections.shuffle(result, random);
        return result.subList(0, size);
    }

    public double getCumulativeProbability() {
        return cumulativeProbability;
    }

    public void setCumulativeProbability(double cumulativeProbability) {
        this.cumulativeProbability = cumulativeProbability;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}
