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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CodeGenerator {

    public static IonApp parse(List<DetectedObject> detectedObjects) throws NoDetectedObjects, NoFramesDetected, FailedToCreateHtmlFromIonApp {
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
            throw new NoFramesDetected("Only one frame detected");
        } else {
            return parseWithDoubleFrames(topFrame, bottomFrame, objects);
        }
    }

    private static IonApp parseWithDoubleFrames(DetectedObject topFrame, DetectedObject bottomFrame, List<DetectedObject> objects) throws FailedToCreateHtmlFromIonApp {
        List<IonView> views = new ArrayList<>();
        int i = 1;
        int j = 1;
        int k = 1;
        for (DetectedObject object : objects) {
            IonView view = getViewInstance(object);
            if (view == null) continue;
            if (view instanceof IonButton) {
                view.setId("Button " + j);
                ((IonButton) view).setText("Button " + j);
                j++;
            }
            if (view instanceof IonItem) {
                view.setId("Input" + k);
                ((IonItem) view).getLabel().setLabel("Input " + k);
                k++;
            }
            if (view instanceof IonImg) {
                view.setId("Image" + i);
                i++;
                double heightPercent = object.getBox().getHeight() / (bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax()) > 1 ? 100 : 100 * object.getBox().getHeight() / (bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax());
                view.setHeight(String.format(Locale.US, "%.2f", heightPercent) + "%");
                ((IonImg) view).setSrc(CodeGenerator.class.getResource("/codeGenerationModule/placeholder.png").getPath());
            }
            double widthPercent = object.getBox().getWidth() / topFrame.getBox().getWidth() > 1 ? 100 : 100 * object.getBox().getWidth() / topFrame.getBox().getWidth();
            view.setWidth(String.format(Locale.US, "%.2f", widthPercent) + "%");

            double top = (object.getBox().getyMin() - topFrame.getBox().getyMax())
                    / (bottomFrame.getBox().getyMin() - topFrame.getBox().getyMax());
            double left = (object.getBox().getxMin() - topFrame.getBox().getxMin())
                    / topFrame.getBox().getWidth();

            view.setTop(String.format(Locale.US, "%.2f", top * 100) + "%");
            view.setLeft(String.format(Locale.US, "%.2f", left * 100) + "%");

            views.add(view);
        }

        return buildLayout(views);
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

    public static String generateTempHtml(IonApp app) throws JAXBException, IOException, URISyntaxException {
        app.getHeader().getToolbar().setTitle("Page");

        //serializing IonApp to string
        JAXBContext jc = JAXBContext.newInstance(IonApp.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(app, sw);
        String body = sw.toString();

        //reading template html string and replacing title and body
        String tempPath = System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages\\temp\\";
        File htmlTemplateFile = new File(CodeGenerator.class.getResource("/codeGenerationModule/template.html").toURI());
        String htmlString = FileUtils.readFileToString(htmlTemplateFile);
        String title = app.getHeader().getToolbar().getTitle();
        htmlString = htmlString.replace("$title", title);
        htmlString = htmlString.replace("$body", body);

        //writing html file
        File newHtmlFile = new File(tempPath + "temp.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);

        return tempPath + "temp.html";
    }

    public static String generatePageFolder(IonApp app) throws JAXBException, IOException, URISyntaxException {
        //setting page name
        String pagesPath = System.getProperty("user.dir") + "\\src\\main\\RelatedFiles\\generated_views\\pages\\";
        File idFile = new File(pagesPath + "\\.last_id");
        int id = 1;
        //in case last_id file doesn't exist
        try {
            id = Integer.parseInt(FileUtils.readFileToString(idFile)) + 1;
        } catch (IOException ignored) {
            //
        }
        String pageName = "Page" + id;

        //serializing IonApp to string
        if (app == null) {
            app = new IonApp();
        }
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

        //writing html file
        String htmlPath = pagesPath + "\\" + pageName + "\\" + pageName + ".html";
        File newHtmlFile = new File(htmlPath);
        FileUtils.writeStringToFile(newHtmlFile, htmlString);

        //writing config file
        String configString = String.format("{\n\t\"page\": \"%s\",\n\t\"html\": \"%s.html\"\n}", pageName, pageName);
        File configFile = new File(pagesPath + "\\" + pageName + "\\" + "conf.json");
        FileUtils.writeStringToFile(configFile, configString);

        //updating id file
        FileUtils.writeStringToFile(idFile, id + "");

        return pagesPath + pageName;
    }
}
