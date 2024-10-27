FROM openjdk:8-jdk-alpine

RUN mkdir -p /var/log/xechat && \
    mkdir -p /xechat && \
    chmod 777 /var/log/xechat && \
    chmod 777 /xechat

COPY target/${finalName}.jar /app.jar

WORKDIR /

EXPOSE 8082

CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]  
