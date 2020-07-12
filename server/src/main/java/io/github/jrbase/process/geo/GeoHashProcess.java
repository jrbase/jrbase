package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.score.PositionScore;
import io.github.jrbase.database.GeoRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEOHASH;
import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 * GEOHASH key member [member ...]
 * Time complexity: O(log(N)) for each member requested, where N is the number of elements in the sorted set.
 * <p>
 * redis> GEOADD Sicily 13.361389 38.115556 "Palermo" 15.087269 37.502669 "Catania"
 * (integer) 2
 * redis> GEOHASH Sicily Palermo Catania
 * 1) "sqc8b49rny0"
 * 2) "sqdtr74hyu0"
 * redis>
 */
@KeyCommand
public class GeoHashProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEOHASH.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 1;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
        if (redisValue == null) {
            return REDIS_EMPTY_STRING;
        }
        if (!(redisValue instanceof GeoRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        GeoRedisValue geoRedisValue = (GeoRedisValue) redisValue;
        String[] args = clientCmd.getArgs();
        StringBuilder result = new StringBuilder();
        result.append("*").append(args.length).append("\r\n");
        for (String member : args) {
            PositionScore positionScore = geoRedisValue.get(member);
            if (positionScore == null) {
                result.append("$").append(-1).append("\r\n");
            } else {
                String positionCode = positionScore.getPositionCode();
                result.append("$").append(positionCode.length()).append("\r\n")
                        .append(positionCode).append("\r\n");
            }
        }
        return result.toString();
    }
}
