package io.github.jrbase.clientterminal;

import io.github.jrbase.common.datatype.Cmd;
import org.jline.reader.Completer;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.NullCompleter;
import org.jline.reader.impl.completer.StringsCompleter;

import java.util.ArrayList;
import java.util.List;

public class CompleterFactory {
    // complete Factory
    static Completer getCompleter() {

        List<Completer> completerList = new ArrayList<>();

        for (String keyName : Cmd.getLookup().keySet()) {
            completerList.add(new ArgumentCompleter(
                    new StringsCompleter(keyName),
                    NullCompleter.INSTANCE
            ));
        }

        return new AggregateCompleter(
                completerList
        );
    }
}
