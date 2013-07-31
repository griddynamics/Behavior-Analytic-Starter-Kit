package com.griddynamics.deming.data.generator.hadoop.mr;

import com.google.common.base.Objects;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author Denis Khurtin (dkhurtin@griddynamics.com)
 */
public class TransactionsGenerationSplit extends InputSplit implements Writable {

    private long startPosition;
    private long count;

    @SuppressWarnings("UnusedDeclaration")
    public TransactionsGenerationSplit() {}

    public TransactionsGenerationSplit(long startPosition, long count) {
        this.startPosition = startPosition;
        this.count = count;
    }

    public long getStartPos() {
        return startPosition;
    }

    @Override
    public long getLength() throws IOException, InterruptedException {
        return count;
    }

    @Override
    public String[] getLocations() throws IOException, InterruptedException {
        return new String[]{};
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        startPosition = in.readLong();
        count = in.readLong();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(startPosition);
        out.writeLong(count);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        TransactionsGenerationSplit that = (TransactionsGenerationSplit) obj;

        return Objects.equal(this.startPosition, that.startPosition) &&
                Objects.equal(this.count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startPosition, count);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(getClass())
                .add("startPosition", startPosition)
                .add("count", count)
                .toString();
    }
}
