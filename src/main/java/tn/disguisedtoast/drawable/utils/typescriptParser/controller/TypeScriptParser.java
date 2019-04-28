package tn.disguisedtoast.drawable.utils.typescriptParser.controller;

import org.apache.commons.lang3.StringUtils;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.ImportElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.Param;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.SearchResult;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ListIterator;

public final class TypeScriptParser {

    public static void addImport(Path typescriptPath, ImportElement importElement) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, "import {");
            if (searchResult != null && searchResult.getPos() != -1 && getLastWordPos(searchResult.getLines(), importElement.getModule()) == -1) {
                String lineToAdd = "import {";
                for (String dependency : importElement.getDependencies()) {
                    lineToAdd += " " + dependency + ",";
                }
                lineToAdd = lineToAdd.substring(0, lineToAdd.length() - 1);
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

    public static void addParameterToFunction(Path typescriptPath, String functionName, Param param) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, functionName);
            if (searchResult != null && searchResult.getPos() != -1) {
                String line = searchResult.getLines().get(searchResult.getPos());
                if (!line.contains(param.getName())) {
                    String paramDeclaration = StringUtils.substringBetween(searchResult.getLines().get(searchResult.getPos()), "(", ")");
                    String[] paramCouples = paramDeclaration.split(",", -1);
                    String remaining = line.substring(line.lastIndexOf(')'), line.length());
                    line = line.substring(0, line.lastIndexOf(')')) + (paramCouples[0].isEmpty() ? "" : ", ") + param.getAccessibility() + " " + param.getName() + ": " + param.getType() + remaining;

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

    /*public static void addFunction(Path typescriptPath, FunctionElement functionElement) {
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
    }*/

    public static void addFunction(Path typescriptPath, Path templateFilePath, String functionName, List<Param> params) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, "}");
            if (searchResult != null && searchResult.getPos() != -1) {
                if (getLastWordPos(searchResult.getLines(), functionName) == -1) {

                    String builtParams = "";
                    if (params != null) {
                        for (Param param : params) {
                            builtParams += param.getAccessibility() + " " + param.getName() + ": " + param.getType() + ", ";
                        }
                        builtParams = builtParams.substring(0, builtParams.length() - 2);
                    }

                    String functionTemplate = new String(Files.readAllBytes(templateFilePath));
                    functionTemplate = functionTemplate.replaceAll("#functionName#", functionName).replaceAll("#params#", builtParams);

                    searchResult.getLines().add(searchResult.getPos(), functionTemplate);

                    Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
                } else {
                    throw new Exception("Function with name '" + functionName + "' already exists.");
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

    public static Point getFirstWordPos(List<String> lines, String word) {
        ListIterator iterator = lines.listIterator();
        Point point = new Point(-1, -1);
        while (iterator.hasNext()) {
            String line = (String) iterator.next();
            point.y = line.indexOf(word);
            if (point.y != -1) {
                point.x = iterator.nextIndex();
                return point;
            }
        }
        return point;
    }

    public static boolean doesContaint(List<String> lines, String word) {
        for (String line : lines) {
            if (line.contains(word))
                return true;
        }
        return false;
    }

    public static void addModuleTo(Path typescriptPath, String targetName, String moduleName) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, targetName + ": [");
            System.out.println("Facebook: " + searchResult.getPos());
            if (searchResult != null && searchResult.getPos() != -1) {
                Point point = getFirstWordPos(searchResult.getLines().subList(searchResult.getPos(), searchResult.getLines().size() - 1), "]");
                if (point.x == -1 || doesContaint(searchResult.getLines().subList(searchResult.getPos(), searchResult.getPos() + point.x), moduleName)) {
                    return;
                }
                System.out.println(searchResult.getPos());
                System.out.println(point);
                String line = searchResult.getLines().get(searchResult.getPos() + point.x - 1);
                System.out.println(line);
                line = line.substring(0, point.y) + ", " + moduleName + "],";
                searchResult.getLines().remove(searchResult.getPos() + point.x - 1);
                searchResult.getLines().add(searchResult.getPos() + point.x - 1, line);
                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
