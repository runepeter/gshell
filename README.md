Description
-----------

GShell - A command-line shell framework.

License
-------

[Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

Building
--------

### Requirements

* Maven 2+
* Java 5+

Check out and build:

    git clone git://github.com/sonatype/gshell.git
    cd gshell
    mvn install

After this completes, you can unzip the assembly and launch the shell:

    unzip gshell-assembly/target/gshell-*-bin.zip
    ./gshell-*/bin/gsh
