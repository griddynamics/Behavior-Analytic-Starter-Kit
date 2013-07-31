package com.griddynamics.deming.data.generator.strategies;

import com.griddynamics.deming.data.generator.strategies.random.Pattern;

import java.util.List;

/**
 * @author Max Morozov
 */
public class Utils {
    /**
     * Normalize the patterns probabilities and calculate cumulative probabilities of each rule.
     *
     * @param patterns list of patterns with probabilities to normalize
     */
    public static <ID> void normalize(List<Pattern<ID>> patterns) {
        double probabilitySum = 0;
        for (Pattern<ID> pattern : patterns) {
            probabilitySum += pattern.getProbability();
        }

        double priorProbability = 0;
        for (Pattern<ID> pattern : patterns) {
            double normalized = pattern.getProbability() / probabilitySum;
            pattern.setProbability(normalized);
            pattern.setCumulativeProbability(normalized + priorProbability);
            priorProbability = pattern.getCumulativeProbability();
        }
    }
}
