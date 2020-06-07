package io.github.jrbase.common

import spock.lang.Specification
import io.github.jrbase.common.config.YamlTool

class YamlToolTest extends Specification {
    def "ReadConfig"() {
        when:
        def redisConfigurationOption = YamlTool.readConfig("config/redis_server.yaml")
        then:
        redisConfigurationOption.getBind() == '127.0.0.1'
        redisConfigurationOption.getPort() == 6379
    }

    def "ReadConfigErrorFile"() {
        when:
        YamlTool.readConfig("config/unknownFile.yaml")
        then:
        RuntimeException e = thrown()
        e.message.contains("No such file or directory")

    }

}
