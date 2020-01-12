package org.github.jrbase;

import org.github.jrbase.proxyRheakv.rheakv.Server1;
import org.github.jrbase.proxyRheakv.rheakv.Server2;
import org.github.jrbase.proxyRheakv.rheakv.Server3;

public class StartKvServers {
    public static void main(String[] args) {
        Server1.main(null);
        Server2.main(null);
        Server3.main(null);
    }
}
