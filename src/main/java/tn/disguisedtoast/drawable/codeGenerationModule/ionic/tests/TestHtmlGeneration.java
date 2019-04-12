package tn.disguisedtoast.drawable.codeGenerationModule.ionic.tests;

import tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation.CodeGenerator;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class TestHtmlGeneration {

    public static void main(String[] args) throws JAXBException, IOException, URISyntaxException {
        IonImg ionImg = new IonImg();
        ionImg.setSrc("https://i.imgur.com/yjWwi3F.jpg");
        ionImg.setHeight("20%");
        ionImg.setWidth("30%");
        ionImg.setTop("5%");
        ionImg.setLeft("35%");

        IonLabel titleLabel = new IonLabel("Welcome", null);
        titleLabel.setTop("30%");
        titleLabel.setLeft("45%");

        IonItem item = new IonItem(new IonLabel(), new IonInput());
        item.setInput(new IonInput());
        item.setLabel(new IonLabel("Your name here"));
        item.setTop("50%");
        item.setLeft("75%");
        item.setWidth("30%");

        IonButton button = new IonButton("Confirm");
        item.setTop("65%");
        item.setLeft("30%");

        IonContent ionContent = new IonContent();
        ionContent.setImages(new ArrayList<>(Collections.singletonList(ionImg)));
        ionContent.setButtons(new ArrayList<>(Collections.singletonList(button)));
        ionContent.setLabels(new ArrayList<>(Collections.singletonList(titleLabel)));
        ionContent.setItems(new ArrayList<>(Collections.singletonList(item)));

        IonHeader ionHeader = new IonHeader(new IonToolbar("Page 1"));

        IonApp app = new IonApp();
        app.setContent(ionContent);
        app.setHeader(ionHeader);

        CodeGenerator.generatePageFolder(app);
    }
}
