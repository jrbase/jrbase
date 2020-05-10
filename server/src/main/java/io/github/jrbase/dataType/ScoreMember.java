package io.github.jrbase.dataType;

import org.jetbrains.annotations.NotNull;

/**
 * redis sort set
 */
public class ScoreMember implements Comparable<ScoreMember> {
    private String member;
    private int score;

    public ScoreMember(String member, int score) {
        this.member = member;
        this.score = score;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
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
        if (!(obj instanceof ScoreMember)) {
            return false;
        }
        final ScoreMember other = (ScoreMember) obj;
        return this.member.equals(other.member);
    }

    @Override
    public int hashCode() {
        return member.hashCode();
    }

    @Override
    public int compareTo(@NotNull ScoreMember o) {
        if (this.score > o.score) {
            return 1;
        } else if (this.score < o.score) {
            return -1;
        } else {
            return this.member.compareTo(o.member);
        }
    }

    @Override
    public String toString() {
        return "ScoreMember{" +
                "member='" + member + '\'' +
                ", score=" + score +
                '}';
    }
}
