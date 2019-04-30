package tn.disguisedtoast.drawable.projectGenerationModule.ionic;

import javafx.scene.control.TextInputDialog;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.homeModule.controllers.ScrollHomeLayoutController;
import tn.disguisedtoast.drawable.homeModule.models.Page;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
        processBuilder.command("cmd.exe", "/c", "ionic start ionic_project blank");
        processBuilder.redirectErrorStream(true);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            int exitCode = process.exitValue();
            System.out.println("\nExited with code : " + exitCode);
            generationInProcess = false;
            return exitCode == 0;
        } catch (IOException e) {
            e.printStackTrace();
            generationInProcess = false;
            return false;
        }
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
        List<Page> PagesList = ScrollHomeLayoutController.loadPages();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //copy assets
        CopyAssets();
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
