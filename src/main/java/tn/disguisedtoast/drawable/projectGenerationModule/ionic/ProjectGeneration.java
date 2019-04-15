package tn.disguisedtoast.drawable.projectGenerationModule.ionic;

import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;
import javafx.application.Platform;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.nio.cs.UTF_32;
import tn.disguisedtoast.drawable.homeModule.controllers.HomeController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.projectGenerationModule.generation.GlobalProjectGeneration;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProjectGeneration {

    public static Stage Stage;
    public static String splitn;
    private static String pagesPath;
    private static String linkHref;
    private static List<String> assets =new ArrayList<String>();


    public static void generateBlankProject(Stage st,String projectPath) {



          /*  DirectoryChooser dc = new DirectoryChooser();

            File f = dc.showDialog(st);
            String s = f.getAbsolutePath();*/
            System.out.println(projectPath);

           /* try {
                splitn = dialogSplit();
                System.out.println(splitn);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(projectPath));


            processBuilder.command("cmd.exe", "/c", "ionic start ionic_project blank ");
            try {

                Process process = processBuilder.start();


                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;

                while ((line = reader.readLine()) != null) {

                    System.out.println(line);
                }

                int exitCode = process.waitFor();

                System.out.println("\nExited with error code : " + exitCode);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
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

    public static void changePage(File srcFile) throws IOException {
       /* List<String> newLines = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8)) {
            if (line.contains("1313131")) {
                newLines.add(line.replace("", ""+System.currentTimeMillis()));
            } else {
                newLines.add(line);
            }
        }
        Files.write(Paths.get(fileName), newLines, StandardCharsets.UTF_8);*/
        Scanner scanner = new Scanner(srcFile);
         String text = scanner.useDelimiter("</html>").next();
        scanner.close();
        // System.out.println(text);
          String newScript = StringUtils.substringAfterLast(text, "<ion-app>");
         String finalScript=StringUtils.substringBefore(newScript,"</ion-app>");

        //FileUtils.writeStringToFile(srcFile,finalScript,Charset.forName("UTF-8"));
        System.out.println(finalScript);

         BufferedReader br = new BufferedReader(new FileReader(srcFile));

        String st;
        //while ((st = br.readLine()) != null) {

            //String assetPath = StringUtils.substringBetween(st, "src=", ".png");
            //System.out.println(st);

            //change src
            Document doc = Jsoup.parse(finalScript);
            Elements link = doc.select("ion-img");
        for (Element l:link) {
          linkHref = l.attr("src");
           assets.add(linkHref);// "http://example.com/"
            //String linkText = link.text();
            System.out.println("img:" + linkHref);
        }
            // String text = doc.body().text(); // "An example link"

         /// "http://example.com/"
            //String linkText = link.text();


       // }

           /* Pattern pattern = Pattern.compile("(?<=<ion-img src=\\\")[^\\\"]*");
            Matcher matcher = pattern.matcher(finalScript);
            while (matcher.find()) {
                String group = matcher.group(1);
                System.out.println("This is the image path:"+group);
            }*/

            //Find image in directory similar to  src file

            //if (linkHref != null) {
                for (int i = 0; i < assetList(srcFile.getPath()).size(); i++) {
                    System.out.println("asset:" + assetList(srcFile.getPath()).get(i));
                    if (assets.get(i).contains(assetList(srcFile.getPath()).get(i))) {
                        System.out.println("Path: " + assets.get(i));
                    }
                }

           // }




        //content = content.replaceAll("src=""", "bar");
        // Files.write(path, content.getBytes(charset));

        // FileUtils.writeStringToFile(srcFile, content);


    }

   /* public static void CopyAssets() throws IOException {
        String projectAssets = "\\src\\main\\RelatedFiles\\generated_views\\assets";
        File srcDir = new File(System.getProperty("user.dir") + projectAssets);

        File destDir = new File("C:\\Users\\DELL\\Desktop\\4sim\\ionic\\ttProject\\src\\assets");
        FileUtils.copyDirectory(srcDir, destDir);
        System.out.println("copied!");
    }*/


    public static List<String> assetList(String projectPath) {
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(projectPath+"\\src\\assets\\drawable");
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".png"))) {
                textFiles.add(file.getName());
                //System.out.println(file.getName());
            }
        }
        return textFiles;
    }


    public static void generatePages(String globalProjectPath) throws IOException {
        HomeController hc = new HomeController();
        List<Page> PagesList = hc.loadPages();
        for (Page p : PagesList) {
            System.out.println(p.toString());


            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.directory(new File(globalProjectPath+"\\ionic_project"));
            // TextField projectName = new TextField();


            processBuilder.command("cmd.exe", "/c", "ionic generate page" + " " + p.getName());

            try {

                Process process = processBuilder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {

                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                System.out.println("\nExited with error code : " + exitCode);
                copyFileUsingApacheCommonsIO(new File(System.getProperty("user.dir") + "\\" + p.getName() + "\\" + "ion-test.html"),
                        new File(globalProjectPath+"\\src\\app" + "\\" + p.getName() + "\\" + p.getName() + ".page.html"));
                process.destroy();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}