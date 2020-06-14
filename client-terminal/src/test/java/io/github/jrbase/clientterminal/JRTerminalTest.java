package io.github.jrbase.clientterminal;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JRTerminalTest {
    @Test
    public void testArgs() {

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
        try {
            String[] args = new String[]{"-h", "192.168.100.1", "-p", "6378"};
            ns = parser.parseArgs(args);
            String host = ns.get("host");
            Assert.assertEquals("192.168.100.1", host);
            String port = ns.get("port");
            Assert.assertEquals("6378", port);

        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

}
