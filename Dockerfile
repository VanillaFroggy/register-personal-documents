FROM openjdk:21-oracle

LABEL authors="FrolovIV"

ENV JAVA_PTS="-Xmx256m"

ADD build/libs/register-personal-documents*all.jar app.jar

COPY entrypoint.sh entrypoint.sh

RUN apk add --no-cache tzdata; \
    cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime; \
    echo "Europe/Moscow" > /etc/timezone; \
    chmod 755 /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["./entrypoint.sh"]