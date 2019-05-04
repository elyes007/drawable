package tn.disguisedtoast.drawable.projectGenerationModule.ionic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.control.TextInputDialog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.ProjectMain.GlobalViewController;
import tn.disguisedtoast.drawable.homeModule.controllers.ScrollHomeLayoutController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.utils.typescriptParser.controller.TypeScriptParser;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.ImportElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.Param;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ProjectGeneration {

    private static String splitn;
    private static String pagesPath;
    private static List<String> assets = new ArrayList<String>();
    public static boolean generationInProcess;

    public static boolean generateBlankProject() {
        generationInProcess = true;

        GlobalViewController.startBackgroundProcess("Resolving IONIC project.");
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath));
        processBuilder.command("cmd.exe", "/c", "ionic start ionic_project blank --cordova --package-id tn.esprit.TestApp");
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.exitValue();

            GlobalViewController.startBackgroundProcess("Resolving Firebase dependency.");
            installFirebaseDependency();
            GlobalViewController.startBackgroundProcess("Resolving Facebook dependency.");
            installFacebookCordovaPlugin();

            System.out.println("\nThis Exited with code : " + exitCode);
            generationInProcess = false;

            GlobalViewController.stopBackgroundProcess();
            return exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            generationInProcess = false;
            return false;
        }
    }

    private static void installFacebookCordovaPlugin() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "npm install --save @ionic-native/facebook");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Installing fb: " + line);
        }
        int exitCode = process.exitValue();
        System.out.println("\nExited with code : " + exitCode);
    }

    private static void installFirebaseDependency() throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "npm install firebase @angular/fire");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Installing fb: " + line);
        }
        int exitCode = process.exitValue();
        System.out.println("\nExited with code : " + exitCode);
    }

    public static String dialogSplit() throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Project Name");
        dialog.setHeaderText("Enter your project title");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            splitn = result.get();
        }
        return splitn;
    }

    private static void copyFileUsingApacheCommonsIO(File source, File dest) throws IOException {
        FileUtils.copyFile(source, dest);
        System.out.println("copied successfully!");
    }

    public static void CopyAssets() throws IOException {
        String projectAssets = (Drawable.projectPath + "&RelatedFiles&assets").replace("&", File.separator);
        File srcDir = new File(projectAssets);

        File destDir = new File((Drawable.projectPath + "ionic_project&src&assets").replace("&", File.separator));
        FileUtils.copyDirectoryToDirectory(srcDir, destDir);
    }

    public static List<String> assetList(String projectPath) {
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(projectPath + "\\src\\assets\\drawable");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".png"))) {
                textFiles.add(file.getName());
            }
        }
        return textFiles;
    }

    public static void generatePages() throws IOException {
        installLoginDependencies();
        setUpFirebase();
        List<Page> PagesList = ScrollHomeLayoutController.loadPages();
        List<Page> tabs = getTabs(PagesList);

        for (Page p : PagesList) {
            try {
                makePage(p);

                String folderName = StringUtils.substringAfterLast(p.getFolderName(), File.separator);
                JsonObject pageSettingsJsonObject = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "RelatedFiles" + File.separator + "pages" + File.separator + folderName + File.separator + "conf.json")).getAsJsonObject();
                if (pageSettingsJsonObject.has("actions") && !pageSettingsJsonObject.getAsJsonObject("actions").entrySet().isEmpty()) {
                    for (Map.Entry<String, JsonElement> prop : pageSettingsJsonObject.getAsJsonObject("actions").entrySet()) {
                        JsonObject action = prop.getValue().getAsJsonObject();
                        if (action.has("platform") && action.get("platform").getAsString().equals("facebook")) {
                            Path pageTsPage = Paths.get(Drawable.projectPath + File.separator + "ionic_project" + File.separator + "src" + File.separator + "app" + File.separator + p.getName().trim().toLowerCase() + File.separator + p.getName().trim().toLowerCase() + ".page.ts");

                            ImportElement importElement = new ImportElement("@ionic-native/facebook/ngx");
                            importElement.getDependencies().add("Facebook");
                            TypeScriptParser.addImport(pageTsPage, importElement);

                            importElement = new ImportElement("@ionic/angular");
                            importElement.getDependencies().add("Platform");
                            TypeScriptParser.addImport(pageTsPage, importElement);

                            importElement = new ImportElement("@angular/fire/auth");
                            importElement.getDependencies().add("AngularFireAuth");
                            TypeScriptParser.addImport(pageTsPage, importElement);

                            importElement = new ImportElement("firebase/app");
                            importElement.getDependencies().add("auth");
                            TypeScriptParser.addImport(pageTsPage, importElement);

                            TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "afAuth", "AngularFireAuth"));
                            TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "platfom", "Platform"));
                            TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "fb", "Facebook"));

                            TypeScriptParser.addFunction(pageTsPage,
                                    Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "RelatedFiles" + File.separator + "FirebaseLoginTools" + File.separator + "facebookLoginTemplate.txt"),
                                    "logFacebook" + prop.getKey(),
                                    null);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tabs.forEach(page -> {
            try {
                makePage(page);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        makeAppRouting(PagesList);
        makeTabRouting(tabs);
        //copy assets
        CopyAssets();
    }

    private static void makeTabRouting(List<Page> tabs) {
        String routeTemplate = "{ path: '#tab', loadChildren: '../#tab/#tab.module##TabPageModule' },";
        Map<String, List<Page>> map = new HashMap<>();
        tabs.forEach(tab -> {
            map.putIfAbsent(tab.getTabParent(), new ArrayList<>());
            map.get(tab.getTabParent()).add(tab);
        });
        map.forEach((page, tabList) -> {
            //prepare routes
            StringBuilder routingString = new StringBuilder();
            tabList.forEach(tab -> {
                String tabName = getPageName(tab.getName());
                String route = routeTemplate.replace("#tab", tabName)
                        .replace("#Tab", tabName.substring(0, 1).toUpperCase()
                                + tabName.substring(1));//TODO: remove "-" and turn letter after to upper case
                routingString.append(route).append("\n");
            });
            //write to file
            try {
                File routingTemplate = new File(ProjectGeneration.class
                        .getResource("/tabsGeneration/page_module_template").toURI());
                String routingFileString = FileUtils.readFileToString(routingTemplate);
                routingFileString = routingFileString.replace("#route", routingString.toString())
                        .replace("#page", page)
                        .replace("#Page", page.substring(0, 1).toUpperCase() + page.substring(1));
                File routingFile = new File(Drawable.projectPath +
                        ("&ionic_project&src&app&" + page + "&" + page + ".module.ts").replace("&", File.separator));
                FileUtils.write(routingFile, routingFileString);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private static void makeAppRouting(List<Page> pagesList) {
        //prepare routes
        String routeTemplate = "{ path: '#page', loadChildren: './#page/#page.module##PagePageModule' },";
        StringBuilder routingString = new StringBuilder();
        pagesList.forEach(page -> {
            String pageName = getPageName(page.getName());
            String route = routeTemplate.replace("#page", pageName)
                    .replace("#Page", pageName.substring(0, 1).toUpperCase() + pageName.substring(1));
            routingString.append(route).append("\n");
        });
        //write to file
        try {
            File routingTemplate = new File(ProjectGeneration.class.getResource("/tabsGeneration/routing_template").toURI());
            String routingFileString = FileUtils.readFileToString(routingTemplate);
            routingFileString = routingFileString.replace("#route", routingString.toString());
            File routingFile = new File(Drawable.projectPath +
                    "&ionic_project&src&app&app-routing.module.ts".replace("&", File.separator));
            FileUtils.write(routingFile, routingFileString);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static List<Page> getTabs(List<Page> pagesList) {
        List<Page> tabList = new ArrayList<>();
        for (Page page : pagesList) {
            String folderName = StringUtils.substringAfterLast(page.getFolderName(), File.separator);
            try {
                Document document = Jsoup.parse(new File(page.getFolderName() + File.separator + folderName + ".html"), "UTF-8");
                Elements tabs = document.getElementsByTag("ion-tab");
                tabs.forEach(tab -> {
                    tab.attr("tab", getPageName(page.getName()) + "-" + tab.attr("tab"));
                    tab.attr("id", getPageName(page.getName()));
                    tab.remove();
                    Page tabPage = new Page();
                    tabPage.setName(tab.attr("tab"));
                    tabPage.setHtml(tab.toString());
                    tabPage.setTabParent(getPageName(page.getName()));
                });

                Elements tabButtons = document.getElementsByTag("ion-tab-button");
                tabButtons.forEach(button -> {
                    button.attr("tab", getPageName(page.getName()) + "-" + button.attr("tab"));
                });
                page.setHtml(document.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tabList;
    }

    private static void makePage(Page p) throws IOException {
        System.out.println(p.toString());

        String pageName = getPageName(p.getName());

        //create blank page
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + "\\ionic_project"));
        processBuilder.command("cmd.exe", "/c", "ionic generate page " + pageName);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.exitValue();
        System.out.println("\nExited with exit code : " + exitCode);

        //write html to page
        writeHtmlToPage(p, pageName);
    }

    public static String getPageName(String pageName) {
        pageName = pageName.trim();
        for (int i = 0; i < pageName.length(); i++) {
            char c = pageName.charAt(i);
            if (StringUtils.isAllUpperCase(c + "")) {
                c = (c + "").toLowerCase().charAt(0);
                if (i == 0) {
                    pageName = c + pageName.substring(i + 1);
                } else if (pageName.charAt(i - 1) == ' ') {
                    pageName = pageName.substring(0, i - 1) + "-" + c + pageName.substring(i + 1);
                } else if (pageName.charAt(i - 1) != '-') {
                    pageName = pageName.substring(0, i) + "-" + c + pageName.substring(i + 1);
                    i++;
                }
            }
        }
        return pageName;
    }

    private static void setUpFirebase() {
        copyFirebaseConfigFile();
        Path ionicAppModulePath = Paths.get(Drawable.projectPath + File.separator + "ionic_project" + File.separator + "src" + File.separator + "app" + File.separator + "app.module.ts");

        ImportElement importElement = new ImportElement("@angular/fire");
        importElement.getDependencies().add("AngularFireModule");
        TypeScriptParser.addImport(ionicAppModulePath, importElement);

        TypeScriptParser.addModuleTo(ionicAppModulePath, "imports", "AngularFireModule.initializeApp(FIREBASE_CONF)");

        importElement = new ImportElement("@angular/fire/auth");
        importElement.getDependencies().add("AngularFireAuthModule");
        TypeScriptParser.addImport(ionicAppModulePath, importElement);

        TypeScriptParser.addModuleTo(ionicAppModulePath, "imports", "AngularFireAuthModule");

        importElement = new ImportElement("@ionic-native/facebook/ngx");
        importElement.getDependencies().add("Facebook");
        TypeScriptParser.addImport(ionicAppModulePath, importElement);

        TypeScriptParser.addModuleTo(ionicAppModulePath, "providers", "Facebook");

        importElement = new ImportElement("./app.firebase.conf");
        importElement.getDependencies().add("FIREBASE_CONF");
        TypeScriptParser.addImport(ionicAppModulePath, importElement);
    }

    private static void copyFirebaseConfigFile() {
        try {
            Path firebaseConfigTemplatePath = Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "RelatedFiles" + File.separator + "FirebaseLoginTools" + File.separator + "firebaseConfig.txt");
            String content = new String(Files.readAllBytes(firebaseConfigTemplatePath));

            JsonObject globalSettings = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (globalSettings.has("firebase")) {
                JsonObject firebaseJsonObject = globalSettings.getAsJsonObject("firebase");

                content = content.replaceAll("#apiKey#", firebaseJsonObject.get("apiKey").getAsString())
                        .replaceAll("#projectId#", firebaseJsonObject.get("projectId").getAsString())
                        .replaceAll("#messagingSenderId#", firebaseJsonObject.get("messagingSenderId").getAsString());

                Path firebaseConfPath = Paths.get(Drawable.projectPath + File.separator + "ionic_project" + File.separator + "src" + File.separator + "app" + File.separator + "app.firebase.conf.ts");
                if (!Files.exists(firebaseConfPath)) {
                    Files.createFile(firebaseConfPath);
                }
                Files.write(firebaseConfPath, content.getBytes());
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void installLoginDependencies() {
        try {
            JsonObject globalSettings = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (globalSettings.has("firebase")) {
                if (globalSettings.getAsJsonObject("firebase").getAsJsonObject("platforms").has("facebook")) {
                    JsonObject firebaseJsonObject = globalSettings.getAsJsonObject("firebase");
                    addFacebookCordovaPlugin(firebaseJsonObject);
                }
            }
            //return exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }
    }

    private static void addFacebookCordovaPlugin(JsonObject firebaseJsonObject) throws IOException {
        JsonObject facebookJsonObject = firebaseJsonObject.getAsJsonObject("platforms").getAsJsonObject("facebook");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "ionic cordova plugin add cordova-plugin-facebook4 --variable APP_ID=\"" + facebookJsonObject.get("appId").getAsString() + "\" --variable APP_NAME=\"" + facebookJsonObject.get("appName").getAsString() + "\"");
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Adding FB to Cordova: " + line);
        }
        int exitCode = process.exitValue();
        System.out.println("\nExited with code : " + exitCode);
    }

    private static void writeHtmlToPage(Page page, String pageName) {
        try {
            Document doc = Jsoup.parse(page.getHtml());
            Elements imgs = doc.select("ion-img");
            System.out.println("imgs size: " + imgs.size());
            for (Element img : imgs) {
                String src = img.attr("src");
                src = StringUtils.substringAfterLast(src.replace("\\", "/"), "../");
                img.attr("[src]", "'" + src + "'");
                img.removeAttr("src");
            }
            File dest = new File((Drawable.projectPath + "&ionic_project&src&app&" + pageName + "&" + pageName + ".page.html")
                    .replace("&", File.separator));
            FileUtils.write(dest, doc.selectFirst("ion-app").html().replace("routerlink", "routerLink"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
