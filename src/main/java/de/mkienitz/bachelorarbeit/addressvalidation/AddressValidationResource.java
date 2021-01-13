package de.mkienitz.bachelorarbeit.addressvalidation;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 */
@Path("/address-validation")
@Singleton
public class AddressValidationResource {

    private static Logger log = LoggerFactory.getLogger(AddressValidationResource.class.getName());

    @Inject
    private AddressValidationService service;

    @POST
    @Operation(description = "Validates an address, returns 400 if the address is invalid")
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
