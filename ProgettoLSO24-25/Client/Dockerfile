FROM gradle:8.7-jdk17 AS builder

WORKDIR /home/gradle/project
USER root

RUN apt-get update && apt-get install -y --no-install-recommends unzip wget && \
    rm -rf /var/lib/apt/lists/*

ARG ANDROID_SDK_ROOT="/opt/android-sdk"
ENV ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT}" \
    PATH="${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools"

RUN wget -q https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip -O /tmp/cmdline-tools.zip && \
    mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    unzip -q /tmp/cmdline-tools.zip -d "${ANDROID_SDK_ROOT}/cmdline-tools" && \
    mv "${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools" "${ANDROID_SDK_ROOT}/cmdline-tools/latest" && \
    rm /tmp/cmdline-tools.zip && \
    yes | sdkmanager --sdk_root="${ANDROID_SDK_ROOT}" --licenses >/dev/null && \
    sdkmanager --sdk_root="${ANDROID_SDK_ROOT}" \
        "platform-tools" "platforms;android-29" "build-tools;29.0.3"

COPY . .
RUN gradle assembleDebug --no-daemon

FROM eclipse-temurin:17-jdk-jammy

RUN apt-get update && apt-get install -y --no-install-recommends adb && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY --from=builder /home/gradle/project/app/build/outputs/apk/debug/app-debug.apk .
