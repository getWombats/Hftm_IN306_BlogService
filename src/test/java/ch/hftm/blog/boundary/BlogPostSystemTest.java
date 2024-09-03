package ch.hftm.blog.boundary;

import java.io.StringReader;

import org.jboss.resteasy.reactive.RestResponse.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import ch.hftm.blog.control.BlogPostService;

import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestSecurity(authorizationEnabled = false)
public class BlogPostSystemTest {
    private static final String NEW_BLOG_TITLE = "Ich und mein Quarkus";
    private static final String NEW_BLOG_CONTENT = "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist.";
    private static final String BLOGS_PATH = "/blogs/";

    private int initialBlogsCount;
    private long newBlogId;
    private long newCommentId;

    @Inject
    BlogPostService blogService;

    @Test
    @Order(1)
    void getIntitialBlogsCount() {

        RestAssured
                .when()
                .get(BLOGS_PATH)
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode())
                .extract().body().asString();

        initialBlogsCount = 0;
    }

    @Test
    @Order(2)
    void addBlogItem() {
        String newBlogJson = """
                {
                    "title": "%s",
                    "content": "%s"
                }
                """.formatted(NEW_BLOG_TITLE, NEW_BLOG_CONTENT);

        String location = RestAssured
                .given()
                .body(newBlogJson)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(BLOGS_PATH)
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .extract().header("Location");

        String[] segments = location.split("/");
        newBlogId = Long.parseLong(segments[segments.length - 1]);
        System.out.println("newBlogId: " + newBlogId);
        Assertions.assertTrue(newBlogId == 1);
    }

    @Test
    @Order(3)
    void countBlogItems() {
        String responseBody = RestAssured
                .when()
                .get(BLOGS_PATH)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().body().asString();

        JsonArray jsonBlogArray = Json.createReader(new StringReader(responseBody)).readArray();
        Assertions.assertEquals(initialBlogsCount + 1, jsonBlogArray.size());
    }

    @Test
    @Order(4)
    void readNewBlog() {
        String responseBody = RestAssured
                .when()
                .get(BLOGS_PATH + newBlogId)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().body().asString();

        JsonObject jsonBlogObject = Json.createReader(new StringReader(responseBody)).readObject();

        Assertions.assertEquals(NEW_BLOG_TITLE, jsonBlogObject.getString("title"));
        Assertions.assertEquals(NEW_BLOG_CONTENT, jsonBlogObject.getString("content"));
    }

    @Test
    @Order(5)
    void updateNewBlog() {
        String updatedBlogJson = """
                {
                    "id": %s,
                    "title": "%s",
                    "content": "%s"
                }
                """.formatted(newBlogId, NEW_BLOG_TITLE + " Updated", NEW_BLOG_CONTENT + " Updated");

        String responseBody = RestAssured
                .given()
                .body(updatedBlogJson)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .patch(BLOGS_PATH)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().body().asString();

        JsonObject jsonBlogObject = Json.createReader(new StringReader(responseBody)).readObject();

        Assertions.assertEquals(NEW_BLOG_TITLE + " Updated", jsonBlogObject.getString("title"));
        Assertions.assertEquals(NEW_BLOG_CONTENT + " Updated", jsonBlogObject.getString("content"));
    }

    @Test
    @Order(6)
    void addCommentToBlog() {
        String newCommentJson = """
                {
                    "content": "This is a comment"
                }
                """;

        String location = RestAssured
                .given()
                .body(newCommentJson)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(BLOGS_PATH + newBlogId + "/comments")
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .extract().header("Location");

        String[] segments = location.split("/");
        newCommentId = Long.parseLong(segments[segments.length - 1]);
        Assertions.assertTrue(newCommentId == 1);
    }

    @Test
    @Order(7)
    void countComments() {
        String responseBody = RestAssured
                .when()
                .get(BLOGS_PATH + newBlogId + "/comments")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().body().asString();

        JsonArray commentArray = Json.createReader(new StringReader(responseBody)).readArray();
        Assertions.assertEquals(1, commentArray.size());
    }

    @Test
    @Order(8)
    void updateComment() {
        String updatedCommentJson = """
                {
                    "id": %s,
                    "content": "This is an updated comment"
                }
                """.formatted(newCommentId);

        String responseBody = RestAssured
                .given()
                .body(updatedCommentJson)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .patch(BLOGS_PATH + newBlogId + "/comments")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .extract().body().asString();

        JsonObject jsonCommentObject = Json.createReader(new StringReader(responseBody)).readObject();

        Assertions.assertEquals("This is an updated comment", jsonCommentObject.getString("content"));
    }

    @Test
    @Order(9)
    void deleteComment() {
        RestAssured
                .given()
                .when()
                .delete(BLOGS_PATH + newBlogId + "/comments/" + newCommentId)
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(10)
    void deleteBlog() {
        RestAssured
                .given()
                .when()
                .delete(BLOGS_PATH + newBlogId)
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());
    }
}
