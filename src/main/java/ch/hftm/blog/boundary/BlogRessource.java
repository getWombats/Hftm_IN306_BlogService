package ch.hftm.blog.boundary;

import java.net.URI;
import java.util.Optional;
import ch.hftm.blog.control.BlogService;
import ch.hftm.blog.entity.Blog;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
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
    public Response getBlog(long id) {
        return Response.status(Status.OK).entity(blogService.getBlogById(id)).build();
    }

    @POST
    public Response addBlog(Blog blog, @Context UriInfo uriInfo) {
        blogService.addBlog(blog);
        Log.info("POST Request: " + blog);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(blog.getId())).build();
        return Response.created(uri).entity(blog).build();
    }
}