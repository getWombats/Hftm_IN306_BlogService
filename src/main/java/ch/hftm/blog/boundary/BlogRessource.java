package ch.hftm.blog.boundary;

import java.net.URI;
import java.util.Optional;

import ch.hftm.blog.control.BlogService;
import ch.hftm.blog.model.entity.Blog;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

@Path("blogs")
public class BlogRessource {

    @Inject
    BlogService blogService;

    @GET
    public Response getBlogs(@QueryParam("searchString") Optional<String> searchString,
            @QueryParam("page") Optional<Long> page) {
        return Response.status(Status.OK).entity(blogService.getBlogs(searchString, page)).build();
    }

    @GET
    @Path("{id}")
    public Response getBlog(Long id) {

        if(id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        return Response.status(Status.OK).entity(blogService.getBlogById(id)).build();
    }

    @POST
    public Response addBlog(Blog blog, @Context UriInfo uriInfo) {

        blogService.addBlog(blog);

        Log.info("POST Request: " + blog);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(blog.getId())).build();
        return Response.created(uri).entity(blog).build();
    }

    @DELETE
    public Response deleteBlog(@HeaderParam("id") Long id) {

        if(id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        Blog deletedBlog = blogService.deleteBlog(id);

        if (deletedBlog != null) {
            return Response.status(Status.NO_CONTENT).entity(deletedBlog).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response putBlog(@PathParam("id") Long id, Blog blog) {

        if(blog == null || id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (blog.getTitle().isEmpty() || blog.getContent().isEmpty()) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            Blog replacedBlog = blogService.replaceBlog(id, blog);

            if (replacedBlog != null) {
            return Response.status(Status.OK).entity(replacedBlog).build();
            } else {
                return Response.status(Status.NOT_FOUND).build();
            }
        }
    }

    @PATCH
    public Response patchBlog(@HeaderParam("id") Long id, Blog blog) {
        
        if(blog == null || id == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        
        Blog updatedBlog = blogService.updateBlog(id, blog);

        if (updatedBlog != null) {
            return Response.status(Status.OK).entity(updatedBlog).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}