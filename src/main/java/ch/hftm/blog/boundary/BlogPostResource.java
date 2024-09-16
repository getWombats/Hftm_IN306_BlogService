package ch.hftm.blog.boundary;

import java.util.Optional;

import io.quarkus.security.Authenticated;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import ch.hftm.blog.control.BlogPostService;
import ch.hftm.blog.control.CommentService;
import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.dto.ErrorDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("blogs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogPostResource extends ResourceBase {
    @Inject
    BlogPostService blogService;

    @Inject
    CommentService commentService;

    @Inject
    JsonWebToken jwt;

    @GET
    @Authenticated
    @Operation(summary = "Get all blog posts", description = "Returns a collection of blog posts.")
    @APIResponse(responseCode = "200", description = "Blog posts found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BlogPostDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "No blog posts found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the blog posts.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while getting blog posts.\"}"))
    public Response getAllBlogPostsWithOptionalFiltering(@QueryParam("searchString") Optional<String> searchString,
            @QueryParam("page") Optional<Long> page) {
        return blogService.getAllBlogPostsWithOptionalFiltering(searchString, page).createHttpResponse();
    }

    @GET
    @Path("{blogId}")
    @Authenticated
    @Operation(summary = "Get a blog post by its id", description = "Returns a blog post.")
    @APIResponse(responseCode = "200", description = "Blog post found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BlogPostDTO.class)))
    @APIResponse(responseCode = "404", description = "No blog post with supplied id found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the blog post.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while getting blog post.\"}"))
    public Response getBlogPostById(@PathParam("blogId") Long blogId) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }
        return blogService.getBlogById(blogId).createHttpResponse();
    }

    @POST
    @Path("add")
    @Authenticated
    @Operation(summary = "Save new blog post", description = "Add a new blog post to database. When successful, returns the created blog post.")
    @RequestBody(description = "Blog post Json. Only title and content are required. Id and createdAt are automatically generated. Comments initially null, comments can not exist befor the blog post.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogPostDTO.class), example = "{\"id\":0,\"title\":\"My first blog post\",\"content\":\"This is my first blog post.\",\"createdAt\":\"null\",\"lastEditedAt\":\"null\",\"comments\":\"null\"}"))
    @APIResponse(responseCode = "201", description = "Blog post successfully created.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BlogPostDTO.class), example = "{\"id\":1,\"title\":\"My first blog post\",\"content\":\"This is my first blog post.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"null\",\"comments\":\"null\"}"))
    @APIResponse(responseCode = "409", description = "Problem while persisting the blog post.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the blog posts.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while persisting blog post.\"}"))
    public Response addBlogPost(@Valid BlogPostDTO blogDTO, @Context UriInfo uriInfo) {
        blogDTO.setAuthor(jwt.getName());
        return blogService.addBlogPost(blogDTO).createHttpResponse(uriInfo);
    }

    @DELETE
    @Path("remove/{blogId}")
    @Authenticated
    @Operation(summary = "Delete a blog post by its id", description = "Deletes a blog post by its id. Only the id is required in the path.")
    @APIResponse(responseCode = "204", description = "Blog post was successfully deleted.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "404", description = "The requested blog post with passed id was not found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while deleting the blog post.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while deleting blog post.\"}"))
    public Response deleteBlogPost(@PathParam("blogId") Long blogId) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }
        return blogService.deleteBlogPost(blogId).createHttpResponse();
    }

    @DELETE
    @Path("remove/all")
    @RolesAllowed("admin")
    @Operation(summary = "Delete all blog posts!", description = "Deletes all blog posts from database. Only admins are allowed to perform this action.")
    @APIResponse(responseCode = "204", description = "All blog posts were successfully deleted.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while deleting the blog post.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while deleting blog post.\"}"))
    public Response deleteAllBlogPosts() {
        boolean ohNoAllBlogPostsAreGone = blogService.deleteAllBlogPosts();
        return ohNoAllBlogPostsAreGone ? Response.status(Response.Status.NO_CONTENT).build()
                : Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Problem while deleting all blog posts.").build();
    }

    @PATCH
    @Path("update")
    @Authenticated
    @Operation(summary = "Update a blog post", description = "Update a blog post's title and content. When successful, returns the updated blog post.")
    @RequestBody(description = "The updated blog post.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = BlogPostDTO.class), example = "{\"id\":1,\"title\":\"Update: My first blog post\",\"content\":\"This is my first updated blog post.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"null\",\"comments\":[]}"))
    @APIResponse(responseCode = "200", description = "Blog post was successfully updated.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = BlogPostDTO.class), example = "{\"id\":1,\"title\":\"Update: My first blog post\",\"content\":\"This is my first updated blog post.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"2024-08-25T15:18:29.610083Z\",\"comments\":[]}"))
    @APIResponse(responseCode = "404", description = "The requested blog post with passed id was not found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while updating the blog post.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while updating blog post.\"}"))
    public Response updateBlogPost(@Valid BlogPostDTO blogDTO) {
        if (blogDTO.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing Blog-ID.").build();
        }
        return blogService.updateBlogPost(blogDTO).createHttpResponse();
    }

    @PUT
    @Path("replace/{blogId}")
    @Authenticated
    @Operation(summary = "Replace a blog post => function not implemented", description = "Replace a blog post. This function is not implemented.")
    @APIResponse(responseCode = "501", description = "Function not implemented by the server.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    public Response replaceBlogPost(@PathParam("blogId") Long blogId, BlogPostDTO blogDTO) {
        return Response.status(Response.Status.NOT_IMPLEMENTED).build();
    }

    // ▼ Comments related to a blog ▼
    @GET
    @Path("{blogId}/comments")
    @Authenticated
    @Operation(summary = "Get all comments from a blog post", description = "Returns a collection of comments from a blog post.")
    @APIResponse(responseCode = "200", description = "Comments found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "No comments found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the comments.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while getting comments.\"}"))
    public Response getAllCommentsFromBlogPost(@PathParam("blogId") Long blogId) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("blog id not found in path").build();
        }
        return commentService.getAllCommentsFromBlog(blogId).createHttpResponse();
    }

    @GET
    @Path("{blogId}/comments/{commentId}")
    @Authenticated
    @Operation(summary = "Get a single comment from a blog post", description = "Returns a comment from a blog post by id's of blog and comment.")
    @APIResponse(responseCode = "200", description = "Comments found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class)))
    @APIResponse(responseCode = "404", description = "No comment found. Occurs when blog post was not found or the comment does not exist.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the comment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while getting comment.\"}"))
    public Response getSingleCommentFromBlogPost(@PathParam("blogId") Long blogId,
            @PathParam("commentId") Long commentId) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }

        if (commentId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_COMMENT_ID_IN_PATH_MESSAGE).build();
        }
        return commentService.getSingleCommentFromBlog(blogId, commentId).createHttpResponse();
    }

    @POST
    @Path("{blogId}/comments/add")
    @Authenticated
    @Operation(summary = "Add a new comment to a blog post", description = "Add a new comment to a blog post and save it in database. When successful, returns the created comment.")
    @RequestBody(description = "Comment Json. Only content is required. Id, blogId, commentNumber and createdAt are automatically generated. BlogId is just the reference to the blog the comment is related to.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class), example = "{\"id\":0,\"blogId\":\"null\",\"commentNumber\":0,\"content\":\"This is an example comment.\",\"createdAt\":\"null\",\"lastEditedAt\":\"null\"}"))
    @APIResponse(responseCode = "201", description = "Comment successfully created.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class), example = "{\"id\":1,\"blogId\":1,\"commentNumber\":1,\"content\":\"This is an example comment.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"null\"}"))
    @APIResponse(responseCode = "409", description = "Problem while persisting the comment.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while persisting the comment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while persisting comment.\"}"))
    public Response addCommentToBlogPost(@PathParam("blogId") Long blogId, @Valid CommentDTO commentDTO,
            @Context UriInfo uriInfo) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }
        commentDTO.setAuthor(jwt.getName());
        return commentService.addCommentToBlog(blogId, commentDTO).createHttpResponse(uriInfo);
    }

    @DELETE
    @Path("{blogId}/comments/remove/{commentId}")
    @Authenticated
    @Operation(summary = "Delete a comment from a blog post", description = "Deletes a comment from a blog post by the id's of the comment and blog post. Only the id of the comment and the id of the blog post are required in the path.")
    @APIResponse(responseCode = "204", description = "Comment was successfully deleted.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "404", description = "The requested comment or blog post with passed id was not found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while deleting the comment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while deleting comment.\"}"))
    public Response deleteCommentFromBlogPost(@PathParam("blogId") Long blogId,
            @PathParam("commentId") Long commentId) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }

        if (commentId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_COMMENT_ID_IN_PATH_MESSAGE).build();
        }
        return commentService.deleteCommentFromBlog(blogId, commentId).createHttpResponse();
    }

    @PATCH
    @Path("{blogId}/comments/update")
    @Authenticated
    @Operation(summary = "Update a comment of a blog post", description = "Update a blog post's comment. When successful, returns the updated comment.")
    @RequestBody(description = "The updated comment.", required = true, content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class), example = "{\"id\":1,\"blogId\":1,\"commentNumber\":1,\"content\":\"Updated comment example.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"null\"}"))
    @APIResponse(responseCode = "200", description = "Comment was successfully updated.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class), example = "{\"id\":1,\"blogId\":1,\"commentNumber\":1,\"content\":\"Updated comment example.\",\"createdAt\":\"2024-08-25T15:18:29.610083Z\",\"lastEditedAt\":\"2024-08-25T15:18:29.610083Z\"}"))
    @APIResponse(responseCode = "404", description = "The requested comment (or corresponding blog post) was not found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while updating the comment.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while updating comment.\"}"))
    public Response updateCommentOfBlogPost(@PathParam("blogId") Long blogId, @Valid CommentDTO commentDTO) {
        if (blogId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(MISSING_BLOG_ID_IN_PATH_MESSAGE).build();
        }

        return commentService.updateCommentOfBlog(blogId, commentDTO).createHttpResponse();
    }
}