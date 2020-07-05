package io.github.jrbase.process.geo;

import io.github.jrbase.client.utils.geo.GEOUtils;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.score.PositionScore;
import io.github.jrbase.database.GeoRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEODIST;
import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 * GEODIST key member1 member2 [m|km|ft|mi]
 */
@KeyCommand
public class GeoDistProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEODIST.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() == 2 || clientCmd.getArgLength() == 3;
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
        PositionScore positionScore1 = geoRedisValue.get(clientCmd.getArgs()[0]);
        if (positionScore1 == null) {
            return REDIS_EMPTY_STRING;
        }
        PositionScore positionScore2 = geoRedisValue.get(clientCmd.getArgs()[1]);
        if (positionScore2 == null) {
            return REDIS_EMPTY_STRING;
        }
        String distance;
        if (clientCmd.getArgLength() == 2) {
            distance = GEOUtils.distanceByUnit(positionScore1.getLatitude(), positionScore1.getLongitude(),
                    positionScore2.getLatitude(), positionScore2.getLongitude(), "km");
        } else {
            try {
                distance = GEOUtils.distanceByUnit(positionScore1.getLatitude(), positionScore1.getLongitude(),
                        positionScore2.getLatitude(), positionScore2.getLongitude(), clientCmd.getArgs()[2]);
            } catch (Exception e) {
                return "-ERR unsupported unit provided. please use m, km, ft, mi\r\n";
            }
        }
        return SimpleString(distance);
    }

    private String SimpleString(String distance) {
        return "$" + distance.length() + "\r\n" + distance + "\r\n";
    }
}
