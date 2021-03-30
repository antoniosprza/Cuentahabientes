FROM openjdk:8-alpine
WORKDIR /home/gonet
RUN mkdir /home/gonet/logs
COPY src/main/BD/cuentas.log /home/gonet/logs
COPY src/main/BD/cuentas.log.lck /home/gonet/logs
COPY src/main/BD/externo.log /home/gonet/logs
ADD target/cuentahabientes-0.0.1-SNAPSHOT.jar /usr/share/app.jar
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/app.jar"]