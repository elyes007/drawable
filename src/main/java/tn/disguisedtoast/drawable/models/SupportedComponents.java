package tn.disguisedtoast.drawable.models;

public enum SupportedComponents {
    ION_TOOLBAR("ION-TOOLBAR"),
    ION_BUTTON("ION-BUTTON"),
    ION_IMG("ION-IMG"),
    SPAN("SPAN"),
    ION_ITEM("ION-ITEM"),
    ION_INPUT("ION-INPUT"),
    ION_LABEL("ION-LABEL"),
    ION_TITLE("ION-TITLE"),
    ION_BUTTONS("ION-BUTTONS"),
    ION_BACK_BUTTON("ION-BACK-BUTTON");

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
