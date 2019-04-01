package tn.disguisedtoast.drawable.codeGenerationModule.ionic.tests;

import tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation.CodeGenerator;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.Box;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.DetectedObject;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.FailedToCreateHtmlFromIonApp;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoDetectedObjects;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoFramesDetected;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class TestShapeParsing {
    public static void main(String[] args) throws NoDetectedObjects, NoFramesDetected, FailedToCreateHtmlFromIonApp, JAXBException, IOException, URISyntaxException {
        List<DetectedObject> objects = new ArrayList<>();
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 0, 10, 2)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(3.5, 2, 6.5, 3)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.5, 3, 7.5, 4)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2, 6, 4, 8)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(5, 6.4, 8, 7.4)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(1, 10, 5, 13)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 14, 10, 15)));

        CodeGenerator.generateTempHtml(CodeGenerator.parse(objects));
    }
}
