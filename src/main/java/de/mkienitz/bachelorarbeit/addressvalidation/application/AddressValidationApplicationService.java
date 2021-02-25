package de.mkienitz.bachelorarbeit.addressvalidation.application;

import de.mkienitz.bachelorarbeit.addressvalidation.domain.Address;
import de.mkienitz.bachelorarbeit.addressvalidation.domain.ValidationResult;
import org.eclipse.microprofile.opentracing.Traced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.naming.ConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class AddressValidationApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressValidationApplicationService.class.getName());

    private static final String CITIES_FILE = "staedte_osm.txt";

    private Set<String> cities;

    @PostConstruct
    public void init() throws RuntimeException {
        URL staedteResource = this.getClass().getClassLoader().getResource(CITIES_FILE);

        Set<String> cities = new TreeSet<>();

        LOGGER.debug("init(): reading file \"" + CITIES_FILE + "\"");

        try(BufferedReader read = new BufferedReader(new InputStreamReader(staedteResource.openStream()))) {
            String i;
            while ((i = read.readLine()) != null) {
                cities.add(i);
            }
        } catch(IOException e) {
            LOGGER.error("init(): could not parse cities, e = ", e);

            throw new RuntimeException("could not parse cities", e);
        }

        this.cities = cities;

        LOGGER.debug("init(): this.cities.size() = " + this.cities.size());
    }

    @Traced(operationName = "AddressValidationApplicationService.isValid")
    public ValidationResult isValid(Address address) {
        if(address == null) {
            LOGGER.info("isValid(): Address object is null, returning false");
            return new ValidationResult(false, "address");
        }

        /* validate streetName */
        ValidationResult streetNameValidation = this.validateStreetName(address.getStreetName());
        if(!streetNameValidation.isValid()) {
            return streetNameValidation;
        }

        /* validate streetNumber */
        ValidationResult streetNumberValidation = this.validateStreetNumber(address.getStreetNumber());
        if(!streetNumberValidation.isValid()) {
            return streetNumberValidation;
        }

        /* validate postalCode */
        ValidationResult postalCodeValidation = this.validatePostalCode(address.getPostalCode());
        if(!postalCodeValidation.isValid()) {
            return postalCodeValidation;
        }

        /* validate city */
        ValidationResult cityValidation = this.validateCity(address.getCity());
        if(!cityValidation.isValid()) {
            return cityValidation;
        }

        return new ValidationResult(true);
    }

    private static final Pattern PATTERN_STREET_NAME = Pattern.compile("[a-zA-Z\\,\\-\\ ]+");
    private ValidationResult validateStreetName(String streetName) {
        if(streetName == null) {
            LOGGER.info("isValid(): address.streetName is null, returning false");
            return new ValidationResult(false, "streetName");
        }

        Matcher streetNameMatcher = PATTERN_STREET_NAME.matcher(streetName);
        if(!streetNameMatcher.matches()) {
            LOGGER.info("isValid(): address.streetName is does not meet regex, returning false");
            return new ValidationResult(false, "streetName");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validateStreetNumber(String streetNumber) {
        Integer iStreetNumber = isInteger(streetNumber);
        if(iStreetNumber == null) {
            LOGGER.info("validateStreetNumber(): address.streetNumber is not an integer (\"" + streetNumber + "\"), returning false");
            return new ValidationResult(false, "streetNumber");
        }

        if(iStreetNumber < 1 || iStreetNumber >= 10000) {
            LOGGER.info("validateStreetNumber(): address.streetNumber is not a valid streetNumber (" + iStreetNumber + "), returning false");
            return new ValidationResult(false, "streetNumber");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validatePostalCode(String postalCode) {
        Integer iPostalCode = isInteger(postalCode);
        if(iPostalCode == null) {
            LOGGER.info("validatePostalCode(): address.postalCode is not an integer (\"" + postalCode + "\"), returning false");
            return new ValidationResult(false, "postalCode");
        }

        if(iPostalCode < 10000 || iPostalCode >= 60000) {
            LOGGER.info("validatePostalCode(): address.postalCode is not a valid postalCode (" + iPostalCode + "), returning false");
            return new ValidationResult(false, "postalCode");
        }

        return new ValidationResult(true);
    }

    private ValidationResult validateCity(String city) {
        if(city == null) {
            LOGGER.info("isValid(): address.city is null, returning false");
            return new ValidationResult(false, "city");
        }
        if(!cities.contains(city)) {
            LOGGER.info("isValid(): address.city has an unknown value (\"" + city + "\"), returning false");
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
