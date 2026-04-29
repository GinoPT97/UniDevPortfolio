FROM gradle:8.7-jdk17 AS builder

WORKDIR /home/gradle/project
USER root

RUN apt-get update && \
    apt-get install -y --no-install-recommends tzdata unzip wget && \
    ln -fs /usr/share/zoneinfo/Europe/Rome /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    rm -rf /var/lib/apt/lists/*

ARG ANDROID_SDK_ROOT="/opt/android-sdk"
ENV ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT}"
ENV PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"

RUN mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip /tmp/cmdline-tools.zip -d "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    mv "${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools" "${ANDROID_SDK_ROOT}/cmdline-tools/latest" && \
    yes | sdkmanager --sdk_root="${ANDROID_SDK_ROOT}" \
        "platform-tools" "platforms;android-29" "build-tools;29.0.3" && \
    rm -rf /tmp/cmdline-tools.zip

COPY gradle /home/gradle/project/gradle
COPY gradle.properties settings.gradle.kts /home/gradle/project/
COPY app/build.gradle.kts /home/gradle/project/app/

RUN gradle build --no-daemon --dry-run

COPY app/src /home/gradle/project/app/src

RUN gradle assembleDebug --no-daemon

FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends adb && rm -rf /var/lib/apt/lists/*
COPY --from=builder /home/gradle/project/app/build/outputs/apk/debug/app-debug.apk ./app-debug.apk
