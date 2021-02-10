package de.mkienitz.bachelorarbeit.addressvalidation;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/address-validation")
@RequestScoped
public class AddressValidationResource {

    private static Logger log = LoggerFactory.getLogger(AddressValidationResource.class.getName());

    @Inject
    private AddressValidationService service;

    @POST
    @Operation(description = "Validates an address, returns 400 if the address is invalid")
    @Traced(operationName = "AddressValidationResource.validateAddress")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateAddress(
            @RequestBody(required = true, content = @Content(schema = @Schema(implementation = Address.class)))
            @NotNull @Valid Address address
    ) {
        ValidationResult validationResult = service.isValid(address);

        if(validationResult.isValid()) {
            return Response.ok().entity(validationResult).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationResult).build();
        }
    }
}
