package io.github.jrbase.process.geo;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.GeoRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GEOADD;
import static io.github.jrbase.dataType.CommonMessage.REDIS_ERROR_OPERATION_AGAINST;

/**
 * GEOADD key longitude latitude member [longitude latitude member ...]
 */
@KeyCommand
public class GeoAddProcess implements CmdProcess {
    @Override
    public String getCmdName() {
        return GEOADD.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return isgeoaddArgument(clientCmd.getArgLength());
    }

    private boolean isgeoaddArgument(int length) {
        return length >= 5 && (length - 5) % 3 == 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new GeoRedisValue());
        if (!(redisValue instanceof GeoRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        GeoRedisValue geoRedisValue = (GeoRedisValue) redisValue;
        geoRedisValue.put(clientCmd.getArgs()[0], clientCmd.getArgs()[1], clientCmd.getArgs()[2]);
        return null;
    }
}
