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
import java.util.ArrayList;
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
                String paramDefinition = param.getAccessibility() + " " + param.getName() + ": " + param.getType();
                if (line.contains(param.getName())) {
                    int indexParam = line.indexOf(paramDefinition);
                    int endIndexToDelete = indexParam + paramDefinition.length();
                    System.out.println(line.charAt(endIndexToDelete));
                    if (line.charAt(endIndexToDelete) == ',') {
                        endIndexToDelete += 2;
                    }
                    line = line.substring(0, indexParam) + line.substring(endIndexToDelete);
                    System.out.println("LINEEE: " + line);
                }

                String paramDeclaration = StringUtils.substringBetween(line, "(", ")");
                String[] paramCouples = paramDeclaration.split(",", -1);
                String remaining = line.substring(line.lastIndexOf(')'), line.length());
                line = line.substring(0, line.lastIndexOf(')')) + (paramCouples[0].isEmpty() ? "" : ", ") + paramDefinition + remaining;

                searchResult.getLines().remove(searchResult.getPos());
                searchResult.getLines().add(searchResult.getPos(), line);

                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeAllParams(Path typescriptPath, String functionName) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, functionName);
            if (searchResult != null && searchResult.getPos() != -1) {
                String line = searchResult.getLines().get(searchResult.getPos());
                line = line.substring(0, line.indexOf('(') + 1) + line.substring(line.indexOf(')'));
                searchResult.getLines().remove(searchResult.getPos());
                searchResult.getLines().add(searchResult.getPos(), line);

                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addVariable(Path typescriptPath, Param variable) {
        try {
            SearchResult sameVariableResult = searchLastWordPos(typescriptPath, variable.getName());

            SearchResult searchResult = searchLastWordPos(typescriptPath, "implements OnInit");
            if (searchResult != null && searchResult.getPos() != -1) {
                if (sameVariableResult.getPos() != -1) {
                    searchResult.getLines().remove(sameVariableResult.getPos() - 1);
                    searchResult.getLines().remove(sameVariableResult.getPos() - 1);
                    searchResult.getLines().remove(sameVariableResult.getPos() - 1);
                }

                String lineToAdd = "\n" + variable.getAccessibility() + " " + variable.getName() + ": " + variable.getType() + "\n";
                searchResult.getLines().add(searchResult.getPos() + 1, lineToAdd);

                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToNgOnInit(Path typescriptPath, String body) {
        try {
            SearchResult searchResult = searchLastWordPos(typescriptPath, "ngOnInit");
            if (searchResult != null && searchResult.getPos() != -1) {
                int i = searchResult.getPos() + 1;
                while (!searchResult.getLines().get(i).contains("}")) {
                    searchResult.getLines().remove(i);
                }
                searchResult.getLines().add(i, body);
                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addFunction(Path typescriptPath, Path templateFilePath, String functionName, List<Param> params, List<String> extras) {
        try {
            deleteFunction(typescriptPath, functionName);
            SearchResult searchResult = searchLastWordPos(typescriptPath, "}");
            if (searchResult != null && searchResult.getPos() != -1) {

                String builtParams = "";
                if (params != null) {
                    for (Param param : params) {
                        builtParams += param.getAccessibility() + " " + param.getName() + ": " + param.getType() + ", ";
                    }
                    builtParams = builtParams.substring(0, builtParams.length() - 2);
                }

                String builtExtras = "";
                if (extras != null) {
                    for (String extra : extras) {
                        builtExtras += extra;
                    }
                }

                String functionTemplate = new String(Files.readAllBytes(templateFilePath));
                functionTemplate = functionTemplate.replaceAll("#functionName#", functionName).replaceAll("#params#", builtParams).replace("#extras#", builtExtras);

                searchResult.getLines().add(searchResult.getPos(), functionTemplate);

                Files.write(typescriptPath, searchResult.getLines(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFunction(Path typescriptPath, String functionName) {
        try {
            SearchResult startSearchResult = searchLastWordPos(typescriptPath, "/*start" + functionName + "*/");
            if (startSearchResult != null && startSearchResult.getPos() != -1) {
                SearchResult endSearchResult = searchLastWordPos(typescriptPath, "/*end" + functionName + "*/");
                List<String> newLines = new ArrayList<>();
                for (int i = 0; i < endSearchResult.getLines().size(); i++) {
                    if (i < startSearchResult.getPos() || i > endSearchResult.getPos()) {
                        newLines.add(endSearchResult.getLines().get(i));
                    }
                }

                Files.write(typescriptPath, newLines, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
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

}
