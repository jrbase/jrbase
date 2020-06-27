package io.github.jrbase.client.utils.skipList;

import io.github.jrbase.dataType.ScoreMember;
import io.github.jrbase.dataType.score.PositionScore;

public class SkipLists {

    public static void putScoreMember(final SkipList<ScoreMember> skipList, String member, double score) {

        if (member == null) {
            throw new NullPointerException();
        }
        skipList.lock();
        try {
            ScoreMember scoreMember = skipList.find(member);
            if (scoreMember != null) {
                scoreMember.setScore(score);
            } else {
                skipList.put(member, new ScoreMember(score));
            }
        } finally {
            skipList.unLock();
        }

    }

    public static void putPosition(final SkipList<PositionScore> skipList, String member,
                                   double latitude, double longitude) {
        if (member == null) {
            throw new NullPointerException();
        }
        skipList.lock();
        try {
            PositionScore scoreMember = skipList.find(member);
            if (scoreMember != null) {
                scoreMember.setLongitude(longitude);
                scoreMember.setLatitude(latitude);
            } else {
                skipList.put(member, new PositionScore(longitude, latitude));
            }
        } finally {
            skipList.unLock();
        }

    }

}
