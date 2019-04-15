package tn.disguisedtoast.drawable.utils.typescriptParser.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionElement {
    private String name;
    private Map<String, String> parameters;
    private List<String> bodyLines;

    public FunctionElement() {
        this.parameters = new HashMap<>();
        this.bodyLines = new ArrayList<>();
    }

    public FunctionElement(String name) {
        this.name = name;
        this.parameters = new HashMap<>();
        this.bodyLines = new ArrayList<>();
    }

    public FunctionElement(String name, Map<String, String> parameters, List<String> bodyLines) {
        this.name = name;
        this.parameters = parameters;
        this.bodyLines = bodyLines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public List<String> getBodyLines() {
        return bodyLines;
    }

    public void setBodyLines(List<String> bodyLines) {
        this.bodyLines = bodyLines;
    }
}
