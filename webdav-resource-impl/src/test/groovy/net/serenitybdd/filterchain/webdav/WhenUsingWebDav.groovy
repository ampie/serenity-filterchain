package net.serenitybdd.filterchain.webdav

import spock.lang.Specification

class WhenUsingWebDav extends Specification {
    def 'should construct all folders when writing webdav resources'() {
        given:
        def uid = UUID.randomUUID().toString()
        def root = new WebDavRoot('root', new WebDavConfig('http://localhost:32123/webdav/', 'test', 'test'))
//        root.sardine.delete('http://localhost:32123/webdav/')
        def newDir = root.resolvePotentialContainer(uid)
        root.resolvePotential(uid + '/dir/myfile.txt').write('hello webdav'.bytes)
        when:
        def list = newDir.list()
        then:
        list.length == 1
        list[0].name == 'dir'
    }
    def 'should support webdav resources with encoded names'() {
        given:
        def uid = UUID.randomUUID().toString()
        def root = new WebDavRoot('root', new WebDavConfig('http://localhost:32123/webdav/', 'test', 'test'))
        def newDir = root.resolvePotentialContainer(uid)
        root.resolvePotential(uid + '/some dir/my # file.txt').write('hello webdav'.bytes)
        when:
        def list = newDir.list()
        then:
        list.length == 1
        list[0].name == 'some dir'
    }

    def 'should read webdav resources'() {
        given:
        def uid = UUID.randomUUID().toString()
        def root = new WebDavRoot('root', new WebDavConfig('http://localhost:32123/webdav/', 'test', 'test'))
        def newDir = root.resolvePotentialContainer(uid)
        root.resolvePotential(uid + '/dir/myfile.txt').write('hello webdav'.bytes)

        when:
        def existing = root.resolveExisting(uid + '/dir/myfile.txt')
        then:
        new String(existing.read()) == 'hello webdav'
    }
}
