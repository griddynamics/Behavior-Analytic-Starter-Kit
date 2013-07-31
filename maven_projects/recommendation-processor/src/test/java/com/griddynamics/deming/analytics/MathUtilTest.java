package com.griddynamics.deming.analytics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.primitives.Booleans;
import org.junit.Test;
import org.junit.Assert;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class MathUtilTest {

    private static final boolean T = true;
    private static final boolean F = false;

    @Test
    public void test2_1() {
        boolean[] combination = MathUtil.createInitialCombination(2, 1);

        List<boolean[]> result = Lists.newArrayList();
        result.add(Arrays.copyOf(combination, combination.length));

        while (MathUtil.nextCombination(combination)) {
            result.add(Arrays.copyOf(combination, combination.length));
        }

        assertBooleanArrayListEquals(result, ImmutableList.of(
                new boolean[] {T, F},
                new boolean[] {F, T}
        ));
    }

    @Test
    public void test3_1() {
        boolean[] combination = MathUtil.createInitialCombination(3, 1);

        List<boolean[]> result = Lists.newArrayList();
        result.add(Arrays.copyOf(combination, combination.length));

        while (MathUtil.nextCombination(combination)) {
            result.add(Arrays.copyOf(combination, combination.length));
        }

        assertBooleanArrayListEquals(result, ImmutableList.of(
                new boolean[] {T, F, F},
                new boolean[] {F, T, F},
                new boolean[] {F, F, T}
        ));
    }

    @Test
    public void test3_2() {
        boolean[] combination = MathUtil.createInitialCombination(3, 2);

        List<boolean[]> result = Lists.newArrayList();
        result.add(Arrays.copyOf(combination, combination.length));

        while (MathUtil.nextCombination(combination)) {
            result.add(Arrays.copyOf(combination, combination.length));
        }

        assertBooleanArrayListEquals(result, ImmutableList.of(
                new boolean[] {T, T, F},
                new boolean[] {T, F, T},
                new boolean[] {F, T, T}
        ));
    }

    @Test
    public void test4_1() {
        boolean[] combination = MathUtil.createInitialCombination(4, 1);

        List<boolean[]> result = Lists.newArrayList();
        result.add(Arrays.copyOf(combination, combination.length));

        while (MathUtil.nextCombination(combination)) {
            result.add(Arrays.copyOf(combination, combination.length));
        }

        assertBooleanArrayListEquals(result, ImmutableList.of(
                new boolean[] {T, F, F, F},
                new boolean[] {F, T, F, F},
                new boolean[] {F, F, T, F},
                new boolean[] {F, F, F, T}
        ));
    }

    @Test
    public void test4_2() {
        boolean[] combination = MathUtil.createInitialCombination(4, 2);

        List<boolean[]> result = Lists.newArrayList();
        result.add(Arrays.copyOf(combination, combination.length));

        while (MathUtil.nextCombination(combination)) {
            result.add(Arrays.copyOf(combination, combination.length));
        }

        assertBooleanArrayListEquals(result, ImmutableList.of(
                new boolean[] {T, T, F, F},
                new boolean[] {T, F, T, F},
                new boolean[] {T, F, F, T},
                new boolean[] {F, T, T, F},
                new boolean[] {F, T, F, T},
                new boolean[] {F, F, T, T}
        ));
    }

    private static void assertBooleanArrayListEquals(List<boolean[]> actual, List<boolean[]> expected) {
        Assert.assertEquals(expected.size(), actual.size());

        Comparator<boolean[]> comparator = Booleans.lexicographicalComparator();

        for (int i = 0; i < expected.size(); ++i) {
            Assert.assertEquals(comparator.compare(expected.get(i), actual.get(i)), 0);
        }
    }
}
