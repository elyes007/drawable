package tn.disguisedtoast.drawable.homeModule.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.utils.ImageViewPane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class PageCellViewController implements Initializable {
    @FXML
    private AnchorPane pageImagePane;
    @FXML
    private ImageView pageImage;
    @FXML
    private Label pageName;
    @FXML
    private BorderPane pagePane;
    @FXML
    private Button deleteButton;


    private Page page;
    private String pagesPath = System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pagePane.setOnMouseEntered(event -> {
            this.deleteButton.setVisible(true);
        });
        this.pagePane.setOnMouseExited(event -> {
            this.deleteButton.setVisible(false);

        });
        this.deleteButton.setOnMouseClicked(event -> {
            Page pageToDelete = new Page();

            System.out.println(page.getName());
            File files = new File(pagesPath + "/" + pageName.getText());

            // File folder = new File();
            String[] entries = files.list();
            for (String s : entries) {
                File currentFile = new File(files.getPath(), s);
                currentFile.delete();
            }


               /* FileInputStream imagePath = null;
                try {
                    imagePath = new FileInputStream(pagesPath+"\\"+pageName.getText()+"\\"+"snapshot.png");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {

                    imagePath.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Files.delete(Paths.get(pagesPath+"\\"+pageName.getText()+"\\"+"snapshot.png"));
                } catch (NoSuchFileException x) {
                    System.err.format("%s: no such" + " file or directory%n", pagesPath+"\\"+pageName.getText()+"\\"+"snapshot.png");
                } catch (DirectoryNotEmptyException x) {
                    System.err.format("%s not empty%n", pagesPath+"\\"+pageName.getText()+"/snapshot.png");
                } catch (IOException x) {
                    // File permission problems are caught here.
                    System.err.println(x);
                }*/


        });

    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /*public  void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) { //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }*/


    public void setPage(Page page, PageClickCallback callback) {
        this.page = page;

        ImageView imageView = new ImageView(page.getImage());
        imageView.setPreserveRatio(true);
        ImageViewPane imageViewPane = new ImageViewPane(imageView);

        pagePane.setCenter(imageViewPane);

        this.pageName.setText(page.getName());

        this.pagePane.setOnMouseClicked(event -> {
            callback.clicked(page);
        });
    }

    public interface PageClickCallback {
        void clicked(Page page);
    }

    public Pane getPagePane() {
        return pagePane;
    }

    public Page getPage() {
        return page;
    }
}
