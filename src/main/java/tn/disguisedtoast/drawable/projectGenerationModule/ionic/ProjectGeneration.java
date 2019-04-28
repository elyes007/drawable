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
import tn.disguisedtoast.drawable.homeModule.controllers.HomeController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;
import tn.disguisedtoast.drawable.utils.typescriptParser.controller.TypeScriptParser;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.ImportElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.Param;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProjectGeneration {

    private static String splitn;
    private static String pagesPath;
    private static List<String> assets = new ArrayList<String>();
    public static boolean generationInProcess;

    public static boolean generateBlankProject() {
        generationInProcess = true;
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

            installFirebaseDependency();
            installFacebookCordovaPlugin();

            System.out.println("\nExited with code : " + exitCode);
            generationInProcess = false;
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
        EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
        installLoginDependencies();
        setUpFirebase();
        List<Page> PagesList = HomeController.loadPages();
        for (Page p : PagesList) {
            try {
                System.out.println(p.toString());
                //create blank page
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(new File(Drawable.projectPath + "\\ionic_project"));
                processBuilder.command("cmd.exe", "/c", "ionic generate page" + " " + p.getName().trim()
                        .replace(" ", "_"));
                Process process = processBuilder.start();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                int exitCode = process.exitValue();
                System.out.println("\nExited with exit code : " + exitCode);
                //write html to page
                writeHtmlToPage(p.getFolderName(), p.getName().trim().replace(" ", "_").toLowerCase());

                JsonObject pageSettingsJsonObject = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "RelatedFiles" + File.separator + "pages" + File.separator + p.getName() + File.separator + "conf.json")).getAsJsonObject();
                if (pageSettingsJsonObject.has("actions") && !pageSettingsJsonObject.getAsJsonObject("actions").entrySet().isEmpty()) {
                    for (Map.Entry<String, JsonElement> prop : pageSettingsJsonObject.getAsJsonObject("actions").entrySet()) {
                        if (prop.getValue().getAsJsonObject().get("platform").getAsString().equals("facebook")) {
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
        //copy assets
        CopyAssets();
        EveryWhereLoader.getInstance().stopLoader(null);
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

    private static void writeHtmlToPage(String folderPath, String pageName) {
        try {
            String folderName = StringUtils.substringAfterLast(folderPath, File.separator);
            File srcFile = new File((folderPath + "&" + folderName + ".html")
                    .replace("&", File.separator));
            Document doc = Jsoup.parse(srcFile, "UTF-8");
            Elements imgs = doc.select("ion-img");
            for (Element img : imgs) {
                String src = img.attr("src");
                src = StringUtils.substringAfterLast(src.replace("\\", "/"), "../");
                img.attr("[src]", "'" + src + "'");
                img.removeAttr("src");
            }
            File dest = new File((Drawable.projectPath + "&ionic_project&src&app&" + pageName + "&" + pageName + ".page.html")
                    .replace("&", File.separator));
            FileUtils.write(dest, doc.selectFirst("ion-app").html());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
