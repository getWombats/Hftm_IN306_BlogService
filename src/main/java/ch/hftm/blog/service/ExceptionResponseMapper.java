package ch.hftm.blog.service;

// import org.hibernate.exception.DataException;

// import jakarta.persistence.EntityExistsException;
// import jakarta.persistence.EntityNotFoundException;
// import jakarta.persistence.NoResultException;
// import jakarta.persistence.NonUniqueResultException;
// import jakarta.persistence.OptimisticLockException;
// import jakarta.ws.rs.core.Response;
// import jakarta.ws.rs.ext.ExceptionMapper;
// import jakarta.ws.rs.ext.Provider;

// @Provider
// public class ExceptionResponseMapper implements ExceptionMapper<Exception> {

//     @Override
//     public Response toResponse(Exception exception) {
//         return mapExceptionToResponse(exception);
//     }

//     private Response mapExceptionToResponse(Exception exception) {

//         if (exception instanceof jakarta.validation.ConstraintViolationException||
//         exception instanceof IllegalArgumentException ||
//         exception instanceof DataException ||
//         exception instanceof NonUniqueResultException) {
//             return Response.status(Response.Status.BAD_REQUEST).entity(exception.getMessage()).build();

//         } else if (exception instanceof org.hibernate.exception.ConstraintViolationException ||
//         exception instanceof OptimisticLockException ||
//         exception instanceof EntityExistsException) {
//             return Response.status(Response.Status.CONFLICT).entity(exception.getMessage()).build();

//         } else if (exception instanceof NoResultException ||
//         exception instanceof EntityNotFoundException) {
//             return Response.status(Response.Status.NOT_FOUND).entity(exception.getMessage()).build();
//         } else {
//             // logger.fatalf(exception, "Failed to process request to: {}", httpServerRequestProvider.get().absoluteURI());
//             return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal Server Error\n\n" + exception.getMessage()).build();
//         }
//     }
// }
