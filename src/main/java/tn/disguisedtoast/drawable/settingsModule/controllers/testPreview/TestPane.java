package tn.disguisedtoast.drawable.settingsModule.controllers.testPreview;

import code_generation.entities.DetectedObject;
import code_generation.service.CodeGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import preview.PreviewController;
import preview.PreviewGenerator;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TestPane implements Initializable {
    @FXML public VBox previewPane;

    private SettingsViewController settingsViewController;
    private Node selectedNode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*CodeGenerator.ParseResult result = getFromJson();
        List<Node> nodes = PreviewGenerator.getNodes(result, 400, 600);
        for(Node node : nodes){
            node.setOnMouseClicked(event -> {
                settingsViewController.setComponent(node);
                if(selectedNode!= null) selectedNode.getStyleClass().remove("selected");
                node.getStyleClass().add("selected");
                selectedNode = node;
            });
        }
        previewPane.getChildren().addAll(nodes);*/
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/preview.fxml"));
        try {
            loader.load();
            loader.getLocation().openStream();
            PreviewController previewController = loader.getController();
            Node root = previewController.getRoot();
            previewPane.getChildren().add(root);
            for(Node node : previewController.start()){
                node.setOnMouseClicked(event -> {
                    settingsViewController.setComponent(node);
                    if(selectedNode!= null) selectedNode.getStyleClass().remove("selected");
                    node.getStyleClass().add("selected");
                    selectedNode = node;
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CodeGenerator.ParseResult getFromJson() {
        //String json = "[{\"box\": {\"ymin\": 0.040865182876586914, \"xmax\": 0.7273321747779846, \"ymax\": 0.171548992395401, \"xmin\": 0.16464856266975403}, \"score\": 0.9998561143875122, \"class\": 2.0}, {\"box\": {\"ymin\": 0.2122548669576645, \"xmax\": 0.4740588068962097, \"ymax\": 0.3058491349220276, \"xmin\": 0.20741981267929077}, \"score\": 0.9979639053344727, \"class\": 1.0}, {\"box\": {\"ymin\": 0.21429763734340668, \"xmax\": 0.7280877232551575, \"ymax\": 0.3078218698501587, \"xmin\": 0.54476398229599}, \"score\": 0.8325586915016174, \"class\": 1.0}, {\"box\": {\"ymin\": 0.3722515106201172, \"xmax\": 0.5906668901443481, \"ymax\": 0.6535466909408569, \"xmin\": 0.30715855956077576}, \"score\": 0.9962480664253235, \"class\": 2.0}, {\"box\": {\"ymin\": 0.692771315574646, \"xmax\": 0.812876284122467, \"ymax\": 0.7865605354309082, \"xmin\": 0.48643046617507935}, \"score\": 0.9434722661972046, \"class\": 1.0}, {\"box\": {\"ymin\": 0.8430420756340027, \"xmax\": 0.6217881441116333, \"ymax\": 0.9570510387420654, \"xmin\": 0.399746298789978}, \"score\": 0.9260500073432922, \"class\": 1.0}]";
        String json = "[{\"score\": 0.9703431725502014, \"class\": 2.0, \"box\": {\"ymin\": 0.11155825853347778, \"xmin\": 0.3074879050254822, \"xmax\": 0.6092404127120972, \"ymax\": 0.23529963195323944}}, {\"score\": 0.999929666519165, \"class\": 1.0, \"box\": {\"ymin\": 0.26207438111305237, \"xmin\": 0.3204402029514313, \"xmax\": 0.5158522725105286, \"ymax\": 0.36070990562438965}}, {\"score\": 0.9853319525718689, \"class\": 3.0, \"box\": {\"ymin\": 0.37872251868247986, \"xmin\": 0.3985820412635803, \"xmax\": 0.599473237991333, \"ymax\": 0.4650939404964447}}, {\"score\": 0.9999793767929077, \"class\": 4.0, \"box\": {\"ymin\": 0.4998987317085266, \"xmin\": 0.430580735206604, \"xmax\": 0.590684175491333, \"ymax\": 0.6517508625984192}}, {\"score\": 0.9151538014411926, \"class\": 3.0, \"box\": {\"ymin\": 0.657299280166626, \"xmin\": 0.3246595561504364, \"xmax\": 0.5204195976257324, \"ymax\": 0.7235527038574219}}, {\"score\": 0.981560230255127, \"class\": 2.0, \"box\": {\"ymin\": 0.8265131711959839, \"xmin\": 0.3116040825843811, \"xmax\": 0.6287856698036194, \"ymax\": 0.936105489730835}}]";
        List<DetectedObject> objects = new Gson().fromJson(json, new TypeToken<List<DetectedObject>>() {
        }.getType());
        return CodeGenerator.parse(objects);
    }

    public void setSettingsViewController(SettingsViewController settingsViewController){
        this.settingsViewController = settingsViewController;
    }
}
