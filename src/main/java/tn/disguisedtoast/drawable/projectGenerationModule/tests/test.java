package tn.disguisedtoast.drawable.projectGenerationModule.tests;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import tn.disguisedtoast.drawable.projectGenerationModule.ionic.ProjectGeneration;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class test extends Application {
    public static Stage Stage;
    //public static void main(String[] args) throws IOException {


        //ProjectGeneration.generatePages();
      //ProjectGeneration.changeFile(new File("C:\\\\Users\\\\DELL\\\\Desktop\\\\4sim\\\\pim\\\\drawable-app\\\\src\\\\main\\\\RelatedFiles\\\\generated_views\\\\pages\\\\page1\\\\ion-test.html"));
        //ProjectGeneration.generateBlankProject(Stage);
      //  System.out.println(System.getProperty("user.dir"));

        String s = "<script language=\"javascript\"><!--\r\n"
                + "            document.write('<a href=\"javascript:popupWindow(\\'https://www.kitchenniche.ca/prepara-adjustable-oil-pourer-pi-5597.html?invis=0\\')\">\r\n"
                + "<img src=\"images/imagecache/prepara-adjustable-oil-pourer-1.jpg\" border=\"0\" alt=\"Prepara Adjustable Oil Pourer\" title=\" Prepara Adjustable Oil Pourer \" width=\"170\" height=\"175\" hspace=\"5\" vspace=\"5\">\r\n"
                + "<br>\r\n" + "</a>');\r\n" + "--></script>";





    //}

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.Stage = primaryStage;
        Parent root = (new FXMLLoader(getClass().getResource("/layouts/projectGenerationViews/startView.fxml"))).load();
        primaryStage.setTitle("Drawable");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
       // height = primaryStage.getScene().getHeight();
       // width = primaryStage.getScene().getWidth();
        //primaryStage.setWidth(1366);
        //primaryStage.setHeight(768);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
}
