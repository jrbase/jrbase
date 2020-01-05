package org.github.jrbase.config

import spock.lang.Specification

class YamlToolTest extends Specification {
    def "ReadConfig"() {
        when:
        def redisConfigurationOption = YamlTool.readConfig("config/redis_server.yaml")
        then:
        redisConfigurationOption.getBind() == '127.0.0.1'
        redisConfigurationOption.getPort() == 6379
    }
}
