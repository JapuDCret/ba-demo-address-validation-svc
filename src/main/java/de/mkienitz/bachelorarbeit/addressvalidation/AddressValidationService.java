package de.mkienitz.bachelorarbeit.addressvalidation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class AddressValidationService {

    private static Logger log = LoggerFactory.getLogger(AddressValidationService.class.getName());

    private final Set<String> staedte;

    public AddressValidationService() throws IOException {
        URL staedteResource = this.getClass().getClassLoader().getResource("staedte_osm.txt");

        BufferedReader read = new BufferedReader(new InputStreamReader(staedteResource.openStream()));

        Set<String> staedte = new TreeSet<>();

        String i;
        while ((i = read.readLine()) != null) {
            staedte.add(i);
        }
        read.close();

        this.staedte = staedte;

        log.debug("AddressValidationService(): this.staedte = " + Arrays.toString(this.staedte.toArray()));
    }

    public boolean isValid(Address address) {
        if(address == null) {
            log.info("isValid(): Address object is null, returning false");
            return false;
        }

        /* validate streetName */
        if(address.getStreetNumber() == null) {
            log.info("isValid(): address.streetName is null, returning false");
            return false;
        }

        /* validate streetNumber */
        Integer iStreetNumber = isInteger(address.getStreetNumber());
        if(iStreetNumber == null) {
            log.info("isValid(): address.streetNumber is not an integer (\"" + address.getStreetNumber() + "\"), returning false");
            return false;
        }
        if(iStreetNumber < 1 || iStreetNumber >= 10000) {
            log.info("isValid(): address.streetNumber is not a valid streetNumber (" + iStreetNumber + "), returning false");
            return false;
        }

        /* validate postalCode */
        Integer iPostalCode = isInteger(address.getPostalCode());
        if(iPostalCode == null) {
            log.info("isValid(): address.postalCode is not an integer (\"" + address.getPostalCode() + "\"), returning false");
            return false;
        }
        if(iPostalCode < 10000 || iPostalCode >= 60000) {
            log.info("isValid(): address.postalCode is not a valid postalCode (" + iPostalCode + "), returning false");
            return false;
        }

        /* validate city */
        if(address.getCity() == null) {
            log.info("isValid(): address.city is null, returning false");
            return false;
        }
        if(!staedte.contains(address.getCity())) {
            log.info("isValid(): address.city has an unknown value (\"" + address.getCity() + "\"), returning false");
            return false;
        }

        return true;
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
