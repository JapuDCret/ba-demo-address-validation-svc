package de.mkienitz.bachelorarbeit.addressvalidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidationService {

    private static Logger log = LoggerFactory.getLogger(AddressValidationService.class.getName());

    private final Set<String> staedte;

    public AddressValidationService() throws IOException {
        URL staedteResource = this.getClass().getClassLoader().getResource("staedte_osm.txt");

        Set<String> staedte = new TreeSet<>();

        try(BufferedReader read = new BufferedReader(new InputStreamReader(staedteResource.openStream()))) {
            String i;
            while ((i = read.readLine()) != null) {
                staedte.add(i);
            }
        } catch(IOException ioe) {
            log.error("AddressValidationService(): could not initialize this.staedte: ", ioe);

            throw ioe;
        }

        this.staedte = staedte;

        log.debug("AddressValidationService(): this.staedte.size() = " + this.staedte.size());
    }

    public ValidationResult isValid(Address address) {
        if(address == null) {
            log.info("isValid(): Address object is null, returning false");
            return new ValidationResult(false, "address");
        }

        /* validate streetName */
        String streetName = address.getStreetName();
        if(streetName == null) {
            log.info("isValid(): address.streetName is null, returning false");
            return new ValidationResult(false, "streetName");
        }
        Pattern streetNamePattern = Pattern.compile("[a-zA-Z\\,\\-\\ ]+");
        Matcher streetNameMatcher = streetNamePattern.matcher(streetName);
        if(!streetNameMatcher.matches()) {
            log.info("isValid(): address.streetName is does not meet regex, returning false");
            return new ValidationResult(false, "streetName");
        }

        /* validate streetNumber */
        Integer iStreetNumber = isInteger(address.getStreetNumber());
        if(iStreetNumber == null) {
            log.info("isValid(): address.streetNumber is not an integer (\"" + address.getStreetNumber() + "\"), returning false");
            return new ValidationResult(false, "streetNumber");
        }
        if(iStreetNumber < 1 || iStreetNumber >= 10000) {
            log.info("isValid(): address.streetNumber is not a valid streetNumber (" + iStreetNumber + "), returning false");
            return new ValidationResult(false, "streetNumber");
        }

        /* validate postalCode */
        Integer iPostalCode = isInteger(address.getPostalCode());
        if(iPostalCode == null) {
            log.info("isValid(): address.postalCode is not an integer (\"" + address.getPostalCode() + "\"), returning false");
            return new ValidationResult(false, "postalCode");
        }
        if(iPostalCode < 10000 || iPostalCode >= 60000) {
            log.info("isValid(): address.postalCode is not a valid postalCode (" + iPostalCode + "), returning false");
            return new ValidationResult(false, "postalCode");
        }

        /* validate city */
        String city = address.getCity();
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
