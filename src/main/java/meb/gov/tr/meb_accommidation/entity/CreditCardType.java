package meb.gov.tr.meb_accommidation.entity;

// Todo: Add surcharge ?
public enum CreditCardType {
    MasterCard("Mastercard"),
    Visa("Visa");

    private String description;

    CreditCardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
