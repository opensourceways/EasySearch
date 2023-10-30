FROM openeuler/openeuler:22.03 as BUILDER

RUN cd / \
    && yum install -y wget \
    && wget https://download.oracle.com/java/17/archive/jdk-17.0.7_linux-x64_bin.tar.gz \
    && tar -zxvf jdk-17.0.7_linux-x64_bin.tar.gz \
    && wget https://repo.huaweicloud.com/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
    && tar -zxvf apache-maven-3.8.1-bin.tar.gz \
    && yum install -y git

COPY . /EaseSearch-search

ENV JAVA_HOME=/jdk-17.0.7
ENV PATH=${JAVA_HOME}/bin:$PATH

ENV MAVEN_HOME=/apache-maven-3.8.1
ENV PATH=${MAVEN_HOME}/bin:$PATH

RUN cd /EaseSearch-search \
    && mvn clean install package -Dmaven.test.skip

RUN cp -r jdk-17.0.7 jre


FROM openeuler/openeuler:22.03

RUN groupadd -g 1001 easysearch \
    && useradd -u 1001 -g easysearch -s /bin/bash -m easysearch

ENV WORKSPACE=/home/easysearch

WORKDIR ${WORKSPACE}

COPY --chown=easysearch --from=Builder /EaseSearch-search/target ${WORKSPACE}/target
COPY --chown=easysearch --from=Builder /jre ${WORKSPACE}/jre

ENV JAVA_HOME=${WORKSPACE}/jre
ENV PATH=${JAVA_HOME}/bin:$PATH
ENV LANG="C.UTF-8"
ENV NO_ID_USER=anonymous

EXPOSE 8080

USER easysearch

CMD java -jar ${WORKSPACE}/target/EaseSearch-search-0.0.1-SNAPSHOT.jar --spring.config.location=${APPLICATION_PATH}