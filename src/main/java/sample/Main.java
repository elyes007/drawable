package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.TextProgressMonitor;

import java.io.File;
import java.io.PrintWriter;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/layouts/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) throws GitAPIException {
        cloneRepo();
        launch(args);
    }

    public static void cloneRepo() throws GitAPIException {
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI("https://github.com/CodingLandChallengers/Node.git")
                .setDirectory(new File("D:\\Users\\mabde\\Desktop\\test_java"))
                .setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out)));
        cloneCommand.call();
    }
}
