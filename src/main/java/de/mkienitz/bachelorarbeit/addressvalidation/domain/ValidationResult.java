package de.mkienitz.bachelorarbeit.addressvalidation.domain;

public class ValidationResult {
    private boolean valid;

    private String invalidField;

    public ValidationResult() {
    }

    public ValidationResult(boolean valid) {
        this.valid = valid;
        this.invalidField = null;
    }

    public ValidationResult(boolean valid, String invalidField) {
        this.valid = valid;
        this.invalidField = invalidField;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getInvalidField() {
        return invalidField;
    }

    public void setInvalidField(String invalidField) {
        this.invalidField = invalidField;
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
                "valid=" + valid +
                ", invalidField='" + invalidField + '\'' +
                '}';
    }
}
