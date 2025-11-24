

# Required env to work with Podman
export TESTCONTAINERS_RYUK_DISABLED=true
export DOCKER_HOST=unix://$XDG_RUNTIME_DIR/podman/podman.sock

# Test connection to podman
podman pull postgres:17
curl --unix-socket $DOCKER_HOST http://d/v4.0.0/libpod/info | grep linux

# Run Tests
#sudo -E mvn test
mvn test -Drun.integration.test=true