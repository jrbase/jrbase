package io.github.jrbase.thread

import spock.lang.Specification

class MyThreadFactoryTest extends Specification {
    def "NewThread"() {
        def myThreadFactory = new MyThreadFactory()
        when:
        Thread t1 = myThreadFactory.newThread(new Runnable() {
            @Override
            void run() {

            }
        })
        then:
        t1.getName() == 'JRBase-handler-1-thread-1'
        when:
        Thread t2 = myThreadFactory.newThread(new Runnable() {
            @Override
            void run() {

            }
        })
        then:
        t2.getName() == 'JRBase-handler-1-thread-2'
    }
}
