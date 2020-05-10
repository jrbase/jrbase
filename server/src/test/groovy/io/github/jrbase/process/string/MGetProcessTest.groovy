package io.github.jrbase.process.string


import io.github.jrbase.dataType.ClientCmd
import io.github.jrbase.process.CmdProcess
import spock.lang.Specification

class MGetProcessTest extends Specification {
    CmdProcess cmdProcess = new MGetProcess()
    ClientCmd clientCmd = new ClientCmd()
    //get a = b
    def "Process"() {
        /* given:
         clientCmd.setKey("a")
         clientCmd.setArgs(args as String[])

         final BackendProxy backendProxy = Mock()
         clientCmd.setBackendProxy(backendProxy)

         List<byte[]> keyList = new ArrayList<>()
         keyList.add((clientCmd.getKey() + STRINGS.getAbbreviation()).getBytes())

         Map<ByteArray, byte[]> multiGetResult = new HashMap<>()
         multiGetResult.put(ByteArray.wrap((clientCmd.getKey() + STRINGS.getAbbreviation()).getBytes()),
                 clientCmd.getKey().getBytes())

         for (String arg : clientCmd.getArgs()) {
             keyList.add((arg + STRINGS.getAbbreviation()).getBytes())

             multiGetResult.put(ByteArray.wrap((arg + STRINGS.getAbbreviation()).getBytes()),
                     arg.getBytes())
         }

         backendProxy.bMultiGet(keyList) >> multiGetResult

         expect:
         message == cmdProcess.process(clientCmd)
         where:
         args  | message
         []    | '*1\r\n$1\r\na\r\n'
         ["b"] | '*2\r\n$1\r\na\r\n$1\r\nb\r\n'*/
//        [97]  | '$1\r\na\r\n'
    }

}
