package tn.disguisedtoast.drawable.utils.typescriptParser.controller;

import org.apache.commons.lang3.StringUtils;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.FunctionElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.ImportElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.SearchResult;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public final class TypeScriptParser {

    public static void addImport(Path typescriptPath, ImportElement importElement) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, "import");
            if (searchResult != null && searchResult.getPos() != -1 && getLastWordPos(searchResult.getLines(), importElement.getModule()) == -1) {
                String lineToAdd = "import {";
                for (String dependency : importElement.getDependencies()) {
                    lineToAdd += " " + dependency + ",";
                }
                lineToAdd = lineToAdd.substring(0, lineToAdd.length() - 2);
                lineToAdd += " } from '" + importElement.getModule() + "';";
                searchResult.getLines().add(searchResult.getPos() + 1, lineToAdd);
                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeImport(Path typescriptPath, String moduleName) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, moduleName);
            if (searchResult != null && searchResult.getPos() != -1) {
                searchResult.getLines().remove(searchResult.getPos());
                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addParameterToFunction(Path typescriptPath, String functionName, String paramName, String paramType) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, functionName);
            if (searchResult != null && searchResult.getPos() != -1) {
                String line = searchResult.getLines().get(searchResult.getPos());
                if (!line.contains(paramName)) {
                    String paramDeclaration = StringUtils.substringBetween(searchResult.getLines().get(searchResult.getPos()), "(", ")");
                    String[] paramCouples = paramDeclaration.split(",", -1);
                    String remaining = line.substring(line.lastIndexOf(')'), line.length());
                    line = line.substring(0, line.lastIndexOf(')')) + (paramCouples[0].isEmpty() ? "" : ", ") + "private " + paramName + ": " + paramType + remaining;

                    searchResult.getLines().remove(searchResult.getPos());
                    searchResult.getLines().add(searchResult.getPos(), line);

                    Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
                } else {
                    throw new Exception("Param already existing.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addFunction(Path typescriptPath, FunctionElement functionElement) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, "}");
            if (searchResult != null && searchResult.getPos() != -1) {
                if (getLastWordPos(searchResult.getLines(), functionElement.getName()) == -1) {
                    String line = "\n" + functionElement.getName() + "(";
                    for (Map.Entry<String, String> paramCouple : functionElement.getParameters().entrySet()) {
                        line += paramCouple.getKey() + ": " + paramCouple.getValue() + ", ";
                    }
                    if (functionElement.getParameters().size() != 0) {
                        line = line.substring(0, line.length() - 3);
                    }
                    line += ") {\n";
                    for (String bodyLine : functionElement.getBodyLines()) {
                        line += bodyLine + "\n";
                    }
                    line += "}\n";

                    searchResult.getLines().add(searchResult.getPos(), line);

                    Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
                } else {
                    throw new Exception("Function with name '" + functionElement.getName() + "' already exists.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SearchResult searchLastWordPos(Path filePath, String word) {
        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            return new SearchResult(lines, getLastWordPos(lines, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getLastWordPos(List<String> lines, String word) {
        ListIterator iterator = lines.listIterator();
        int pos = -1;
        while (iterator.hasNext()) {
            String line = (String) iterator.next();
            if (line.contains(word)) {
                pos = iterator.nextIndex() - 1;
            }
        }
        return pos;
    }

    private void test() {
        /*
            String functionDeclarationName = line.substring(0, line.lastIndexOf('('));
            String[] paramCouples = StringUtils.substringBetween(searchResult.getLines().get(searchResult.getPos()), "(", ")").split(",");
            Map<String, String> params = new HashMap<>();
            for(String param : paramCouples) {
                String[] keyValue = param.trim().split("\\s+");
                params.put(keyValue[0], keyValue[1]);
            }
            String remaining = line.substring(line.lastIndexOf(')')+1, line.length()-1);

            System.out.println("Name: " + functionDeclarationName);
            System.out.println("Params: ");
            params.forEach((k, v) -> System.out.println("Key: " + k + " Value: " + v));
            System.out.println("Remaining: " + remaining);

         */
    }

}
