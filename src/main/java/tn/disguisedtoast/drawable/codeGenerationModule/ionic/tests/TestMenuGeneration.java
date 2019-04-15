package tn.disguisedtoast.drawable.codeGenerationModule.ionic.tests;

import tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation.CodeGenerator;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.*;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TestMenuGeneration {
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

        IonTab tab1 = new IonTab();
        tab1.setTab("tab1");
        tab1.setImages(new ArrayList<>(Collections.singletonList(ionImg)));
        tab1.setButtons(new ArrayList<>(Collections.singletonList(button)));
        tab1.setLabels(new ArrayList<>(Collections.singletonList(titleLabel)));
        tab1.setItems(new ArrayList<>(Collections.singletonList(item)));

        IonTab tab2 = new IonTab();
        tab2.setTab("tab2");
        tab2.setButtons(new ArrayList<>(Collections.singletonList(button)));
        tab2.setLabels(new ArrayList<>(Collections.singletonList(titleLabel)));
        tab2.setItems(new ArrayList<>(Collections.singletonList(item)));

        IonTab tab3 = new IonTab();
        tab3.setTab("tab3");
        tab3.setLabels(new ArrayList<>(Collections.singletonList(titleLabel)));
        tab3.setItems(new ArrayList<>(Collections.singletonList(item)));

        IonTabButton b1 = new IonTabButton();
        b1.setTab("tab1");
        b1.setLabel("Maps");
        b1.setIcon(new IonIcon("map"));

        IonTabButton b2 = new IonTabButton();
        b2.setTab("tab2");
        b2.setLabel("Schedule");
        b2.setIcon(new IonIcon("calendar"));

        IonTabButton b3 = new IonTabButton();
        b3.setTab("tab3");
        b3.setLabel("Contacts");
        b3.setIcon(new IonIcon("contacts"));

        IonTabBar ionTabBar = new IonTabBar();
        ionTabBar.getTabButtons().addAll(Arrays.asList(b1, b2, b3));

        IonTabs ionTabs = new IonTabs();
        ionTabs.getTabs().addAll(Arrays.asList(tab1, tab2, tab3));
        ionTabs.setTabBar(ionTabBar);

        Div div = new Div();
        div.setTabs(ionTabs);
        div.setClasse("ion-page");
        div.setMain("");
        div.getHeader().getToolbar().getIonButtons().setMenuButton(new IonMenuButton());
        div.getHeader().getToolbar().getIonButtons().setSlot("start");

        IonItem item1 = new IonItem(new IonLabel("Contacts", null, null), new IonIcon("contacts"));
        IonItem item2 = new IonItem(new IonLabel("Schedule", null, null), new IonIcon("calendar"));
        item1.getIcon().setSlot("start");
        item2.getIcon().setSlot("start");
        item1.setPosition(null);
        item2.setPosition(null);

        IonList ionList = new IonList();
        ionList.getItems().addAll(Arrays.asList(item1, item2));

        IonContent ionContent = new IonContent();
        ionContent.setIonLists(new ArrayList<>(Collections.singletonList(ionList)));

        IonMenu ionMenu = new IonMenu();
        ionMenu.setContent(ionContent);
        ionMenu.getHeader().getToolbar().setTitle("Menu");
        ionMenu.getHeader().getToolbar().setIonButtons(null);

        IonApp ionApp = new IonApp();
        ionApp.setHeader(null);
        ionApp.setIonMenu(ionMenu);
        ionApp.setDiv(div);

        CodeGenerator.generateTempHtml(ionApp);
    }
}
