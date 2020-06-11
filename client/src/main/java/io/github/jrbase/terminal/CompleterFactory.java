package io.github.jrbase.terminal;

import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

public class CompleterFactory {
    // complete Factory
    static Completer getCompleter() {
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
                NullCompleter.INSTANCE
        );

        return new AggregateCompleter(
                createCompleter,
                openCompleter,
                writeCompleter
        );
    }
}
