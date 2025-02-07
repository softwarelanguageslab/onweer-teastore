package tools.descartes.teastore.webui.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        HashMap<String, Object> payload = new HashMap<>();
        payload.put("exception", exception);
        payload.put("stacktrace", exception.getStackTrace());
        payload.put("name", exception.getClass().getName());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(payload).build();
    }
}