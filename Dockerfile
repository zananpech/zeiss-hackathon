FROM openjdk:17-ea-jdk-buster as jdkbase-build 
FROM debian:11-slim

RUN apt-get update && apt-get install -y --no-install-recommends maven unzip

WORKDIR /usr/hackathon

RUN mkdir -p "/usr/local/openjdk-17"
COPY --from=jdkbase-build /usr/local/openjdk-17 /usr/local/openjdk-17

ENV JAVA_HOME=/usr/local/openjdk-17
ENV PATH=$JAVA_HOME/bin:$PATH

RUN echo Verifying copy in smbase-prod ... \
    && javac --version \
    && echo copy Java Complete.


RUN echo "#!/bin/bash\n \
if [[ \"\$(java -cp /usr/hackathon/target/Solution-1.0.0.jar hackathon.Solution 3 3 121301111)\" == \"121341111\" ]]; then echo \"Good solution\" ;else echo \"Wrong solution\"; fi" > /usr/hackathon/eval.sh && chmod +x /usr/hackathon/eval.sh

RUN echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n \
<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n \
    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n \
    <modelVersion>4.0.0</modelVersion>\n \
\n \
    <groupId>org.springframework</groupId>\n \
    <artifactId>Solution</artifactId>\n \
    <packaging>jar</packaging>\n \
    <version>1.0.0</version>\n \
\n \
    <properties>\n \
        <maven.compiler.source>1.8</maven.compiler.source>\n \
        <maven.compiler.target>1.8</maven.compiler.target>\n \
    </properties>\n \
\n \
    <build>\n \
        <plugins>\n \
        </plugins>\n \
    </build>\n \
</project>\n " > /usr/hackathon/pom.xml

RUN mkdir -p /usr/hackathon/src/main/java/hackathon/
RUN echo "package hackathon;\n \
\n \
public class Solution {\n \
	public static void main(String[] args) {\n \
		System.out.println(args[2]);\n \
	}\n \
}\n" > /usr/hackathon/src/main/java/hackathon/Solution.java

RUN mvn package
RUN rm -rf src
RUN rm -rf target

COPY ./src.zip /usr/hackathon/src.zip
RUN unzip src.zip

RUN mvn -o package

ENTRYPOINT ["/usr/hackathon/eval.sh"]