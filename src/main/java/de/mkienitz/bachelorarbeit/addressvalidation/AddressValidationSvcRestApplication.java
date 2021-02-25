package de.mkienitz.bachelorarbeit.addressvalidation;

import de.mkienitz.bachelorarbeit.addressvalidation.application.AddressValidationResource;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/data")
public class AddressValidationSvcRestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> s = new HashSet<Class<?>>();

        s.add(CORSFilter.class);
        s.add(AddressValidationResource.class);

        return s;
    }
}
