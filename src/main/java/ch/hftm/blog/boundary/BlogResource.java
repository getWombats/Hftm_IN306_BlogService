package ch.hftm.blog.boundary;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import ch.hftm.blog.control.BlogService;
import ch.hftm.blog.control.CommentService;
import ch.hftm.blog.model.dto.BlogDTO;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

// @DenyAll
@Path("blogs")
@Consumes("application/json")
public class BlogResource {

    @Inject
    BlogService blogService;

    @Inject
    CommentService commentService;

    @Inject
    JsonWebToken jwt;

    @GET
    @PermitAll
    public Response getBlogs(@QueryParam("searchString") Optional<String> searchString,
            @QueryParam("page") Optional<Long> page) {
        List<BlogDTO> blogDtos = blogService.getBlogs(searchString, page);

        if (blogDtos == null || blogDtos.isEmpty()) {
            return Response.status(Status.NOT_FOUND).entity("No blogs found.").build();
        }

        return Response.status(Status.OK).entity(blogDtos).build();
    }

    @GET
    @Path("{blogId}")
    // @PermitAll
    public Response getBlog(Long blogId) {

        if (blogId == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog-id provided.").build();
        }

        BlogDTO foundBlog = blogService.getBlogById(blogId);

        if (foundBlog == null) {
            return Response.status(Status.NOT_FOUND).entity("No blog with id " + blogId + "available.").build();
        }

        return Response.status(Status.OK).entity(foundBlog).build();
    }

    @POST
    public Response addBlog(@Valid BlogDTO blog, @Context UriInfo uriInfo) {

        if (blog == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog provided.").build();
        }

        // String username = jwt.getClaim(Claims.preferred_username.name());

        // if(username != null) {
        // blog.setAuthorName(username);
        // }

        BlogDTO addedBlog = blogService.addBlog(blog);

        if (addedBlog == null) {
            return Response.status(Status.NOT_MODIFIED).entity("Error while adding blog.").build();
        }

        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(addedBlog.getId())).build();
        return Response.created(uri).entity(addedBlog).build();
    }

    @DELETE
    @Path("{blogId}")
    public Response deleteBlog(Long blogId) {

        if (blogId == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog-id provided.").build();
        }

        BlogDTO deletedBlog = blogService.deleteBlog(blogId);

        if (deletedBlog == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Action not performed. Blog with id " + blogId + " was not found.").build();
        }

        return Response.status(Status.NO_CONTENT).build();
    }

    @PUT
    @Path("{replaceBlogId}")
    public Response replaceBlog(Long replaceBlogId, @HeaderParam("blogId") Long blogId) {

        if (replaceBlogId == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog-id in path provided.").build();
        }

        if (blogId == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog-id in header provided.").build();
        }

        BlogDTO replacedBlog = blogService.replaceBlog(replaceBlogId, blogId);

        if (replacedBlog == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.status(Status.OK).entity(replacedBlog).build();
    }

    @PATCH
    @Path("{blogId}")
    public Response updatehBlog(Long blogId, @Valid BlogDTO blog) {

        if (blogId == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog-id provided.").build();
        }

        if (blog == null) {
            return Response.status(Status.BAD_REQUEST).entity("No blog provided.").build();
        }

        BlogDTO updatedBlog = blogService.updateBlog(blogId, blog);

        if (updatedBlog == null) {
            return Response.status(Status.NOT_FOUND)
                    .entity("Action not performed. Blog with id " + blogId + " was not found.").build();
        }

        return Response.status(Status.OK).entity(updatedBlog).build();
    }
}