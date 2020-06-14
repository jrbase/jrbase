package io.github.jrbase.process.string;

import io.github.jrbase.client.utils.Tools;
import io.github.jrbase.common.datatype.Cmd;
import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.database.ByteRedisValue;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;

import static io.github.jrbase.common.datatype.Cmd.GETBIT;

/**
 * SETBIT key offset value
 * Time complexity: O(1)
 */
@KeyCommand
public class SetBitProcess implements CmdProcess {
    // 1M
    public static final int MAX_BIT = 1024 * 1024;

    @Override
    public String getCmdName() {
        return Cmd.SETBIT.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return Tools.checkArgs(2, clientCmd.getArgLength());
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }

    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (GETBIT) {
            final RedisValue redisValue = clientCmd.getDb().getOrDefault(clientCmd.getKey(), new ByteRedisValue());
            final ByteRedisValue byteRedisValue = (ByteRedisValue) redisValue;
            final String[] args = clientCmd.getArgs();
            final int positionInt;
            final int valueInt;
            try {
                positionInt = Integer.parseInt(args[0]);
                valueInt = Integer.parseInt(args[1]);
                if (positionInt > MAX_BIT || positionInt < 0) {
                    return ("-ERR bit offset is not an integer or out of range\r\n");
                }
            } catch (NumberFormatException ignore) {
                return ("-ERR bit offset is not an integer or out of range\r\n");
            }

            //setbit key 2 1
            byte[] value = byteRedisValue.getValue();
            if (null == value) {
                value = new byte[MAX_BIT];
                Tools.setBit(positionInt, valueInt, value);
                // update bytes
                byteRedisValue.setValue(value);
                clientCmd.getDb().put(clientCmd.getKey(), byteRedisValue);
                return (":" + 0 + "\r\n");
            } else {
                final int lastBit = Tools.getBit(positionInt, value);
                byte[] value2 = Tools.setBit(positionInt, valueInt, value);
                // update bytes
                byteRedisValue.setValue(value2);
                clientCmd.getDb().put(clientCmd.getKey(), byteRedisValue);
                return (":" + lastBit + "\r\n");
            }

        }
    }

}
