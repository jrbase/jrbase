package io.github.jrbase.dataType;

import org.jetbrains.annotations.NotNull;

/**
 * redis sort set
 */
public class ScoreMember implements Comparable<ScoreMember> {
    private double score;

    public ScoreMember(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof ScoreMember)) {
            return false;
        }
        final ScoreMember other = (ScoreMember) obj;
        return this.score == other.score;
    }

    @Override
    public int hashCode() {
        return (int) score;
    }

    @Override
    public int compareTo(@NotNull ScoreMember o) {
        return (int) (this.score - o.score);
    }

    @Override
    public String toString() {
        return "" + score;
    }
}
