FROM maven:3.2-jdk-8

RUN apt-get update
RUN apt-get install -y git

WORKDIR /usr/

RUN git clone http://ST.Pouwels%40student.han.nl:1993-02-10@git.icaprojecten.nl/stash/scm/asdn/protocol.git
WORKDIR /usr/protocol
RUN mvn install -Dmaven.test.skip=true

WORKDIR /usr/

RUN git clone http://ST.Pouwels%40student.han.nl:1993-02-10@git.icaprojecten.nl/stash/scm/asdn/commonservices.git
WORKDIR /usr/commonservices
RUN git checkout development
RUN mvn install -Dmaven.test.skip=true

RUN git clone http://ST.Pouwels%40student.han.nl:1993-02-10@git.icaprojecten.nl/stash/scm/asdn/scripting.git
WORKDIR /usr/scripting
RUN git checkout develop
RUN mvn install -Dmaven.test.skip=true

ADD . /usr/src/app

WORKDIR /usr/src/app
RUN mvn clean install
CMD mvn exec:java -Dexec.mainClass="nl.han.asd.project.client.commonclient.Main"