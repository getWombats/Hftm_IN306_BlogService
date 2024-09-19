<a name="apidoc-top"></a>

### [◄ Go back to the Readme](../README.md)

# API Documentation

When the project is running, the documentation can be seen via the  Swagger UI. **The port differs on the profile you run the project:**

### Dev-Profile:

Quarkus URL: http://localhost:8080/q/swagger-ui/<br>
Keycloak URL: http://localhost:8088/<br>

### Production-Profile:
Quarkus URL: http://localhost:9090/q/swagger-ui/<br>
Keycloak URL: http://keycloak:8180/<br>

---

### Get a Token

Default Content-Type: <b>application/json</b> (consumes and produces)<br>
For all requests you will need an access token provided by a keycloak instance. Make sure you request the token on the correct keycloak url **(dev profile url / prod profile url)**<br>
Edit the URL in the command depending on the profile you start the quarkus server:

* **Get the token for the demo user "alice" (roles: admin, user):**

    Command Prompt:
    ```sh
    curl -L -X POST 'http://localhost:8088/realms/blog/protocol/openid-connect/token' --data-urlencode 'client_id=backend-service' --data-urlencode 'client_secret=secret!' --data-urlencode 'grant_type=password' --data-urlencode 'username=alice' --data-urlencode 'password=alice'
    ```

    Or using HTTPie:
    ```sh
    http -v --form --auth backend-service:secret! POST http://localhost:8088/realms/blog/protocol/openid-connect/token username=alice password=alice grant_type=password
    ```

<br>

* **Get the token for the demo user "bob" (roles: user):**

    ```sh
    curl -L -X POST 'http://localhost:8088/realms/blog/protocol/openid-connect/token' --data-urlencode 'client_id=backend-service' --data-urlencode 'client_secret=secret!' --data-urlencode 'grant_type=password' --data-urlencode 'username=bob' --data-urlencode 'password=bobs_password'
    ```

    Or using HTTPie:
    ```sh
    http -v --form --auth backend-service:secret! POST http://localhost:8088/realms/blog/protocol/openid-connect/token username=bob password=bobs_password grant_type=password
    ```

---

## Available endpoints

<details>
<summary><b>Get all blog posts</b></summary>

* **Type:** GET
* **Path:** host:port/blogs
* **Constraints:** Authenticated
* **Response types:** `200 OK` `404 NOT FOUND` `500 ERROR`
* **Example response body:**
    ```json
    [
        {
            "id": 1,
            "title": "Blog post title",
            "content": "Awesome content",
            "author": "Spongebob Squarepants",
            "createdAt": "2022-03-10T16:15:50Z",
            "lastEditedAt": "2022-03-10T16:15:50Z",
            "comments": [
            {
                "id": 1,
                "blogId": 1,
                "commentNumber": 1,
                "content": "You live in an ananas...",
                "author": "Patrick Star",
                "createdAt": "2022-03-10T16:15:50Z",
                "lastEditedAt": "2022-03-10T16:15:50Z"
            }
            ]
        }
    ]
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v GET :8080/blogs
    ```

    **Using a filter:**
    ```sh
    http -v GET :8080/blogs searchString=="your search string"
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Save new blog post</b></summary>

* **Type:** POST
* **Path:** host:port/blogs/add
* **Constraints:** Authenticated
* **Response types:** `201 CREATED` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `409 CONFLICT` `500 ERROR`
* **Example request body:**
    Note that only the title and content has to be submitted. The rest is generated automatically.
    ```json
    {
        "title": "Super blog title",
        "content": "Even better content"
    }
    ```

* **Example response body:**
    ```json
    {
        "id": 1,
        "title": "Blog post title",
        "content": "Awesome content",
        "author": "Spongebob Squarepants",
        "createdAt": "2022-03-10T16:15:50Z",
        "lastEditedAt": null,
        "comments": null
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v POST :8080/blogs/add title="Your Blogtitle" content="Your Blogcontent" 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Delete all blog posts</b></summary>

* **Type:** DELETE
* **Path:** host:port/blogs/remove/all
* **Constraints:** Authenticated, admin role
* **Response types:** `204 NO CONTENT` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `500 ERROR`

* **Request with HTTPie CLI:**
    ```sh
    http -v DELETE :8080/blogs/remove/all 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Update a blog post</b></summary>

* **Type:** PATCH
* **Path:** host:port/blogs/update
* **Constraints:** Authenticated
* **Response types:** `200 OK` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `404 NOT FOUND` `500 ERROR`
* **Example request body:**
    ```json
    {
        "id": 1,
        "title": "My first blog post",
        "content": "This is my first updated blog post.",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": null,
        "comments": null
    }
    ```

* **Example response body:**
    ```json
    {
        "id": 1,
        "title": "Updated blog title",
        "content": "Updated blog content",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": "2024-08-25T15:18:29.610083Z",
        "comments": null
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v PATCH :8080/blogs/update id=1 title="Updated blog title" content="Updated blog content" 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Get a blog post by id</b></summary>

* **Type:** GET
* **Path:** host:port/blogs/{blogId}
* **Constraints:** Authenticated
* **Response types:** `200 OK` `404 NOT FOUND` `500 ERROR`
* **Example response body:**
    ```json
    {
        "id": 1,
        "title": "Blog title",
        "content": "Blog content",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": "2024-08-25T15:18:29.610083Z",
        "comments": null
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v GET :8080/blogs/1
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Delete a blog post by id</b></summary>

* **Type:** DELETE
* **Path:** host:port/blogs/remove/{blogId}
* **Constraints:** Authenticated
* **Response types:** `204 NO CONTENT` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `404 NOT FOUND` `500 ERROR`
* **Request with HTTPie CLI:**
    ```sh
    http -v DELETE :8080/blogs/remove/1 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Get all comments of a blog post</b></summary>

* **Type:** GET
* **Path:** host:port/blogs/{blogId}/comments
* **Constraints:** Authenticated
* **Response types:** `200 OK` `404 NOT FOUND` `500 ERROR`
* **Example response body:**
    ```json
    [
        {
            "id": 1,
            "blogId": 1,
            "commentNumber": 1,
            "content": "This is a nonsense comment",
            "author": "Sheldon Plankton",
            "createdAt": "2022-03-10T16:15:50Z",
            "lastEditedAt": "2022-03-10T16:15:50Z"
        }
    ]
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v GET :8080/blogs/1/comments
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Add a new comment to a blog post</b></summary>

* **Type:** POST
* **Path:** host:port/blogs/{blogId}/comments/add
* **Constraints:** Authenticated
* **Response types:** `201 CREATED` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `409 CONFLICT` `500 ERROR`
* **Example request body:**
    Note that only the content has to be submitted. The rest is generated automatically.
    ```json
    {
        "content": "This is an example comment."
    }
    ```

* **Example response body:**
    ```json
    {
        "id": 1,
        "blogId": 1,
        "commentNumber": 1,
        "content": "This is an example comment.",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": null
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v POST :8080/blogs/1/comments/add content="Your Blogcontent" 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Update a comment from a blog post</b></summary>

* **Type:** PATCH
* **Path:** host:port/blogs/{blogId}/comments/update
* **Constraints:** Authenticated
* **Response types:** `200 OK` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `404 NOT FOUND` `500 ERROR`
* **Example request body:**
    ```json
    {
        "id": 1,
        "blogId": 1,
        "commentNumber": 1,
        "content": "This is an example comment.",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": null
    }
    ```

* **Example response body:**
    ```json
    {
        "id": 1,
        "blogId": 1,
        "commentNumber": 1,
        "content": "Updated comment example.",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": "2024-08-25T15:18:29.610083Z"
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v PATCH :8080/blogs/1/comments/update id=1 content="Updated comment example." 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Get a comment from a blog post by comment id</b></summary>

* **Type:** GET
* **Path:** host:port/blogs/{blogId}/comments/{commentId}
* **Constraints:** Authenticated
* **Response types:** `200 OK` `404 NOT FOUND` `500 ERROR`
* **Example response body:**
    ```json
    {
        "id": 1,
        "blogId": 1,
        "commentNumber": 1,
        "content": "This is an example comment.",
        "createdAt": "2024-08-25T15:18:29.610083Z",
        "lastEditedAt": null
    }
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v GET :8080/blogs/1/comments/1
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Delete a comment from a blog post by comment id</b></summary>

* **Type:** DELETE
* **Path:** host:port/blogs/{blogId}/comments/remove/{commentId}
* **Constraints:** Authenticated
* **Response types:** `204 NO CONTENT` `401 NOT AUTHORIZED` `403 NOT ALLOWED` `404 NOT FOUND` `500 ERROR`
* **Request with HTTPie CLI:**
    ```sh
    http -v DELETE :8080/blogs/1/comments/remove/1 'Authorization: Bearer {token}'
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

<details>
<summary><b>Get all comments</b></summary>

* **Type:** GET
* **Path:** host:port/comments
* **Constraints:** Authenticated
* **Response types:** `200 OK` `404 NOT FOUND` `500 ERROR`
* **Example response body:**
    ```json
    [
        {
            "id": 1,
            "blogId": 1,
            "commentNumber": 1,
            "content": "This is an example comment.",
            "createdAt": "2024-08-25T15:18:29.610083Z",
            "lastEditedAt": null
        }
    ]
    ```

* **Request with HTTPie CLI:**
    ```sh
    http -v GET :8080/comments
    ```

    **Using a filter:**
    ```sh
    http -v GET :8080/comments searchString=="your search string"
    ```
</details>

<!------------------------------------------------------------------------------------------------------------------>

---

## Error responses

Nobody is perfect and sometimes a wild error appears!
Lucky that we are not getting an empty error response :)

Example error response body:
```json
{
    "traceId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
    "error": "Error name",
    "message:": "Problem description"
}
```

---

<p align="right">(<a href="#apidoc-top">back to top</a>)</p>