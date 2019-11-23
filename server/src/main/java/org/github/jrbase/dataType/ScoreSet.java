package org.github.jrbase.dataType;

/**
 * redis sort set
 */
public class ScoreSet {
    private String data;
    private int score;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (!(obj instanceof ScoreSet)) {
            return false;
        }
        final ScoreSet other = (ScoreSet) obj;
        return this.data.equals(other.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
}
