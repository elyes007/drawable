package tn.disguisedtoast.drawable.utils.typescriptParser.models;

import java.util.ArrayList;
import java.util.List;

public class ImportElement {
    private List<String> dependencies;
    private String module;

    public ImportElement() {
        this.dependencies = new ArrayList<>();
    }

    public ImportElement(String module) {
        this.module = module;
        this.dependencies = new ArrayList<>();
    }

    public ImportElement(List<String> dependencies, String module) {
        this.dependencies = dependencies;
        this.module = module;
    }

    public List<String> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<String> dependencies) {
        this.dependencies = dependencies;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
