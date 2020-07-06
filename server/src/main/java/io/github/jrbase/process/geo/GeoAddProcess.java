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
        return isGeoAddArgument(clientCmd.getArgLength());
    }

    private boolean isGeoAddArgument(int length) {
        return length >= 3 && (length - 3) % 3 == 0;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new GeoRedisValue());
        if (!(redisValue instanceof GeoRedisValue)) {
            return REDIS_ERROR_OPERATION_AGAINST;
        }
        GeoRedisValue geoRedisValue = (GeoRedisValue) redisValue;
        int addSize = addGeo(geoRedisValue, clientCmd.getArgs());
        clientCmd.getDb().put(clientCmd.getKey(), geoRedisValue);
        return gainSize(addSize);
    }

    private int addGeo(GeoRedisValue geoRedisValue, String[] args) {
        int addSize = 0;
        for (int i = 0; i < args.length; i = i + 3) {
            //                         log       lat       member
            int add = geoRedisValue.put(args[i], args[i + 1], args[i + 2]);
            addSize += add;
        }
        return addSize;

    }

    private String gainSize(int addSize) {
        return ":" + addSize + "\r\n";
    }
}
