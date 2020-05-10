package io.github.jrbase.terminal;

import org.jline.builtins.Completers;
import org.jline.reader.*;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JRTerminal {

    private static final List<String> fileVars = new ArrayList<>();
    private static final FileVarsCompleter fileVarsCompleter = new FileVarsCompleter();

    public static void main(String[] args) throws IOException {

        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        Completer createCompleter = new ArgumentCompleter(
                new StringsCompleter("CREATE"),
                new Completers.FileNameCompleter(),
                NullCompleter.INSTANCE
        );

        Completer openCompleter = new ArgumentCompleter(
                new StringsCompleter("OPEN"),
                new Completers.FileNameCompleter(),
                new StringsCompleter("AS"),
                NullCompleter.INSTANCE
        );

        Completer writeCompleter = new ArgumentCompleter(
                new StringsCompleter("WRITE"),
                new StringsCompleter("TIME", "DATE", "LOCATION"),
                new StringsCompleter("TO"),
                fileVarsCompleter,
                NullCompleter.INSTANCE
        );

        Completer fogCompleter = new AggregateCompleter(
                createCompleter,
                openCompleter,
                writeCompleter
        );

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
                if (line.startsWith("OPEN")) {
                    fileVars.add(line.split(" ")[3]);
                    fileVarsCompleter.setFileVars(fileVars);
                }

                System.out.println("sousfsd");
                // 交给客户端
                Thread.sleep(500);

            } catch (UserInterruptException e) {
                // Do nothing
            } catch (EndOfFileException e) {
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
