package io.github.jrbase.clientterminal;

import io.github.jrbase.client.RedisClient;
import io.github.jrbase.client.response.TypeResponse;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

import static io.github.jrbase.client.response.ResponseFactory.selectTypeResponse;

public class JRTerminal {
    //TODO: auth
    public static void main(String[] args) throws IOException {
        // parse args
        ArgumentParser parser = ArgumentParsers
                .newFor("redis-client.sh")
                .addHelp(false)
                .build()
                .description("Redis client");

        parser.addArgument("-h", "--host")
                .setDefault("localhost")
                .help("Redis server host");
        parser.addArgument("-p", "--port")
                .setDefault("6379")
                .help("Redis server port");

        Namespace ns;
        String host = "127.0.0.1";
        String port = "6379";
        try {
            ns = parser.parseArgs(args);
            host = ns.get("host");
            port = ns.get("port");
        } catch (
                ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        final RedisClient redisClient = RedisClient.builder()
                .host(host)
                .port(Integer.parseInt(port));

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        Completer fogCompleter = CompleterFactory.getCompleter();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(fogCompleter)
                .history(new JRHistory())
                .build();

        String prompt;
        while (true) {
            String line;
            try {
                if (redisClient.isActive()) {
                    prompt = "JRBase> ";
                } else {
                    prompt = "not connected> ";
                }
                line = lineReader.readLine(prompt);
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if ("quit".equals(line)) {
                    break;
                }
                if (!redisClient.isActive()) {
                    redisClient.connect();
                    if (!redisClient.isActive()) {
                        System.out.printf("Could not connect to Redis at %s:%s: Connection refused\n", redisClient.getHost(), redisClient.getPort());
                        continue;
                    }
                }
                String msg = redisClient.sendMessageAndReceive(line);
                handleMessage(msg);

            } catch (UserInterruptException e) {
                // Do nothing
            } catch (EndOfFileException e) {
                return;
            }
        }
    }


    private static void handleMessage(String msg) {
        if (msg.isEmpty()) {
            return;
        }
        TypeResponse response = selectTypeResponse(msg);
        String handle = response.handle(msg);
        System.out.println(handle);
    }


}
