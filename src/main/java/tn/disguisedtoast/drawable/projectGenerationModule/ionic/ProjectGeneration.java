package tn.disguisedtoast.drawable.projectGenerationModule.ionic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import tn.disguisedtoast.drawable.storyboardModule.controllers.StoryboardViewController;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;
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

    private static String getPackageName() {
        FileReader fileReader;
        String filePath = Drawable.projectPath + File.separator + "state.json";
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "tn.esprit.TestApp";
        }
        JsonObject projectsJson = new JsonParser().parse(fileReader).getAsJsonObject();
        try {
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectsJson.get("package_name").getAsString();
    }

    public static boolean generateBlankProject() {
        generationInProcess = true;
        GlobalViewController.BackgroundProcess backgroundProcess1 = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Resolving IONIC project.", null));

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath));
        processBuilder.command("cmd.exe", "/c", "ionic start ionic_project blank --cordova --package-id " + getPackageName());
        processBuilder.redirectErrorStream(true);
        try {
            backgroundProcess1.setProcess(processBuilder.start());
            BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess1.getProcess().getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = backgroundProcess1.getProcess().exitValue();
            GlobalViewController.stopBackgroundProcess(backgroundProcess1);

            GlobalViewController.BackgroundProcess backgroundProcess2 = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Resolving Firebase dependency.", null));
            installFirebaseDependency(backgroundProcess2);
            GlobalViewController.stopBackgroundProcess(backgroundProcess2);

            GlobalViewController.BackgroundProcess backgroundProcess3 = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Resolving Facebook dependency.", null));
            installFacebookCordovaPlugin(backgroundProcess3);
            GlobalViewController.stopBackgroundProcess(backgroundProcess3);

            loadIonicLab();

            System.out.println("\nThis Exited with code : " + exitCode);
            generationInProcess = false;
            /*try {
                loadIonicLab();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return exitCode == 0;

        } catch (IOException e) {
            e.printStackTrace();
            generationInProcess = false;
            return false;
        }


    }

    public static boolean loadIonicLab() throws IOException {
        GlobalViewController.BackgroundProcess backgroundProcess = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Loading IONIC Lab.", null));
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + "\\ionic_project"));
        processBuilder.command("cmd.exe", "/c", "npm i @ionic/lab");
        backgroundProcess.setProcess(processBuilder.start());
        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
        System.out.println("\nExited with exit code : " + exitCode);

        GlobalViewController.stopBackgroundProcess(backgroundProcess);
        return exitCode == 0;
    }


    private static void installFacebookCordovaPlugin(GlobalViewController.BackgroundProcess backgroundProcess) throws IOException {

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "npm install --save @ionic-native/facebook");
        processBuilder.redirectErrorStream(true);
        backgroundProcess.setProcess(processBuilder.start());

        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Installing fb: " + line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
        System.out.println("\nExited with code : " + exitCode);
    }

    private static void installFirebaseDependency(GlobalViewController.BackgroundProcess backgroundProcess) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "npm install firebase @angular/fire");
        processBuilder.redirectErrorStream(true);
        backgroundProcess.setProcess(processBuilder.start());

        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Installing fb: " + line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
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
        String projectAssets = (Drawable.projectPath + "\\RelatedFiles\\assets");
        File srcDir = new File(projectAssets);

        File destDir = new File((Drawable.projectPath + "\\ionic_project\\src\\assets"));
        FileUtils.copyDirectory(srcDir, destDir);
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

    public static void deleteAssets() {
        File index = new File(Drawable.projectPath + "\\ionic_project\\src\\assets");
        File[] entries = index.listFiles();
        for (File f : entries) {

            if (!f.getPath().contains("icon") && !f.getPath().contains("shapes.svg")) {
                System.out.printf("File:" + f.getPath());

                f.delete();


            }
        }
    }

    public static void deleteExistingPages() {
        File index = new File(Drawable.projectPath + "\\ionic_project\\src\\app");
        File[] entries = index.listFiles();
        for (File f : entries) {

            if (f.isDirectory()) {
                System.out.printf("File:" + f.getPath());
                try {
                    FileUtils.deleteDirectory(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getStartPage() {
        String path = Drawable.projectPath + File.separator + "state.json";
        FileReader reader = null;
        try {
            reader = new FileReader(path);
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
            if (jsonObject.has("start_page")) {
                closeReader(reader);
                String startFolder = jsonObject.get("start_page").getAsString();
                path = Drawable.projectPath + ("&RelatedFiles&pages&" + startFolder + "&conf.json").replace("&", File.separator);
                try {
                    reader = new FileReader(path);
                    jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                    return getPageName(jsonObject.get("page").getAsString());
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            List<Page> pages = StoryboardViewController.loadPages();
            if (pages.size() == 0) return null;

            String folderName = StringUtils.substringAfterLast(pages.get(0).getFolderName(), "/");
            jsonObject.addProperty("start_page", folderName);
            FileUtils.write(new File(path), jsonObject.toString());
            closeReader(reader);
            return getPageName(pages.get(0).getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeReader(reader);
        return null;
    }

    private static void closeReader(FileReader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generatePages() {
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Run this task in the background?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("This task can take a lot of time!");
            alert.setTitle("Run this task in the background?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.YES) {
                EveryWhereLoader.getInstance().stopLoader(null);
            }
        });
        new Thread(() -> {
            GlobalViewController.BackgroundProcess backgroundProcess1 = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Resolving Login dependencies.", null));
            installLoginDependencies(backgroundProcess1);
            GlobalViewController.stopBackgroundProcess(backgroundProcess1);

            GlobalViewController.BackgroundProcess backgroundProcess2 = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Setting up Firebase.", null));
            setUpFirebase();
            GlobalViewController.stopBackgroundProcess(backgroundProcess2);

            GlobalViewController.BackgroundProcess backgroundProcess = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Generating pages.", null));
            deleteExistingPages();
            deleteAssets();
            List<Page> PagesList = ScrollHomeLayoutController.loadPages();
            List<Page> tabs = getTabs(PagesList);
            for (Page p : PagesList) {
                try {
                    makePage(p, backgroundProcess);

                    String folderName = StringUtils.substringAfterLast(p.getFolderName(), File.separator);
                    JsonObject pageSettingsJsonObject = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "RelatedFiles" + File.separator + "pages" + File.separator + folderName + File.separator + "conf.json")).getAsJsonObject();
                    Path pageTsPage = Paths.get((Drawable.projectPath + "&ionic_project&src&app&" + getPageName(p.getName()) + "&" + getPageName(p.getName()) + ".page.ts").replace("&", File.separator));
                    if (pageSettingsJsonObject.has("actions") && !pageSettingsJsonObject.getAsJsonObject("actions").entrySet().isEmpty()) {
                        for (Map.Entry<String, JsonElement> prop : pageSettingsJsonObject.getAsJsonObject("actions").entrySet()) {
                            JsonObject action = prop.getValue().getAsJsonObject();
                            if (action.has("platform") && action.get("platform").getAsString().equals("facebook")) {

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

                                TypeScriptParser.removeAllParams(pageTsPage, "constructor");

                                TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "afAuth", "AngularFireAuth"));
                                TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "platfom", "Platform"));
                                TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "fb", "Facebook"));

                                List<String> extras = new ArrayList<>();

                                String destPageFolderName = action.get("destinationPageName").getAsString();
                                if (!destPageFolderName.isEmpty()) {
                                    String destPagePath = (Drawable.projectPath + "&RelatedFiles&pages&" + destPageFolderName + "&conf.json").replace("&", File.separator);
                                    JsonObject destPageConfig = new JsonParser().parse(new FileReader(destPagePath)).getAsJsonObject();
                                    String pageName = ProjectGeneration.getPageName(destPageConfig.get("page").getAsString());


                                    importElement = new ImportElement("@angular/router");
                                    importElement.getDependencies().add("Router");
                                    TypeScriptParser.addImport(pageTsPage, importElement);
                                    TypeScriptParser.addParameterToFunction(pageTsPage, "constructor", new Param("private", "router", "Router"));

                                    extras.add("this.router.navigateByUrl('/" + pageName + "');");
                                }

                                System.out.println("EXTRAASS: " + extras);

                                TypeScriptParser.addFunction(pageTsPage,
                                        Paths.get(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "RelatedFiles" + File.separator + "FirebaseLoginTools" + File.separator + "facebookLoginTemplate.txt"),
                                        "logFacebook" + prop.getKey(),
                                        null, extras);
                            }
                        }
                    }

                    if (pageSettingsJsonObject.has("bindings")) {
                        JsonObject bindingsObject = pageSettingsJsonObject.getAsJsonObject("bindings");
                        String ngOnInitBody = "";
                        for (Map.Entry<String, JsonElement> entry : bindingsObject.entrySet()) {
                            ImportElement importElement = new ImportElement("firebase/app");
                            importElement.getDependencies().add("auth");
                            TypeScriptParser.addImport(pageTsPage, importElement);

                            TypeScriptParser.addVariable(pageTsPage, new Param("", entry.getKey(), "string"));

                            System.out.println("Entry Value : " + entry.getValue());
                            if (entry.getValue().getAsString().equals("image")) {
                                ngOnInitBody += "   this." + entry.getKey() + " = auth().currentUser.photoURL + '?type=large'\n";
                            } else if (entry.getValue().getAsString().equals("name")) {
                                ngOnInitBody += "   this." + entry.getKey() + " = auth().currentUser.displayName\n";
                            } else if (entry.getValue().getAsString().equals("email")) {
                                ngOnInitBody += "   this." + entry.getKey() + " = auth().currentUser.email\n";
                            }
                        }
                        TypeScriptParser.addToNgOnInit(pageTsPage, ngOnInitBody);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            GlobalViewController.BackgroundProcess tabsBackgroundProcess = backgroundProcess;
            tabs.forEach(tab -> {
                try {
                    makePage(tab, tabsBackgroundProcess);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            makeAppRouting(PagesList);
            makeTabRouting(tabs);
            GlobalViewController.stopBackgroundProcess(backgroundProcess);

            //copy assets
            try {
                backgroundProcess = GlobalViewController.startBackgroundProcess(new GlobalViewController.BackgroundProcess("Copying assets.", null));
                CopyAssets();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GlobalViewController.stopBackgroundProcess(backgroundProcess);

            //copy themes
            try {
                String themesPath = Drawable.projectPath + "&RelatedFiles&themes".replace("&", File.separator);
                String themes = FileUtils.readFileToString(new File(themesPath), "UTF-8");
                themesPath = Drawable.projectPath + "&ionic_project&src&theme&variables.scss".replace("&", File.separator);
                FileUtils.write(new File(themesPath), themes, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            EveryWhereLoader.getInstance().stopLoader(null);
        }).start();
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
                        .replace("#Tab", getReversePageName(tabName));
                routingString.append(route).append("\n\t\t\t\t\t\t");
            });
            //write to file
            try {
                File routingTemplate = new File(ProjectGeneration.class
                        .getResource("/tabsGeneration/page_module_template").toURI());
                String routingFileString = FileUtils.readFileToString(routingTemplate);
                routingFileString = routingFileString.replace("#route", routingString.toString())
                        .replace("#page", page)
                        .replace("&Page", getReversePageName(page))
                        .replace("#tab", getPageName(tabList.get(0).getName()));
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
                    .replace("#Page", getReversePageName(pageName));
            routingString.append(route).append("\n\t");
        });
        //write to file
        try {
            File routingTemplate = new File(ProjectGeneration.class.getResource("/tabsGeneration/routing_template").toURI());
            String routingFileString = FileUtils.readFileToString(routingTemplate);
            routingFileString = routingFileString.replace("#route", routingString.toString());
            String startPage = getStartPage();
            startPage = startPage == null ? "" : startPage;
            routingFileString = routingFileString.replace("#start", startPage);
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
                    tabList.add(tabPage);
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

    private static void makePage(Page p, GlobalViewController.BackgroundProcess backgroundProcess) throws IOException {
        System.out.println(p.toString());

        String pageName = getPageName(p.getName());

        //create blank page
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + "\\ionic_project"));
        processBuilder.command("cmd.exe", "/c", "ionic generate page " + pageName);
        backgroundProcess.setProcess(processBuilder.start());
        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
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

    public static String getReversePageName(String pageName) {
        pageName = pageName.trim();
        for (int i = 0; i < pageName.length(); i++) {
            char c = pageName.charAt(i);
            if (i == 0) {
                c = (c + "").toUpperCase().charAt(0);
                pageName = c + pageName.substring(1);
            } else if (c == '-') {
                c = (pageName.charAt(i + 1) + "").toUpperCase().charAt(0);
                pageName = pageName.substring(0, i) + c + pageName.substring(i + 2);
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

    private static void installLoginDependencies(GlobalViewController.BackgroundProcess backgroundProcess) {
        try {
            JsonObject globalSettings = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (globalSettings.has("firebase")) {
                if (globalSettings.getAsJsonObject("firebase").getAsJsonObject("platforms").has("facebook")) {
                    JsonObject firebaseJsonObject = globalSettings.getAsJsonObject("firebase");
                    addFacebookCordovaPlugin(firebaseJsonObject, backgroundProcess);
                }
            }
            //return exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
        }
    }

    private static void addFacebookCordovaPlugin(JsonObject firebaseJsonObject, GlobalViewController.BackgroundProcess backgroundProcess) throws IOException {
        JsonObject facebookJsonObject = firebaseJsonObject.getAsJsonObject("platforms").getAsJsonObject("facebook");

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(Drawable.projectPath + File.separator + "ionic_project"));
        processBuilder.command("cmd.exe", "/c", "ionic cordova plugin add cordova-plugin-facebook4 --variable APP_ID=\"" + facebookJsonObject.get("appId").getAsString() + "\" --variable APP_NAME=\"" + facebookJsonObject.get("appName").getAsString() + "\"");
        processBuilder.redirectErrorStream(true);
        backgroundProcess.setProcess(processBuilder.start());

        BufferedReader reader = new BufferedReader(new InputStreamReader(backgroundProcess.getProcess().getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("Adding FB to Cordova: " + line);
        }
        int exitCode = backgroundProcess.getProcess().exitValue();
        System.out.println("\nExited with code : " + exitCode);
    }

    private static void writeHtmlToPage(Page page, String pageName) {
        try {
            Document doc = Jsoup.parse(page.getHtml());
            Elements imgs = doc.select("ion-img");
            System.out.println("imgs size: " + imgs.size());
            for (Element img : imgs) {
                String src = img.attr("src");
                if (src.contains("facebook")) {
                    src = img.id();
                    img.attr("[src]", src);
                    img.removeAttr("src");
                } else if (src.contains("assets")) {
                    src = "'" + StringUtils.substringAfterLast(src.replace("\\", "/"), "../") + "'";
                    img.attr("[src]", src);
                    img.removeAttr("src");
                }
            }

            Elements boundLabels = doc.select("ion-label[data-bind]");
            for (Element boundLabel : boundLabels) {
                boundLabel.text("{{" + boundLabel.id() + "}}");
            }

            Elements routerLinkElements = doc.select("*[[routerlink]]");
            JsonParser jsonParser = new JsonParser();
            for (Element routerLinkElement : routerLinkElements) {
                String destPageFolderName = routerLinkElement.attr("[routerlink]");
                String destPagePath = (Drawable.projectPath + "&RelatedFiles&pages&" + destPageFolderName + "&conf.json").replace("&", File.separator);
                JsonObject jsonObject = jsonParser.parse(new FileReader(destPagePath)).getAsJsonObject();

                String destPageName = jsonObject.get("page").getAsString();
                routerLinkElement.attr("[routerlink]", "'/" + ProjectGeneration.getPageName(destPageName) + "'");
            }

            File dest = new File((Drawable.projectPath + "&ionic_project&src&app&" + pageName + "&" + pageName + ".page.html")
                    .replace("&", File.separator));
            try {
                FileUtils.write(dest, doc.selectFirst("ion-app").html().replace("routerlink", "routerLink"));
            } catch (NullPointerException e) {
                FileUtils.write(dest, "<ion-content>\n\t"
                        + doc.selectFirst("ion-tab").html().replace("routerlink", "routerLink")
                        + "\n</ion-content>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
