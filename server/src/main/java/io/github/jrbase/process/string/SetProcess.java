package io.github.jrbase.process.string;

import io.github.jrbase.dataType.ClientCmd;
import io.github.jrbase.dataType.Cmd;
import io.github.jrbase.database.RedisValue;
import io.github.jrbase.database.StringRedisValue;
import io.github.jrbase.process.CmdProcess;
import io.github.jrbase.process.annotation.KeyCommand;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import static io.github.jrbase.dataType.CommonMessage.*;

@KeyCommand
public class SetProcess implements CmdProcess {

    @Override
    public String getCmdName() {
        return Cmd.SET.getCmdName();
    }

    @Override
    public boolean isCorrectArguments(ClientCmd clientCmd) {
        return clientCmd.getArgLength() >= 1 && clientCmd.getArgLength() <= 4;
    }

    @Override
    public String process(ClientCmd clientCmd) {
        return requestKVAndReplyClient(clientCmd);
    }


    public String requestKVAndReplyClient(ClientCmd clientCmd) {
        synchronized (this) {
            final RedisValue redisValue = clientCmd.getDb().get(clientCmd.getKey());
            final String[] args = clientCmd.getArgs();
            if (redisValue == null) {
                return handleAddKey(clientCmd, args);
            }
            return handleUpdateKey(clientCmd, redisValue, args);
        }
    }

    @NotNull
    private String handleUpdateKey(ClientCmd clientCmd, RedisValue redisValue, String[] args) {
        if (args.length > 1) {
            final Argument argument;
            try {
                argument = getArgument(args);
                if (argument.isHasNX()) {
                    return REDIS_EMPTY_STRING;
                }
            } catch (IntegerOutOfRangeException e) {
                return REDIS_ERROR_INTEGER_OUT_RANGE;
            } catch (SyntaxErrorException e) {
                return REDIS_ERROR_SYNTAX;
            }
            updateRedisValue(argument, redisValue);
        }
        ((StringRedisValue) redisValue).setValue(clientCmd.getArgs()[0]);
        clientCmd.getDb().put(clientCmd.getKey(), redisValue);
        return REDIS_ZORE_INTEGER;
    }

    @NotNull
    private String handleAddKey(ClientCmd clientCmd, String[] args) {
        final StringRedisValue addStringRedisValue = new StringRedisValue();
        if (args.length > 1) {
            final Argument argument;
            try {
                argument = getArgument(args);
                if (argument.isHasXX()) {
                    return REDIS_EMPTY_STRING;
                }
            } catch (IntegerOutOfRangeException e) {
                return REDIS_ERROR_INTEGER_OUT_RANGE;
            } catch (SyntaxErrorException e) {
                return REDIS_ERROR_SYNTAX;
            }
            updateRedisValue(argument, addStringRedisValue);

        }
        //                args0  1    2
        // check set key value ex|px nx|xx
        addStringRedisValue.setValue(clientCmd.getArgs()[0]);
        clientCmd.getDb().put(clientCmd.getKey(), addStringRedisValue);
        return REDIS_ONE_INTEGER;
    }

    private Argument getArgument(String[] args) throws IntegerOutOfRangeException, SyntaxErrorException {
        int index = 1;
        final Argument argument = new Argument();
        while (index < args.length) {
            final String arg = args[index];
            if ("ex".equals(arg) && index + 1 < args.length) {
                final String second = args[index + 1];
                if (!StringUtils.isNumeric(second)) {
                    throw new IntegerOutOfRangeException();
                }
                argument.setHasEX(true);
                argument.setHasPX(false);
                argument.setSecond(1000 * Integer.parseInt(second));
                index = index + 2;
            } else if ("px".equals(arg) && index + 1 < args.length) {
                final String ms = args[index + 1];
                if (!StringUtils.isNumeric(ms)) {
                    throw new IntegerOutOfRangeException();
                }
                argument.setHasPX(true);
                argument.setHasEX(false);
                argument.setMs(Integer.parseInt(ms));
                index = index + 2;
            } else if ("nx".equals(arg)) {
                argument.setHasNX(true);
                argument.setHasXX(false);
                index++;
            } else if ("xx".equals(arg)) {
                argument.setHasXX(true);
                argument.setHasNX(false);
                index++;
            } else {
                throw new SyntaxErrorException();
            }
        }
        return argument;
    }

    private void updateRedisValue(Argument argument, RedisValue addStringRedisValue) {
        if (argument.isHasEX()) {
            addStringRedisValue.setExpire(argument.getSecond() * 1000);
        } else if (argument.isHasPX()) {
            addStringRedisValue.setExpire(argument.getMs());
        }
    }

    private static class IntegerOutOfRangeException extends Throwable {
    }

    private static class SyntaxErrorException extends Throwable {

    }
}

class Argument {
    //second
    private boolean hasEX;
    private int second;
    //millisecond
    private boolean hasPX;
    private int ms;
    //not exists key
    private boolean hasNX;
    //exists key
    private boolean hasXX;

    public boolean isHasEX() {
        return hasEX;
    }

    public void setHasEX(boolean hasEX) {
        this.hasEX = hasEX;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public boolean isHasPX() {
        return hasPX;
    }

    public void setHasPX(boolean hasPX) {
        this.hasPX = hasPX;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    public boolean isHasNX() {
        return hasNX;
    }

    public void setHasNX(boolean hasNX) {
        this.hasNX = hasNX;
    }

    public boolean isHasXX() {
        return hasXX;
    }

    public void setHasXX(boolean hasXX) {
        this.hasXX = hasXX;
    }
}
