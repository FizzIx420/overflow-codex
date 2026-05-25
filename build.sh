#!/bin/bash
# ===========================================
# Overflow Codex - Build Script
# NeoForge 21.0.109 | MC 1.21.1 | Ars Nouveau 5.10.6
# ===========================================

set -e

echo "========================================"
echo "  Overflow Codex Build Script"
echo "  v1.0.0"
echo "========================================"
echo ""

# Check for Java 21
if ! java -version 2>&1 | grep -q "21"; then
    echo "ERROR: Java 21 is required. Found:"
    java -version 2>&1
    exit 1
fi

echo "[1/3] Granting Gradle execute permission..."
chmod +x gradlew

echo "[2/3] Building mod (this may take a few minutes on first run)..."
echo ""

# Build with Gradle
./gradlew build --no-daemon "$@"

BUILD_EXIT=$?

echo ""
if [ $BUILD_EXIT -eq 0 ]; then
    echo "========================================"
    echo "  BUILD SUCCESSFUL!"
    echo "========================================"
    echo ""
    echo "Output JAR location:"
    ls -la build/libs/*.jar 2>/dev/null
    echo ""
    echo "To install: Copy the JAR file to your"
    echo "  .minecraft/mods/ folder"
    echo ""
    echo "Required mods:"
    echo "  - NeoForge 21.0.109"
    echo "  - Ars Nouveau 5.10.6"
    echo "  - AllTheModium 3.0.1 (optional, for recipes)"
else
    echo "========================================"
    echo "  BUILD FAILED (exit code: $BUILD_EXIT)"
    echo "========================================"
    echo ""
    echo "Common issues:"
    echo "  - Network timeout: Re-run the script"
    echo "  - Ars Nouveau API mismatch: Check AN 5.10.6 compatibility"
    echo "  - JDK version: Ensure Java 21 is active"
    exit 1
fi
