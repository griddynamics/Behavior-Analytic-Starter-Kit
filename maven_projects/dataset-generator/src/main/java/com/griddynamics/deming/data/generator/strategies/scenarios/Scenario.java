package com.griddynamics.deming.data.generator.strategies.scenarios;

import java.util.List;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class Scenario {

    public String name;
    public double probability;
    public List<BehaviorItem> behavior;

    public static class BehaviorItem {
        public String productCategory;
        public double probability;
    }
}
