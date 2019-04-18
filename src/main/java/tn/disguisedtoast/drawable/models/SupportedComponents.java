package tn.disguisedtoast.drawable.models;

public enum SupportedComponents {
    ION_TOOLBAR("ion-toolbar"),
    ION_BUTTON("ion-button"),
    ION_IMG("ion-img"),
    SPAN("span"),
    ION_ITEM("ion-item"),
    ION_INPUT("ion-input"),
    ION_LABEL("ion-label"),
    ION_TITLE("ion-title"),
    ION_BUTTONS("ion-buttons"),
    ION_BACK_BUTTON("ion-back-button"),
    ION_ICON("ion-icon"),
    BODY("body"),
    ION_CONTENT("ion-content");

    private String value;

    SupportedComponents(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static SupportedComponents getEnum(String value) {
        if(value == null)
            throw new IllegalArgumentException();
        for(SupportedComponents v : values())
            if(value.equalsIgnoreCase(v.getValue())) return v;
        throw new IllegalArgumentException();
    }
}
