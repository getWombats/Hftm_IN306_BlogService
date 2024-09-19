### [◄ Go back to the Readme](../README.md)

# Run the app with docker compose

Starting all containers with docker compose means the app will be started with production profile. There is no dev profile since the image for the quarkus project is pulled from github container registry.

## Run docker compose file

The docker compose file is located at the project's root (where the poml.xml is located). Open a terminal window in root and run following command:

```sh
docker compose up
```

All images will be pulled and the containers are started automatically in following order:

1. MySQL database for the quarkus project
2. MySQL database for keycloak authentication service
3. Keycloak authentication service (on http://keycloak:8180)
4. Blog rest api project (base url: http://localhost:9090)

The endpoints are described in the api documentation in this readme.

Note that the containers take quite a while to start. Each container waits for the successul startup of the previous container to make sure, every service is available.

## Adjust timings

If timings are bad, they can be adjusted in the docker-compose.yaml file. Adjust interval or timout to your needs.

Example of the keycloak health check:

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8180"]
  interval: 20s
  timeout: 10s
  retries: 10
```

## Trouble shoot

Running everything via docker compose doesnt always go according to plan. Especially the import of the blog-realm.json sometimes fails. In this case try the [manual startup procedure](RunTheAppManually.md) of the app.