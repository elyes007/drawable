package tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation;

import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.*;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.FailedToCreateHtmlFromIonApp;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoDetectedObjects;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoFramesDetected;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CodeGenerator {

    public static File parse(List<DetectedObject> detectedObjects) throws NoDetectedObjects, NoFramesDetected, FailedToCreateHtmlFromIonApp {
        if (detectedObjects == null || detectedObjects.isEmpty()) {
            throw new NoDetectedObjects("No detected objects");
        }
        //find frame and remove it from list
        List<DetectedObject> frames = new ArrayList<>();
        List<DetectedObject> objects = new ArrayList<>();
        for (DetectedObject object : detectedObjects) {
            if (object.getClasse() == DetectedObject.FRAME) {
                frames.add(object);
            } else {
                objects.add(object);
            }
        }
        if (frames.isEmpty()) {
            throw new NoFramesDetected("No frames detected");
        }
        if (objects.isEmpty()) {
            throw new NoDetectedObjects("No detected objects inside frames");
        }

        DetectedObject topFrame = frames.get(0);
        DetectedObject bottomFrame = frames.get(0);
        for (DetectedObject object : frames) {
            if (object.getBox().getyMin() < topFrame.getBox().getyMin()) {
                topFrame = object;
            }
            if (object.getBox().getyMin() > bottomFrame.getBox().getyMin()) {
                bottomFrame = object;
            }
        }
        if (topFrame == bottomFrame) {
            //return parseWithSingleFrame(topFrame, objects);
            return null;
        } else {
            return parseWithDoubleFrames(topFrame, bottomFrame, objects);
        }
    }

    private static File parseWithDoubleFrames(DetectedObject topFrame, DetectedObject bottomFrame, List<DetectedObject> objects) throws FailedToCreateHtmlFromIonApp {
        List<IonView> views = new ArrayList<>();
        int i = 1;
        int j = 1;
        int k = 1;
        for (DetectedObject object : objects) {
            IonView view = getViewInstance(object);
            if (view == null) continue;
            //view.setId(view.getClass().getSimpleName() + i);
            if (view instanceof IonButton) {
                ((IonButton) view).setText("Button " + j);
                j++;
            }
            if (view instanceof IonItem) {
                ((IonItem) view).getLabel().setLabel("Input " + k);
                k++;
            }
            if (view instanceof IonImg) {
                double heightPercent = object.getBox().getHeight() / (bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax()) > 1 ? 100 : 100 * object.getBox().getHeight() / (bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax());
                view.setHeight(new DecimalFormat("#0.00").format(heightPercent));
                ((IonImg) view).setSrc(CodeGenerator.class.getResource("/codeGenerationModule/placeholder.png").getPath());
            }
            double widthPercent = object.getBox().getWidth() / topFrame.getBox().getWidth() > 1 ? 100 : 100 * object.getBox().getWidth() / topFrame.getBox().getWidth();
            view.setWidth(new DecimalFormat("#0.00").format(widthPercent) + "%");

            double top = Math.min(1, Math.max(0, object.getBox().getyMin() - topFrame.getBox().getyMax()) /
                    Math.max(0, bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax() - object.getBox().getHeight()));
            double left = Math.max(0, object.getBox().getxMin() - topFrame.getBox().getxMin()) /
                    Math.max(0, topFrame.getBox().getWidth() - object.getBox().getWidth());

            view.setTop(new DecimalFormat("#0.00").format(top * 100) + "%");
            view.setLeft(new DecimalFormat("#0.00").format(left * 100) + "%");

            views.add(view);
            i++;
        }

        try {
            return getHtml(buildLayout(views));
        } catch (JAXBException | IOException | URISyntaxException e) {
            e.printStackTrace();
            throw new FailedToCreateHtmlFromIonApp("Exception occurred while creating html file");
        }
    }


    private static IonView getViewInstance(DetectedObject object) {
        switch ((int) object.getClasse()) {
            case DetectedObject.BUTTON:
                return new IonButton();
            case DetectedObject.IMAGE:
                return new IonImg();
            case DetectedObject.EditText:
                return new IonItem();
        }
        return null;
    }

    private static IonApp buildLayout(List<IonView> views) {
        IonContent ionContent = new IonContent();

        for (IonView view : views) {
            if (view instanceof IonButton) {
                if (ionContent.getButtons() == null) {
                    ionContent.setButtons(new ArrayList<>());
                }
                ionContent.getButtons().add((IonButton) view);
            }
            if (view instanceof IonImg) {
                if (ionContent.getImages() == null) {
                    ionContent.setImages(new ArrayList<>());
                }
                ionContent.getImages().add((IonImg) view);
            }
            if (view instanceof IonItem) {
                if (ionContent.getItems() == null) {
                    ionContent.setItems(new ArrayList<>());
                }
                ionContent.getItems().add((IonItem) view);
            }
        }
        return new IonApp(ionContent);
    }

    public static File getHtml(IonApp app) throws JAXBException, IOException, URISyntaxException {
        //serializing IonApp to string
        JAXBContext jc = JAXBContext.newInstance(IonApp.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(app, sw);
        String body = sw.toString();

        //reading template html string and replacing title and body
        File htmlTemplateFile = new File(CodeGenerator.class.getResource("/codeGenerationModule/template.html").toURI());
        String htmlString = FileUtils.readFileToString(htmlTemplateFile);
        String title = app.getHeader().getToolbar().getTitle();
        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$body", body);
        File newHtmlFile = new File(CodeGenerator.class.getResource("/codeGenerationModule/").toURI().getPath() + "result.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);

        return newHtmlFile;
    }
}
