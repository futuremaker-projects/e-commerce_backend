#!/usr/bin/env bash
set -e

APP_HOME="/home/noah/workspace/ecommerce_backend"

JAR="${APP_HOME}/app.jar"
YML="${APP_HOME}/application.yml"
PID="${APP_HOME}/myapp/app.pid"
LOG="${APP_HOME}/logs/app.out"

if [ -f "$PID" ] && kill -0 "$(cat "$PID")" 2>/dev/null; then
  kill "$(cat "$PID")"
  sleep 2
  kill -0 "$(cat "$PID")" 2>/dev/null && kill -9 "$(cat "$PID")" || true
fi
rm -f "$PID"

nohup java -jar "$JAR" --spring.config.location="file:$YML" >> "$LOG" 2>&1 &
echo $! > "$PID"
echo "OK pid=$(cat "$PID")"
