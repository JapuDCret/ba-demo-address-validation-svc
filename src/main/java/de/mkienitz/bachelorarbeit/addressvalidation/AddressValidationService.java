package de.mkienitz.bachelorarbeit.addressvalidation;

import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class AddressValidationService {

    private static Logger log = LoggerFactory.getLogger(AddressValidationService.class.getName());

    private Set<String> staedte;

    @PostConstruct
    public void postConstruct() throws IOException {
        URL staedteResource = this.getClass().getClassLoader().getResource("staedte_osm.txt");

        Set<String> staedte = new TreeSet<>();

        try(BufferedReader read = new BufferedReader(new InputStreamReader(staedteResource.openStream()))) {
            String i;
            while ((i = read.readLine()) != null) {
                staedte.add(i);
            }
        } catch(IOException ioe) {
            log.error("postConstruct(): could not initialize this.staedte: ", ioe);

            throw ioe;
        }

        this.staedte = staedte;

        log.debug("postConstruct(): this.staedte.size() = " + this.staedte.size());
    }

    @Traced(operationName = "AddressValidationService.isValid")
    public ValidationResult isValid(Address address) {
        if(address == null) {
            log.info("isValid(): Address object is null, returning false");
            return new ValidationResult(false, "address");
        }

        /* validate streetName */
        ValidationResult streetNameValidation = this.validateStreetName(address.getStreetName());
        if(!streetNameValidation.isValid()) return streetNameValidation;

        /* validate streetNumber */
        ValidationResult streetNumberValidation = this.validateStreetNumber(address.getStreetNumber());
        if(!streetNumberValidation.isValid()) return streetNumberValidation;

        /* validate postalCode */
        ValidationResult postalCodeValidation = this.validatePostalCode(address.getPostalCode());
        if(!postalCodeValidation.isValid()) return postalCodeValidation;

        /* validate city */
        ValidationResult cityValidation = this.validateCity(address.getCity());
        if(!cityValidation.isValid()) return cityValidation;

        return new ValidationResult(true);
    }

    private static final Pattern streetNamePattern = Pattern.compile("[a-zA-Z\\,\\-\\ ]+");
    private ValidationResult validateStreetName(String streetName) {
        if(streetName == null) {
            log.info("isValid(): address.streetName is null, returning false");
            return new ValidationResult(false, "streetName");
        }

        Matcher streetNameMatcher = streetNamePattern.matcher(streetName);
        if(!streetNameMatcher.matches()) {
            log.info("isValid(): address.streetName is does not meet regex, returning false");
            return new ValidationResult(false, "streetName");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validateStreetNumber(String streetNumber) {
        Integer iStreetNumber = isInteger(streetNumber);
        if(iStreetNumber == null) {
            log.info("validateStreetNumber(): address.streetNumber is not an integer (\"" + streetNumber + "\"), returning false");
            return new ValidationResult(false, "streetNumber");
        }

        if(iStreetNumber < 1 || iStreetNumber >= 10000) {
            log.info("validateStreetNumber(): address.streetNumber is not a valid streetNumber (" + iStreetNumber + "), returning false");
            return new ValidationResult(false, "streetNumber");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validatePostalCode(String postalCode) {
        Integer iPostalCode = isInteger(postalCode);
        if(iPostalCode == null) {
            log.info("validatePostalCode(): address.postalCode is not an integer (\"" + postalCode + "\"), returning false");
            return new ValidationResult(false, "postalCode");
        }

        if(iPostalCode < 10000 || iPostalCode >= 60000) {
            log.info("validatePostalCode(): address.postalCode is not a valid postalCode (" + iPostalCode + "), returning false");
            return new ValidationResult(false, "postalCode");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validateCity(String city) {
        if(city == null) {
            log.info("isValid(): address.city is null, returning false");
            return new ValidationResult(false, "city");
        }
        if(!staedte.contains(city)) {
            log.info("isValid(): address.city has an unknown value (\"" + city + "\"), returning false");
            return new ValidationResult(false, "city");
        }

        return new ValidationResult(true);
    }

    public static Integer isInteger(String strNum) {
        if (strNum == null) {
            return null;
        }
        try {
            return Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return null;
        }
    }
}
