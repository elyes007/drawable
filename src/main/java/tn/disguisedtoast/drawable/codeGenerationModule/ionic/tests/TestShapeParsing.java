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
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 0, 10 / 30, 2 / 30)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(3.5 / 30, 2 / 30, 6.5 / 30, 3 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.5 / 30, 3 / 30, 7.5 / 30, 4 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2 / 30, 6 / 30, 4 / 30, 8 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(5 / 30, 6.4 / 30, 8 / 30, 7.4 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(1 / 30, 10 / 30, 5 / 30, 13 / 30)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 14 / 30, 10 / 30, 15 / 30)));

        CodeGenerator.generateTempHtml(CodeGenerator.parse(objects));
    }
}
