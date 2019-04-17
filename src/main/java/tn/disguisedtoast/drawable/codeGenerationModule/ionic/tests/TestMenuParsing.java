package tn.disguisedtoast.drawable.codeGenerationModule.ionic.tests;

import tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation.CodeGenerator;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.Box;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.DetectedObject;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.MissingFramesException;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoDetectedObjects;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TestMenuParsing {
    public static void main(String[] args) throws InterruptedException, MissingFramesException, ExecutionException, NoDetectedObjects, JAXBException, IOException, URISyntaxException {
        //List<DetectedObject> objects = getSimple();
        List<DetectedObject> objects = getTabs();

        CodeGenerator.generateTempHtml(CodeGenerator.parse(objects));
    }

    private static List<DetectedObject> getSimple() {
        List<DetectedObject> objects = new ArrayList<>();
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 0, 10.0 / 30, 2.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(3.5 / 30, 2.0 / 30, 6.5 / 30, 3.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.5 / 30, 3.0 / 30, 7.5 / 30, 4.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.0 / 30, 6.0 / 30, 4.0 / 30, 8.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(-3.0 / 30, 6.4 / 30, 8.0 / 30, 7.4 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(5.0 / 30, 10.0 / 30, 15.0 / 30, 13.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 14.0 / 30, 10.0 / 30, 15.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.MENU, new Box(0, 0, 0, 0)));
        return objects;
    }

    private static List<DetectedObject> getTabs() {
        List<DetectedObject> objects = new ArrayList<>();
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 0, 10.0 / 30, 2.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(3.5 / 30, 2.0 / 30, 6.5 / 30, 3.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.5 / 30, 3.0 / 30, 7.5 / 30, 4.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.0 / 30, 6.0 / 30, 4.0 / 30, 8.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(5.0 / 30, 6.4 / 30, 8.0 / 30, 7.4 / 30)));
        objects.add(new DetectedObject(DetectedObject.TEXT, new Box(3.0 / 30, 8.0 / 30, 6.0 / 30, 9.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(1.0 / 30, 10.0 / 30, 5.0 / 30, 13.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 14.0 / 30, 10.0 / 30, 15.0 / 30)));

        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(10.0 / 30, 0, 20.0 / 30, 2.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(13.5 / 30, 2.0 / 30, 16.5 / 30, 3.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(12.5 / 30, 3.0 / 30, 17.5 / 30, 4.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(12.0 / 30, 6.0 / 30, 14.0 / 30, 8.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(11.0 / 30, 10.0 / 30, 15.0 / 30, 13.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(10.0 / 30, 14.0 / 30, 20.0 / 30, 15.0 / 30)));

        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(20.0 / 30, 0, 30.0 / 30, 2.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(22.5 / 30, 3.0 / 30, 27.5 / 30, 4.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(22.0 / 30, 6.0 / 30, 24.0 / 30, 8.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(25.0 / 30, 6.4 / 30, 28.0 / 30, 7.4 / 30)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(21.0 / 30, 10.0 / 30, 25.0 / 30, 13.0 / 30)));
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(20.0 / 30, 14.0 / 30, 30.0 / 30, 15.0 / 30)));

        objects.add(new DetectedObject(DetectedObject.MENU, new Box(0, 0, 0, 0)));

        return objects;
    }
}
