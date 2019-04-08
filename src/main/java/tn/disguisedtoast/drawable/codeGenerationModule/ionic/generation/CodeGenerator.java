package tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation;

import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.*;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.MissingFramesException;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoDetectedObjects;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CodeGenerator {

    public static IonApp parse(List<DetectedObject> detectedObjects) throws NoDetectedObjects, MissingFramesException {

        List<List<DetectedObject>> tabs = partition(detectedObjects);

        List<CompletableFuture<List<IonView>>> promises = new ArrayList<>();
        for (List<DetectedObject> tab : tabs) {
            promises.add(CompletableFuture.supplyAsync(() -> {
                Date start = new Date();
                DetectedObject topFrame = tab.get(0);
                DetectedObject bottomFrame = tab.get(1);
                if (bottomFrame.getClasse() != DetectedObject.FRAME) {
                    //tab.remove(0);
                    //return parseWithSingleFrame(topFrame, objects);
                    try {
                        throw new MissingFramesException("Only one frame detected");
                    } catch (MissingFramesException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    tab.remove(0); //remove topFrame
                    tab.remove(0); //remove bottomFrame after index shift
                    tab.sort((o1, o2) -> o1.getBox().getyMin() < o2.getBox().getyMin() ? -1 : 1); //sort by height to get the naming order right
                    List<IonView> views = parseWithDoubleFrames(topFrame, bottomFrame, tab);
                    Date finish = new Date();
                    System.out.println("done in " + (finish.getTime() - start.getTime()) + " ms");
                    return views;
                }
            }));
        }
        Date start = new Date();
        CompletableFuture.allOf(promises.stream().toArray(CompletableFuture[]::new));
        Date finish = new Date();
        System.out.println("All done in " + (finish.getTime() - start.getTime()) + " ms");

        List<List<IonView>> viewsList = new ArrayList<>();
        for (CompletableFuture<List<IonView>> promise : promises) {
            try {
                viewsList.add(promise.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
            } //in case one of the promises returned null
        }

        if (viewsList.size() > 1) {
            return buildTabLayout(viewsList);
        }
        return buildContentLayout(viewsList.get(0));
    }

    private static List<List<DetectedObject>> partition(List<DetectedObject> objectList) throws NoDetectedObjects, MissingFramesException {
        if (objectList == null || objectList.isEmpty()) {
            throw new NoDetectedObjects("No detected objects");
        }

        //separate frames from other objects
        List<DetectedObject> frames = new ArrayList<>();
        List<DetectedObject> objects = new ArrayList<>();
        for (DetectedObject object : objectList) {
            if (object.getClasse() == DetectedObject.FRAME) {
                frames.add(object);
            } else {
                objects.add(object);
            }
        }

        //test for exceptions
        if (frames.isEmpty()) {
            throw new MissingFramesException("No frames detected");
        }
        if (objects.isEmpty()) {
            throw new NoDetectedObjects("No detected objects inside frames");
        }

        List<List<DetectedObject>> tabs = new ArrayList<>();

        //pair up frames
        while (!frames.isEmpty()) {
            List<DetectedObject> tab = new ArrayList<>();
            DetectedObject frame1 = frames.remove(0);
            tab.add(frame1);
            for (int i = 0; i < frames.size(); i++) {
                DetectedObject frame2 = frames.get(i);
                double centerX = (frame2.getBox().getxMin() + frame2.getBox().getxMax()) / 2;
                if (centerX >= frame1.getBox().getxMin() && centerX <= frame1.getBox().getxMax()) {
                    tab.add(frame2);
                    frames.remove(i);
                }
            }
            tab.sort((o1, o2) -> o1.getBox().getyMin() < o2.getBox().getyMin() ? -1 : 1);
            tabs.add(tab);
        }

        //add objects to each tab
        for (List<DetectedObject> tab : tabs) {
            DetectedObject frame = tab.get(0);
            for (int i = 0; i < objects.size(); i++) {
                DetectedObject object = objects.get(i);
                double centerX = (object.getBox().getxMin() + object.getBox().getxMax()) / 2;
                if (centerX >= frame.getBox().getxMin() && centerX <= frame.getBox().getxMax()) {
                    tab.add(object);
                    objects.remove(i);
                    i--;
                }
            }
        }

        //remove empty tabs
        for (int i = 0; i < tabs.size(); i++) {
            List<DetectedObject> tab = tabs.get(i);
            if (tab.size() == 1 || tab.size() == 2 && tab.get(1).getClasse() == DetectedObject.FRAME) {
                tabs.remove(i);
                i--;
            }
        }

        //sort tabs by x
        tabs.sort((o1, o2) -> o1.get(0).getBox().getxMin() < o2.get(0).getBox().getxMin() ? 1 : -1);

        return tabs;
    }

    private static List<IonView> parseWithDoubleFrames(DetectedObject topFrame, DetectedObject bottomFrame, List<DetectedObject> objects) {
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

        return views;
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

    private static IonApp buildContentLayout(List<IonView> views) {
        return new IonApp(getContent(views));
    }

    private static IonApp buildTabLayout(List<List<IonView>> tabs) {
        IonTabs ionTabs = new IonTabs();

        int i = 1;
        for (List<IonView> tab : tabs) {
            IonTab ionTab = new IonTab();
            ionTab.setTab("tab" + i);

            IonTabButton tabButton = new IonTabButton();
            tabButton.setTab(ionTab.getTab());
            tabButton.setLabel("Tab " + i);
            tabButton.setIcon(new IonIcon(IonIcon.names.get(i - 1)));

            IonContent content = getContent(tab);

            ionTab.setButtons(content.getButtons());
            ionTab.setImages(content.getImages());
            ionTab.setItems(content.getItems());
            ionTab.setLabels(content.getLabels());

            ionTabs.getTabs().add(ionTab);
            ionTabs.getTabBar().getTabButtons().add(tabButton);

            i++;
        }

        return new IonApp(ionTabs);
    }

    private static IonContent getContent(List<IonView> views) {
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
        return ionContent;
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
