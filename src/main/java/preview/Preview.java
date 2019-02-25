package preview;

import code_generation.entities.DetectedObject;
import code_generation.entities.views.Button;
import code_generation.entities.views.EditText;
import code_generation.entities.views.ImageView;
import code_generation.entities.views.View;
import code_generation.service.CodeGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Preview extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Preview");
        AnchorPane root = new AnchorPane();
        root.setPrefHeight(600);
        root.setPrefWidth(400);

        CodeGenerator.ParseResult result = getFromJson();
        root.getChildren().addAll(PreviewGenerator.getNodes(result, 400, 600));
        try {
            CodeGenerator.generateLayoutFile(result.getLayout());
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("error generating file");
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static CodeGenerator.ParseResult getFromJson() {
        String json = "[{\"box\": {\"ymin\": 0.040865182876586914, \"xmax\": 0.7273321747779846, \"ymax\": 0.171548992395401, \"xmin\": 0.16464856266975403}, \"score\": 0.9998561143875122, \"class\": 2.0}, {\"box\": {\"ymin\": 0.2122548669576645, \"xmax\": 0.4740588068962097, \"ymax\": 0.3058491349220276, \"xmin\": 0.20741981267929077}, \"score\": 0.9979639053344727, \"class\": 1.0}, {\"box\": {\"ymin\": 0.21429763734340668, \"xmax\": 0.7280877232551575, \"ymax\": 0.3078218698501587, \"xmin\": 0.54476398229599}, \"score\": 0.8325586915016174, \"class\": 1.0}, {\"box\": {\"ymin\": 0.3722515106201172, \"xmax\": 0.5906668901443481, \"ymax\": 0.6535466909408569, \"xmin\": 0.30715855956077576}, \"score\": 0.9962480664253235, \"class\": 2.0}, {\"box\": {\"ymin\": 0.692771315574646, \"xmax\": 0.812876284122467, \"ymax\": 0.7865605354309082, \"xmin\": 0.48643046617507935}, \"score\": 0.9434722661972046, \"class\": 1.0}, {\"box\": {\"ymin\": 0.8430420756340027, \"xmax\": 0.6217881441116333, \"ymax\": 0.9570510387420654, \"xmin\": 0.399746298789978}, \"score\": 0.9260500073432922, \"class\": 1.0}]";
        List<DetectedObject> objects = new Gson().fromJson(json, new TypeToken<List<DetectedObject>>() {
        }.getType());
        return CodeGenerator.parse(objects);
    }

    public static CodeGenerator.ParseResult getStatic() {

        Button button = new Button();
        button.setText("Button1");
        button.setHorizontalBias("0");
        button.setVerticalBias("0.1");
        button.setWidthPercent("0.2");
        button.setHeightPercent("0.1");

        EditText editText = new EditText();
        editText.setHint("Edit text 1");
        editText.setHorizontalBias("0.5");
        editText.setVerticalBias("0.1");
        editText.setWidthPercent("0.4");
        editText.setHeightPercent("0.1");

        ImageView imageView = new ImageView();
        imageView.setHorizontalBias("0.5");
        imageView.setVerticalBias("0.5");
        imageView.setWidthPercent("0.5");
        imageView.setHeightPercent("0.5");

        List<View> views = new ArrayList<>();
        views.add(button);
        views.add(editText);
        views.add(imageView);

        List<List<CodeGenerator.ObjectWrapper>> rows = new ArrayList<>();
        List<CodeGenerator.ObjectWrapper> row1 = new ArrayList<>();
        CodeGenerator.ObjectWrapper btnWrapper = new CodeGenerator.ObjectWrapper();
        btnWrapper.setView(button);
        row1.add(btnWrapper);
        CodeGenerator.ObjectWrapper textWrapper = new CodeGenerator.ObjectWrapper();
        textWrapper.setView(editText);
        row1.add(textWrapper);
        rows.add(row1);

        List<CodeGenerator.ObjectWrapper> row2 = new ArrayList<>();
        CodeGenerator.ObjectWrapper imgWrapper = new CodeGenerator.ObjectWrapper();
        imgWrapper.setView(imageView);
        row2.add(imgWrapper);
        rows.add(row2);

        return new CodeGenerator.ParseResult(false, views, rows, null);
    }
}
