package tn.disguisedtoast.drawable.ProjectMain;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Drawable extends javafx.application.Application {
    public static Stage globalStage;
    public static double height;
    public static double width;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.globalStage = primaryStage;
        Parent root = (new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"))).load();
        primaryStage.setTitle("Drawable");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
        height = primaryStage.getScene().getHeight();
        width = primaryStage.getScene().getWidth();
        //primaryStage.setWidth(1366);
        //primaryStage.setHeight(768);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

       /* File dir = new File("C:\\Users\\DELL\\Desktop\\4sim\\pim\\Drawable");
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept (File dir, String name) {
                return name.startsWith("f");
            }
        };
        String[] children = dir.list(filter);
        if (children == null) {
            System.out.println("Either dir does not exist or is not a directory");
        } else {
            for (int i = 0; i< children.length; i++) {
                String filename = children[i];
                System.out.println(filename);
            }
        }*/

    }


}
