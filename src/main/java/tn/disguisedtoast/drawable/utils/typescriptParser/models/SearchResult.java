package tn.disguisedtoast.drawable.utils.typescriptParser.models;

import java.util.List;

public class SearchResult {
    private List<String> lines;
    private int pos;

    public SearchResult(List<String> lines, int pos) {
        this.lines = lines;
        this.pos = pos;
    }

    public List<String> getLines() {
        return lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
