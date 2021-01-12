package de.mkienitz.bachelorarbeit.addressvalidation;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class Address {
    @Schema(required = true, example = "Musterallee")
    protected String streetName;

    @Schema(required = true, example = "42")
    protected String streetNumber;

    @Schema(required = true, example = "44141")
    protected String postalCode;

    @Schema(required = true, example = "Dortmund")
    protected String city;

    public Address() {
    }

    public Address(String streetName, String streetNumber, String postalCode, String city) {
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Address{" +
                "streetName='" + streetName + '\'' +
                ", streetNumber='" + streetNumber + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
