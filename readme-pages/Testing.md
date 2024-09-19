### [◄ Go back to the Readme](../README.md)

# Run quarkus unit and integration tests

**`Unit and integration tests are currently disabled due to unusual behavior of the tests`**

The tests can be started in two ways:

1. **Run tests explicitly**

    ```sh
    ./mvnw test
    ```

    When running the command, test containers are started. Make sure docker is running.

2. **Run tests within running devservices**

    When running the quarkus project with dev profile you can see a menu on the bottom of the terminal. You can press 'o' to toggle the test output and then re-run tests as you like by pressing 'r'. In this mode, every time you save changes, the tests are automatically re-run.


## This is what we all want to see :)

![Quarkus test screenshot](QuarkusTests.png)