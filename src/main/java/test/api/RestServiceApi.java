package test.api;

import jakarta.ws.rs.Encoded;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("service")
public interface RestServiceApi {

    @GET
    @Path("method-encoded/{pp}")
    @Produces(MediaType.APPLICATION_JSON)
    public ParameterSummary methodEncoded(@Encoded @PathParam("pp") String pp,
            @QueryParam("qp") String qp, @Encoded @QueryParam("eqp") String eqp);

    @GET
    @Path("method/{pp}")
    @Produces(MediaType.APPLICATION_JSON)
    public ParameterSummary method(@PathParam("pp") String pp, @QueryParam("qp") String qp,
            @Encoded @QueryParam("eqp") String eqp);
}
