package ch.hftm.blog.model.domain;

import java.util.UUID;
import java.net.URI;

import ch.hftm.blog.model.dto.BlogPostDTO;
import ch.hftm.blog.model.dto.CommentDTO;
import ch.hftm.blog.model.dto.ErrorDTO;
import ch.hftm.blog.util.RequestType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public class ServiceResponse<T> {

    private T data;
    private Exception exception;
    private ErrorDTO errorDto;
    private RequestType requestType;

    public ServiceResponse(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setError(Exception exception) {
        this.exception = exception;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Exception getError() {
        return this.exception;
    }

    public T getData() {
        return this.data;
    }

    public boolean hasError() {
        return this.exception != null;
    }

    public void setErrorDto(ErrorDTO errorDto) {
        this.errorDto = errorDto;
    }

    public ErrorDTO getErrorDto() {
        return this.errorDto;
    }

    public Response createHttpResponse() {

        // Check if there is an exception
        if (this.exception != null) {
            if (this.errorDto == null) {

                ErrorDTO error = new ErrorDTO();
                error.setError(this.exception.getClass().getSimpleName());
                error.setMessage("No action performed.");
                error.setTraceId(UUID.randomUUID());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error)
                        .build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDto)
                    .build();
        }

        // Check if there is no data
        if (this.data == null) {

            switch (this.requestType) {
                case POST:
                    return Response.status(Response.Status.CONFLICT).entity("Data was not persisted.").build();
                default:
                    return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        // Check if data is Iterable (List) and if it is empty
        if (this.data instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) this.data;
            if (!iterable.iterator().hasNext()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }

        // If no errors and data not null
        switch (this.requestType) {
            case POST:
                return Response.status(Response.Status.CREATED).entity(this.data).build();
            case DELETE:
                return Response.status(Response.Status.NO_CONTENT).build();
            default:
                return Response.status(Response.Status.OK).entity(this.data).build();
        }
    }

    public Response createHttpResponse(UriInfo uriInfo) {

        // Check if there is an exception
        if (this.exception != null) {
            if (this.errorDto == null) {

                ErrorDTO error = new ErrorDTO();
                error.setError(this.exception.getClass().getSimpleName());
                error.setMessage("No action performed.");
                error.setTraceId(UUID.randomUUID());

                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error)
                        .build();
            }

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorDto)
                    .build();
        }

        // Check if there is no data
        if (this.data == null) {

            switch (this.requestType) {
                case POST:
                    return Response.status(Response.Status.CONFLICT).entity("Data was not persisted.").build();
                default:
                    return Response.status(Response.Status.NOT_FOUND).build();
            }
        }

        // Check if data is Iterable (List) and if it is empty
        if (this.data instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) this.data;
            if (!iterable.iterator().hasNext()) {
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }

        // If no errors and data not null
        switch (this.requestType) {
            case POST:
                if (this.data instanceof BlogPostDTO) {
                    BlogPostDTO blog = (BlogPostDTO) this.data;
                    URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(blog.getId())).build();
                    return Response.created(uri).entity(this.data).build();
                } else if (this.data instanceof CommentDTO) {
                    CommentDTO comment = (CommentDTO) this.data;
                    URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(comment.getId())).build();
                    return Response.created(uri).entity(this.data).build();
                } else {
                    return Response.status(Response.Status.CREATED).entity(this.data).build();
                }

            case DELETE:
                return Response.status(Response.Status.NO_CONTENT).build();
            default:
                return Response.status(Response.Status.OK).entity(this.data).build();
        }
    }
}