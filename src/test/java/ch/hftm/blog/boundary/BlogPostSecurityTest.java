package ch.hftm.blog.boundary;

import java.util.List;
import java.util.ArrayList;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.RestResponse.Status;
import org.junit.jupiter.api.*;

import ch.hftm.blog.control.BlogPostService;
import ch.hftm.blog.model.dto.BlogPostDTO;

import org.junit.jupiter.api.TestInstance.Lifecycle;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

@Disabled
@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlogPostSecurityTest {
    private static final KeycloakTestClient KEYCLOAK_CLIENT = new KeycloakTestClient();
    private static final String BLOGS_PATH = "/blogs/";
    private static final String CLIENT_ID = "backend-service";
    private static final String SCOPE_OPEN_ID = "openid";

    private static final String NEW_BLOG_TITLE = "Ich und mein Quarkus";
    private static final String NEW_BLOG_CONTENT = "Quarkus ist ein Framework, das auf Java basiert und für die Entwicklung von Cloud-nativen Anwendungen optimiert ist.";

    // Get the client secret from the application.properties file
    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @Inject
    BlogPostService blogService;

    @BeforeAll
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        blogService.addBlogPost(
                new BlogPostDTO(null,
                        "Mein erster Blog",
                        "miau miau miau",
                        "Kater Karlo",
                        null,
                        null,
                        null));
        }

    @Test
    @Order(1)
    void addBlogItemWithToken() {
        List<String> scopes = new ArrayList<>();
        scopes.add(SCOPE_OPEN_ID);

        String accessToken = KEYCLOAK_CLIENT.getAccessToken("alice", "alice", CLIENT_ID, clientSecret, scopes);

        String newBlogJson = """
                {
                    "title": "%s",
                    "content": "%s"
                }
                """.formatted(NEW_BLOG_TITLE, NEW_BLOG_CONTENT);

        RestAssured
                .given()
                .body(newBlogJson)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(BLOGS_PATH)
                .then().assertThat().statusCode(Status.CREATED.getStatusCode());
    }

    @Test
    @Order(2)
    void addBlogItemWithoutToken() {
        String newBlogJson = """
                {
                    "title": "%s",
                    "content": "%s"
                }
                """.formatted(NEW_BLOG_TITLE, NEW_BLOG_CONTENT);

        RestAssured
                .given()
                .body(newBlogJson)
                .header("Authorization", "Bearer ")
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .post(BLOGS_PATH)
                .then().assertThat().statusCode(Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    @Order(3)
    void deleteAllBlogPostsWithAdminRight() {
        List<String> scopes = new ArrayList<>();
        scopes.add(SCOPE_OPEN_ID);

        String accessToken = KEYCLOAK_CLIENT.getAccessToken("alice", "alice", CLIENT_ID, clientSecret, scopes);

        RestAssured
                .given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(BLOGS_PATH)
                .then().assertThat().statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(3)
    void deleteAllBlogPostsWithoutAdminRight() {
        List<String> scopes = new ArrayList<>();
        scopes.add(SCOPE_OPEN_ID);

        String accessToken = KEYCLOAK_CLIENT.getAccessToken("bob", "bobs_password", CLIENT_ID, clientSecret, scopes);

        RestAssured
                .given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .delete(BLOGS_PATH)
                .then().assertThat().statusCode(Status.FORBIDDEN.getStatusCode());
    }
}
