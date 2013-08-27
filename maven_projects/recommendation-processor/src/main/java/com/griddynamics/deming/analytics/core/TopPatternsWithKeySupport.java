package com.griddynamics.deming.analytics.core;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This class based on {@link org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns}
 * This class was extended by key support.
 *
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class TopPatternsWithKeySupport implements Writable {

    private final List<Pair<List<String>,Long>> frequentPatterns = Lists.newArrayList();
    private long keySupport;

    public TopPatternsWithKeySupport() {/* for serialization */}

    public TopPatternsWithKeySupport(long keySupport, Collection<Pair<List<String>, Long>> patterns) {
        this.keySupport = keySupport;
        frequentPatterns.addAll(patterns);
    }

    public long getKeySupport() {
        return keySupport;
    }

    public Iterator<Pair<List<String>,Long>> iterator() {
        return frequentPatterns.iterator();
    }

    public List<Pair<List<String>,Long>> getPatterns() {
        return frequentPatterns;
    }

    public TopPatternsWithKeySupport merge(TopPatternsWithKeySupport pattern, int heapSize) {
        List<Pair<List<String>,Long>> patterns = Lists.newArrayList();

        Iterator<Pair<List<String>,Long>> myIterator = frequentPatterns.iterator();
        Iterator<Pair<List<String>,Long>> otherIterator = pattern.iterator();

        Pair<List<String>,Long> myItem = null;
        Pair<List<String>,Long> otherItem = null;

        for (int i = 0; i < heapSize; i++) {
            if (myItem == null && myIterator.hasNext()) {
                myItem = myIterator.next();
            }
            if (otherItem == null && otherIterator.hasNext()) {
                otherItem = otherIterator.next();
            }

            if (myItem != null && otherItem != null) {
                int cmp = myItem.getSecond().compareTo(otherItem.getSecond());
                if (cmp == 0) {
                    cmp = myItem.getFirst().size() - otherItem.getFirst().size();
                    if (cmp == 0) {
                        for (int j = 0; j < myItem.getFirst().size(); j++) {
                            cmp = myItem.getFirst().get(j).compareTo(
                                    otherItem.getFirst().get(j));
                            if (cmp != 0) {
                                break;
                            }
                        }
                    }
                }
                if (cmp <= 0) {
                    patterns.add(otherItem);
                    if (cmp == 0) {
                        myItem = null;
                    }
                    otherItem = null;
                } else if (cmp > 0) {
                    patterns.add(myItem);
                    myItem = null;
                }
            } else if (myItem != null) {
                patterns.add(myItem);
                myItem = null;
            } else if (otherItem != null) {
                patterns.add(otherItem);
                otherItem = null;
            } else {
                break;
            }
        }

        return new TopPatternsWithKeySupport(Math.max(this.keySupport, pattern.keySupport), patterns);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        keySupport = in.readLong();

        frequentPatterns.clear();
        int length = in.readInt();

        for (int i = 0; i < length; i++) {
            List<String> items = Lists.newArrayList();
            int itemsetLength = in.readInt();
            long support = in.readLong();

            for (int j = 0; j < itemsetLength; j++) {
                items.add(in.readUTF());
            }

            frequentPatterns.add(new Pair<List<String>,Long>(items, support));
        }
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(keySupport);
        out.writeInt(frequentPatterns.size());

        for (Pair<List<String>,Long> pattern : frequentPatterns) {
            out.writeInt(pattern.getFirst().size());
            out.writeLong(pattern.getSecond());

            for (String item : pattern.getFirst()) {
                out.writeUTF(item);
            }
        }
    }

    @Override
    public String toString() {
        return keySupport + "; " + Joiner.on(", ").join(frequentPatterns);
    }
}
