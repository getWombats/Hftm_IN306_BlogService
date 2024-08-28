package ch.hftm.blog.boundary;

import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import ch.hftm.blog.control.CommentService;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.dto.ErrorDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource extends ResourceBase {
    @Inject
    CommentService commentService;

    @GET
    @Operation(summary = "Get all comments", description = "Returns a collection of comments.")
    @APIResponse(responseCode = "200", description = "Comments found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = CommentDTO.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "404", description = "No comments found.", content = @Content(mediaType = MediaType.TEXT_PLAIN))
    @APIResponse(responseCode = "500", description = "An Error occurred while getting the comments.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ErrorDTO.class), example = "{\"traceId\":\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"error\":\"ExceptionName\",\"message:\":\"Problem while getting comments.\"}"))
    public Response getAllCommentsWithOptionalFiltering(@QueryParam("searchString") Optional<String> searchString,
            @QueryParam("page") Optional<Long> page) {
        return commentService.getAllCommentsWithOptionalFiltering(searchString, page).createHttpResponse();
    }
}
