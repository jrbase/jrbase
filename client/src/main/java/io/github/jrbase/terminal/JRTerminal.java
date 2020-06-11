package io.github.jrbase.terminal;

import io.github.jrbase.client.RedisClient;
import io.github.jrbase.client.response.TypeResponse;
import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

import static io.github.jrbase.client.response.ResponseFactory.newTypeResponse;

public class JRTerminal {


    public static void main(String[] args) throws IOException {

        //TODO: how to connect redis?
        final RedisClient redisClient = new RedisClient("192.168.100.1", 6379);


        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        Completer fogCompleter = CompleterFactory.getCompleter();

        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(fogCompleter)
                .history(new JRHistory())
                .build();

        String prompt = "JRBase> ";
        while (true) {
            String line;
            try {
                line = lineReader.readLine(prompt);
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String msg = redisClient.sendMessage(line);
                handleMessage(msg);

            } catch (UserInterruptException e) {
                // Do nothing
            } catch (EndOfFileException e) {
                return;
            }
        }
    }


    private static void handleMessage(String msg) {
        TypeResponse response = newTypeResponse(msg);
        response.handle(msg);
    }


}
