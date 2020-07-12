package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.score.PositionScore;
import io.github.jrbase.database.GeoRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEOPOS;
import static io.github.jrbase.dataType.CommonMessage.REDIS_EMPTY_STRING;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

@KeyCommand
public class GeoPosProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEOPOS.getCmdName();
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
        // result value
        //*3
        //*2
        //$18
        //13.361389338970184
        //$18
        //38.115556395496299
        //*2
        //$18
        //15.087267458438873
        //$17
        //37.50266842333162
        //*-1
        StringBuilder result = new StringBuilder();
        result.append("*").append(args.length).append("\r\n");
        for (String member : args) {
            PositionScore positionScore = geoRedisValue.get(member);
            if (positionScore == null) {
                result.append("*").append(-1).append("\r\n");
            } else {
                double longitude = positionScore.getLongitude();
                double latitude = positionScore.getLatitude();
                result.append("*2").append("\r\n");
                result.append("$").append(Double.valueOf(longitude).toString().length()).append("\r\n")
                        .append(longitude).append("\r\n");
                result.append("$").append(Double.valueOf(latitude).toString().length()).append("\r\n")
                        .append(latitude).append("\r\n");
            }
        }
        return result.toString();
    }
}
