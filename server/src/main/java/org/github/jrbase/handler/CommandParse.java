package org.github.jrbase.handler;

import org.github.jrbase.dataType.ClientCmd;
import org.jetbrains.annotations.NotNull;

public class CommandParse {
    /**
     * ClientCmd:      cmd           key            args
     * length:         1    2     3       4     5      6       7
     * index:          0    1     2       3     4      5       6
     * array:       ['*3', '$3', 'set', '$3', 'key', '$5', 'value']
     */
    public static ClientCmd parseMessageToClientCmd(final String[] redisClientCmdArr) {
        //TODO: ClientCmd Memory Pool Manage
        ClientCmd clientCmd = new ClientCmd();
        clientCmd.setCmd("");
        clientCmd.setKey("");
        clientCmd.setArgs(new String[0]);

        if (isHaveCmd(redisClientCmdArr.length)) {
            clientCmd.setCmd(redisClientCmdArr[2]);
            if (isHaveKey(redisClientCmdArr.length)) {
                clientCmd.setKey(redisClientCmdArr[4]);
                if (isHaveArgs(redisClientCmdArr.length)) {
                    final String[] args = getArgs(redisClientCmdArr);
                    clientCmd.setArgs(args);
                }
            }
        }
        return clientCmd;
    }

    @NotNull
    static String[] getArgs(final String[] redisClientCmdArr) {
        final int argsCount = (redisClientCmdArr.length + 1 - 6) / 2;
        String[] args = new String[argsCount];
        int count = 0;
        for (int i = 6; i < redisClientCmdArr.length; i = i + 2) {
            args[count++] = redisClientCmdArr[i];
        }
        return args;
    }

    private static boolean isHaveCmd(int length) {
        return length >= 3;
    }

    private static boolean isHaveKey(int length) {
        return length >= 5;
    }

    private static boolean isHaveArgs(int length) {
        return length >= 7;
    }
}
