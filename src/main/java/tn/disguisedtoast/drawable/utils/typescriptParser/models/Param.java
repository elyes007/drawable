package tn.disguisedtoast.drawable.utils.typescriptParser.models;

public class Param {

    private String accessibility;
    private String name;
    private String type;

    public Param() {
    }

    public Param(String accessibility, String name, String type) {
        this.accessibility = accessibility;
        this.name = name;
        this.type = type;
    }

    public String getAccessibility() {
        return accessibility;
    }

    public void setAccessibility(String accessibility) {
        this.accessibility = accessibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
