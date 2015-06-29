SRCDIR=/usa/siegel/shortestpath/trunk/src

JARHOME=/usr/local/hamr/lib

JARLIST=$(JARHOME)/activation-1.1.jar:$(JARHOME)/amqp-client-3.2.3.jar:$(JARHOME)/args4j-2.0.28.jar:$(JARHOME)/asm-3.1.jar:$(JARHOME)/avro-1.7.4.jar:$(JARHOME)/commons-beanutils-1.7.0.jar:$(JARHOME)/commons-beanutils-core-1.8.0.jar:$(JARHOME)/commons-cli-1.2.jar:$(JARHOME)/commons-codec-1.4.jar:$(JARHOME)/commons-collections-3.2.1.jar:$(JARHOME)/commons-compress-1.4.1.jar:$(JARHOME)/commons-configuration-1.6.jar:$(JARHOME)/commons-daemon-1.0.13.jar:$(JARHOME)/commons-digester-1.8.jar:$(JARHOME)/commons-el-1.0.jar:$(JARHOME)/commons-httpclient-3.1.jar:$(JARHOME)/commons-io-2.1.jar:$(JARHOME)/commons-lang-2.5.jar:$(JARHOME)/commons-logging-1.1.1.jar:$(JARHOME)/commons-math-2.1.jar:$(JARHOME)/commons-net-3.1.jar:$(JARHOME)/curator-client-2.4.0.jar:$(JARHOME)/curator-framework-2.4.0.jar:$(JARHOME)/curator-recipes-2.4.0.jar:$(JARHOME)/guava-16.0.jar:$(JARHOME)/hadoop-annotations-2.2.0.jar:$(JARHOME)/hadoop-auth-2.2.0.jar:$(JARHOME)/hadoop-common-2.2.0.jar:$(JARHOME)/hadoop-hdfs-2.2.0.jar:$(JARHOME)/hamr-0.2.1.jar:$(JARHOME)/hamr-benchmark-0.2.1.jar:$(JARHOME)/hamr-examples-0.2.1.jar:$(JARHOME)/hppc-0.6.0.jar:$(JARHOME)/jackson-core-asl-1.8.8.jar:$(JARHOME)/jackson-jaxrs-1.8.3.jar:$(JARHOME)/jackson-mapper-asl-1.8.8.jar:$(JARHOME)/jackson-xc-1.8.3.jar:$(JARHOME)/jasper-compiler-5.5.23.jar:$(JARHOME)/jasper-runtime-5.5.23.jar:$(JARHOME)/jaxb-api-2.2.2.jar:$(JARHOME)/jaxb-impl-2.2.3-1.jar:$(JARHOME)/jersey-core-1.9.jar:$(JARHOME)/jersey-json-1.9.jar:$(JARHOME)/jersey-server-1.9.jar:$(JARHOME)/jets3t-0.6.1.jar:$(JARHOME)/jettison-1.1.jar:$(JARHOME)/jetty-6.1.26.jar:$(JARHOME)/jetty-util-6.1.26.jar:$(JARHOME)/jline-0.9.94.jar:$(JARHOME)/jsch-0.1.42.jar:$(JARHOME)/jsp-api-2.1.jar:$(JARHOME)/junit-3.8.1.jar:$(JARHOME)/kafka_2.9.2-0.8.1.1.jar:$(JARHOME)/log4j-1.2.17.jar:$(JARHOME)/metrics-core-2.2.0.jar:$(JARHOME)/netty-3.2.2.Final.jar:$(JARHOME)/paranamer-2.3.jar:$(JARHOME)/protobuf-java-2.5.0.jar:$(JARHOME)/scala-library-2.9.2.jar:$(JARHOME)/servlet-api-2.5.jar:$(JARHOME)/slf4j-api-1.7.6.jar:$(JARHOME)/slf4j-log4j12-1.7.6.jar:$(JARHOME)/snappy-java-1.0.4.1.jar:$(JARHOME)/sorts-1.0-SNAPSHOT.jar:$(JARHOME)/stax-api-1.0.1.jar:$(JARHOME)/tools.jar:$(JARHOME)/xmlenc-0.52.jar:$(JARHOME)/xz-1.0.jar:$(JARHOME)/zkclient-0.3.jar:$(JARHOME)/zookeeper-3.4.5.jar

JAVAC=/usr/bin/javac

compile:
	$(JAVAC) -classpath $(SRCDIR):$(JARLIST) *.java

clean:
	rm -f *.class *~ hamrconfig* *.part*
