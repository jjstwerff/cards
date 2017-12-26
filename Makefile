all: 
		mvn install
clean:
		mvn clean
		rm -rf */.classpath */.project */.settings
test:
		mvn test
config:
		mvn -DdownloadSources=true -DdownloadJavadocs=false eclipse:eclipse
javadoc:
		mvn -Dmaven.test.skip=true -Dskip.pull package
fast:
		mvn -Dmaven.test.skip=true -Dskip.pull install

release:
		mvn release:clean release:prepare release:clean

autorelease:
		mvn --batch-mode release:clean release:prepare release:clean
