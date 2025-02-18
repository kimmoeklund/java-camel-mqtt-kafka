#!/usr/bin/env bash
if ! command -v mvn 2>&1 >/dev/null
then
    echo "<mvn> not found in path"
    exit 1
fi

# Store PIDs to kill them later
declare -a pids

# Function to cleanup processes
cleanup() {
    echo "Stopping all processes..."
    for pid in "${pids[@]}"; do
        kill $pid 2>/dev/null
    done
    exit 0
}

# Set up trap for cleanup on script interruption
trap cleanup SIGINT SIGTERM

# Start first Maven process
mvn camel:run &
pids+=($!)

# Start second Maven process
mvn exec:java &
pids+=($!)

# Wait for log file to be created
while [ ! -f logs/application.log ]; do
    sleep 1
done

# Tail the log file
# The -f flag makes tail follow the file (output appended data as the file grows)
tail -f logs/application.log

# Note: The script will only reach here if tail is interrupted
cleanup

