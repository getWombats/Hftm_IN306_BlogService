package ch.hftm.blog.util;

import jakarta.ws.rs.core.Response;

public class ResponseFactory {
    public static Response createValidationErrorResponse(){
                    return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Text contains forbidden words.")
                    .build();
    }
}
