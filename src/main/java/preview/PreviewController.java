package preview;

import code_generation.entities.DetectedObject;
import code_generation.entities.views.ConstraintLayout;
import code_generation.entities.views.View;
import code_generation.service.CodeGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviewController {
    @FXML
    public AnchorPane preview;
    @FXML
    private AnchorPane root;

    public List<Node> start() {
        /*try {
            fromFile();
        } catch (JAXBException e) {
            e.printStackTrace();
        }*/
        return oneTimeStatic();
    }

    public void update(CodeGenerator.ParseResult result) {
        List<Node> nodes = PreviewGenerator
                .getNodes(result, preview.getPrefWidth(), preview.getPrefHeight());
        System.out.println("nodes: " + nodes.size());
        Platform.runLater(() -> {
            System.out.println("inside run later");
            preview.getChildren().clear();
            preview.getChildren().addAll(nodes);
        });
    }

    public void fromFile() throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(ConstraintLayout.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        File file = new File("../AndroidTest/app/src/main/res/layout/test1.xml");
        ConstraintLayout layout = (ConstraintLayout) unmarshaller.unmarshal(file);
        List<View> views = new ArrayList<>();
        views.addAll(layout.getButtons());
        views.addAll(layout.getEditTexts());
        views.addAll(layout.getImageViews());
        System.out.println("views: " + views.size());
        CodeGenerator.ParseResult result = new CodeGenerator.ParseResult();
        result.setHasSingleFrame(false);
        result.setViews(views);
        List<Node> nodes = PreviewGenerator
                .getNodes(result, preview.getPrefWidth(), preview.getPrefHeight());
        System.out.println("nodes: " + nodes.size());
        Platform.runLater(() -> {
            System.out.println("inside run later");
            preview.getChildren().clear();
            preview.getChildren().addAll(nodes);
        });
    }

    public List<Node> oneTimeStatic(){
        String json = "[{\"score\": 0.9703431725502014, \"class\": 2.0, \"box\": {\"ymin\": 0.11155825853347778, \"xmin\": 0.3074879050254822, \"xmax\": 0.6092404127120972, \"ymax\": 0.23529963195323944}}, {\"score\": 0.999929666519165, \"class\": 1.0, \"box\": {\"ymin\": 0.26207438111305237, \"xmin\": 0.3204402029514313, \"xmax\": 0.5158522725105286, \"ymax\": 0.36070990562438965}}, {\"score\": 0.9853319525718689, \"class\": 3.0, \"box\": {\"ymin\": 0.37872251868247986, \"xmin\": 0.3985820412635803, \"xmax\": 0.599473237991333, \"ymax\": 0.4650939404964447}}, {\"score\": 0.9999793767929077, \"class\": 4.0, \"box\": {\"ymin\": 0.4998987317085266, \"xmin\": 0.430580735206604, \"xmax\": 0.590684175491333, \"ymax\": 0.6517508625984192}}, {\"score\": 0.9151538014411926, \"class\": 3.0, \"box\": {\"ymin\": 0.657299280166626, \"xmin\": 0.3246595561504364, \"xmax\": 0.5204195976257324, \"ymax\": 0.7235527038574219}}, {\"score\": 0.981560230255127, \"class\": 2.0, \"box\": {\"ymin\": 0.8265131711959839, \"xmin\": 0.3116040825843811, \"xmax\": 0.6287856698036194, \"ymax\": 0.936105489730835}}]";
        List<DetectedObject> objects = new Gson().fromJson(json, new TypeToken<List<DetectedObject>>() {
        }.getType());
        List<Node> nodes = PreviewGenerator
                .getNodes(CodeGenerator.parse(objects), preview.getPrefWidth(), preview.getPrefHeight());
        Platform.runLater(() -> {
            preview.getChildren().clear();
            preview.getChildren().addAll(nodes);
        });
        return nodes;
    }

    public void fromStatic() {
        List<String> jsonList = new ArrayList<>();
        /*jsonList.add("[{\"box\": {\"ymin\": 0.025714846327900887, \"xmax\": 0.6734870672225952, \"ymax\": 0.1516139805316925, \"xmin\": 0.11724734306335449}, \"score\": 0.999972939491272, \"class\": 2.0}, {\"box\": {\"ymin\": 0.1873399019241333, \"xmax\": 0.6572803854942322, \"ymax\": 0.2848241627216339, \"xmin\": 0.09451477229595184}, \"score\": 0.8894684314727783, \"class\": 1.0}, {\"box\": {\"ymin\": 0.32904940843582153, \"xmax\": 0.6667465567588806, \"ymax\": 0.5112229585647583, \"xmin\": 0.45405611395835876}, \"score\": 0.9979177117347717, \"class\": 2.0}, {\"box\": {\"ymin\": 0.3343687951564789, \"xmax\": 0.4240373373031616, \"ymax\": 0.43329837918281555, \"xmin\": 0.14275193214416504}, \"score\": 0.8916385769844055, \"class\": 1.0}, {\"box\": {\"ymin\": 0.468723326921463, \"xmax\": 0.4361937642097473, \"ymax\": 0.5646662712097168, \"xmin\": 0.14424824714660645}, \"score\": 0.9951849579811096, \"class\": 1.0}, {\"box\": {\"ymin\": 0.7543667554855347, \"xmax\": 0.6917593479156494, \"ymax\": 0.9149238467216492, \"xmin\": 0.14663562178611755}, \"score\": 0.9999821186065674, \"class\": 2.0}]");
        jsonList.add("[{\"box\": {\"ymin\": 0.016395270824432373, \"xmax\": 0.7258040308952332, \"ymax\": 0.11139510571956635, \"xmin\": 0.062356315553188324}, \"score\": 0.9999881982803345, \"class\": 2.0}, {\"box\": {\"ymin\": 0.025009112432599068, \"xmax\": 0.7368699908256531, \"ymax\": 0.10822702944278717, \"xmin\": 0.32776495814323425}, \"score\": 0.9618628621101379, \"class\": 2.0}, {\"box\": {\"ymin\": 0.13623343408107758, \"xmax\": 0.7395766973495483, \"ymax\": 0.38101688027381897, \"xmin\": 0.5247385501861572}, \"score\": 0.9975408315658569, \"class\": 2.0}, {\"box\": {\"ymin\": 0.16348318755626678, \"xmax\": 0.4583187997341156, \"ymax\": 0.24609439074993134, \"xmin\": 0.07989010959863663}, \"score\": 0.8506920337677002, \"class\": 1.0}, {\"box\": {\"ymin\": 0.3143550455570221, \"xmax\": 0.510962963104248, \"ymax\": 0.430195689201355, \"xmin\": 0.06380912661552429}, \"score\": 0.8270204067230225, \"class\": 1.0}, {\"box\": {\"ymin\": 0.4802985191345215, \"xmax\": 0.6668537855148315, \"ymax\": 0.7226132154464722, \"xmin\": 0.1673123687505722}, \"score\": 0.9946135878562927, \"class\": 2.0}, {\"box\": {\"ymin\": 0.7992014288902283, \"xmax\": 0.6791974306106567, \"ymax\": 0.9526691436767578, \"xmin\": 0.08723928034305573}, \"score\": 0.9072731137275696, \"class\": 1.0}]");
        jsonList.add("[{\"box\": {\"ymin\": 0.012067390605807304, \"xmax\": 0.6467838287353516, \"ymax\": 0.09356971085071564, \"xmin\": 0.09848043322563171}, \"score\": 0.9999938011169434, \"class\": 2.0}, {\"box\": {\"ymin\": 0.10611597448587418, \"xmax\": 0.6688644289970398, \"ymax\": 0.18377980589866638, \"xmin\": 0.41415777802467346}, \"score\": 0.9705276489257812, \"class\": 1.0}, {\"box\": {\"ymin\": 0.10954340547323227, \"xmax\": 0.3605649769306183, \"ymax\": 0.29378557205200195, \"xmin\": 0.0997256264090538}, \"score\": 0.9981801509857178, \"class\": 2.0}, {\"box\": {\"ymin\": 0.21654166281223297, \"xmax\": 0.6746408343315125, \"ymax\": 0.2840975224971771, \"xmin\": 0.4199020564556122}, \"score\": 0.9778462052345276, \"class\": 1.0}, {\"box\": {\"ymin\": 0.33130982518196106, \"xmax\": 0.5925101041793823, \"ymax\": 0.40880081057548523, \"xmin\": 0.23642855882644653}, \"score\": 0.9942663311958313, \"class\": 1.0}, {\"box\": {\"ymin\": 0.43194296956062317, \"xmax\": 0.6656274795532227, \"ymax\": 0.5113154053688049, \"xmin\": 0.44246482849121094}, \"score\": 0.9909635782241821, \"class\": 1.0}, {\"box\": {\"ymin\": 0.5298413038253784, \"xmax\": 0.6755447387695312, \"ymax\": 0.7721793055534363, \"xmin\": 0.3784107565879822}, \"score\": 0.9782417416572571, \"class\": 2.0}, {\"box\": {\"ymin\": 0.8122527003288269, \"xmax\": 0.6851688027381897, \"ymax\": 0.9762448668479919, \"xmin\": 0.06944391876459122}, \"score\": 0.999618649482727, \"class\": 2.0}]");
        jsonList.add("[{\"box\": {\"ymin\": 0.01581699028611183, \"xmax\": 0.6475163698196411, \"ymax\": 0.12089867889881134, \"xmin\": 0.08967076987028122}, \"score\": 0.9999881982803345, \"class\": 2.0}, {\"box\": {\"ymin\": 0.12883709371089935, \"xmax\": 0.6634530425071716, \"ymax\": 0.23044073581695557, \"xmin\": 0.40666040778160095}, \"score\": 0.9940405488014221, \"class\": 1.0}, {\"box\": {\"ymin\": 0.1533840447664261, \"xmax\": 0.3469344675540924, \"ymax\": 0.3606189489364624, \"xmin\": 0.0940157100558281}, \"score\": 0.999599277973175, \"class\": 2.0}, {\"box\": {\"ymin\": 0.26969030499458313, \"xmax\": 0.676900327205658, \"ymax\": 0.35914477705955505, \"xmin\": 0.4122067093849182}, \"score\": 0.9947493672370911, \"class\": 1.0}, {\"box\": {\"ymin\": 0.41563326120376587, \"xmax\": 0.5954150557518005, \"ymax\": 0.5158135890960693, \"xmin\": 0.2366953343153}, \"score\": 0.9921473860740662, \"class\": 1.0}, {\"box\": {\"ymin\": 0.5463553667068481, \"xmax\": 0.6632205247879028, \"ymax\": 0.649465799331665, \"xmin\": 0.44401276111602783}, \"score\": 0.9888402819633484, \"class\": 1.0}, {\"box\": {\"ymin\": 0.6770913600921631, \"xmax\": 0.6707838177680969, \"ymax\": 0.9581685066223145, \"xmin\": 0.3807696998119354}, \"score\": 0.9914342164993286, \"class\": 2.0}]");
        jsonList.add("[{\"box\": {\"ymin\": 0.03821076825261116, \"xmax\": 0.7488583922386169, \"ymax\": 0.13521051406860352, \"xmin\": 0.1799294650554657}, \"score\": 0.9999219179153442, \"class\": 2.0}, {\"box\": {\"ymin\": 0.16840465366840363, \"xmax\": 0.7196097373962402, \"ymax\": 0.24481137096881866, \"xmin\": 0.5433585047721863}, \"score\": 0.817418098449707, \"class\": 2.0}, {\"box\": {\"ymin\": 0.16856978833675385, \"xmax\": 0.4790855050086975, \"ymax\": 0.243069589138031, \"xmin\": 0.20463848114013672}, \"score\": 0.9988706707954407, \"class\": 1.0}, {\"box\": {\"ymin\": 0.2849094271659851, \"xmax\": 0.5940459966659546, \"ymax\": 0.5119214653968811, \"xmin\": 0.3065434396266937}, \"score\": 0.9984373450279236, \"class\": 2.0}, {\"box\": {\"ymin\": 0.5362015962600708, \"xmax\": 0.8081621527671814, \"ymax\": 0.6060941815376282, \"xmin\": 0.4857512414455414}, \"score\": 0.9022560119628906, \"class\": 1.0}, {\"box\": {\"ymin\": 0.6555392146110535, \"xmax\": 0.6144459247589111, \"ymax\": 0.7385701537132263, \"xmin\": 0.40026524662971497}, \"score\": 0.9646076560020447, \"class\": 1.0}, {\"box\": {\"ymin\": 0.8161802887916565, \"xmax\": 0.7786926627159119, \"ymax\": 0.9809336066246033, \"xmin\": 0.17182320356369019}, \"score\": 0.9999579191207886, \"class\": 2.0}]");
        jsonList.add("[{\"box\": {\"ymin\": 0.040865182876586914, \"xmax\": 0.7273321747779846, \"ymax\": 0.171548992395401, \"xmin\": 0.16464856266975403}, \"score\": 0.9998561143875122, \"class\": 2.0}, {\"box\": {\"ymin\": 0.2122548669576645, \"xmax\": 0.4740588068962097, \"ymax\": 0.3058491349220276, \"xmin\": 0.20741981267929077}, \"score\": 0.9979639053344727, \"class\": 1.0}, {\"box\": {\"ymin\": 0.21429763734340668, \"xmax\": 0.7280877232551575, \"ymax\": 0.3078218698501587, \"xmin\": 0.54476398229599}, \"score\": 0.8325586915016174, \"class\": 1.0}, {\"box\": {\"ymin\": 0.3722515106201172, \"xmax\": 0.5906668901443481, \"ymax\": 0.6535466909408569, \"xmin\": 0.30715855956077576}, \"score\": 0.9962480664253235, \"class\": 2.0}, {\"box\": {\"ymin\": 0.692771315574646, \"xmax\": 0.812876284122467, \"ymax\": 0.7865605354309082, \"xmin\": 0.48643046617507935}, \"score\": 0.9434722661972046, \"class\": 1.0}, {\"box\": {\"ymin\": 0.8430420756340027, \"xmax\": 0.6217881441116333, \"ymax\": 0.9570510387420654, \"xmin\": 0.399746298789978}, \"score\": 0.9260500073432922, \"class\": 1.0}]");
*/
        new Thread(() -> {
            for (String json : jsonList) {
                List<DetectedObject> objects = new Gson().fromJson(json, new TypeToken<List<DetectedObject>>() {
                }.getType());
                List<Node> nodes = PreviewGenerator
                        .getNodes(CodeGenerator.parse(objects), preview.getPrefWidth(), preview.getPrefHeight());
                Platform.runLater(() -> {
                    preview.getChildren().clear();
                    preview.getChildren().addAll(nodes);
                });
                System.out.println("adding " + new Date());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public AnchorPane getRoot() {
        return root;
    }

    public AnchorPane getPreview() {
        return preview;
    }
}
