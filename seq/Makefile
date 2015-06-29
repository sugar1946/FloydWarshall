
CLASSPATH=/usa/siegel/shortestpath/trunk/src
TEST=/usa/siegel/shortestpath/trunk/test/Test.txt
JAVAC=/usr/bin/javac
JAVA=/usr/bin/java

compile:
	$(JAVAC) -classpath $(CLASSPATH) *.java

run:
	$(JAVA) -classpath $(CLASSPATH) org.cisc475.shortestpath.seq.FloydWarshall $(TEST)

clean:
	rm -rf *.class *~
