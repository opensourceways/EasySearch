FROM openeuler/openeuler:22.03 as BUILDER

RUN cd / \
    && yum install -y wget \
    && wget https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jdk/x64/linux/OpenJDK17U-jdk_x64_linux_hotspot_17.0.9_9.tar.gz \
    && tar -zxvf OpenJDK17U-jdk_x64_linux_hotspot_17.0.9_9.tar.gz \
    && wget https://repo.huaweicloud.com/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz \
    && tar -zxvf apache-maven-3.8.1-bin.tar.gz \
    && yum install -y git \
    && yum install -y shadow

COPY . /EaseSearch-search

ENV JAVA_HOME=/jdk-17.0.9+9
ENV PATH=${JAVA_HOME}/bin:$PATH

ENV MAVEN_HOME=/apache-maven-3.8.1
ENV PATH=${MAVEN_HOME}/bin:$PATH

RUN cd /EaseSearch-search \
    && mvn clean install package -Dmaven.test.skip

RUN cp -r jdk-17.0.9+9 jre


FROM openeuler/openeuler:22.03

RUN groupadd -g 1001 easysearch \
    && useradd -u 1001 -g easysearch -s /bin/bash -m easysearch

ENV WORKSPACE=/home/easysearch

WORKDIR ${WORKSPACE}

COPY --chown=easysearch --from=Builder /EaseSearch-search/target ${WORKSPACE}/target

RUN echo "umask 027" >> /home/easysearch/.bashrc
RUN source /home/easysearch/.bashrc
RUN chmod 550 -R /home/easysearch

RUN dnf install -y wget \
    && wget https://mirrors.tuna.tsinghua.edu.cn/Adoptium/17/jre/x64/linux/OpenJDK17U-jre_x64_linux_hotspot_17.0.9_9.tar.gz -O jre-17.0.9.tar.gz \
    && tar -zxvf jre-17.0.9.tar.gz

ENV JAVA_HOME=${WORKSPACE}/jre-17.0.9
ENV PATH=${JAVA_HOME}/bin:$PATH
ENV LANG="C.UTF-8"
ENV NO_ID_USER=anonymous

EXPOSE 8080

USER easysearch

CMD java -jar ${WORKSPACE}/target/EaseSearch-0.0.1-SNAPSHOT.jar --spring.config.location=${APPLICATION_PATH}