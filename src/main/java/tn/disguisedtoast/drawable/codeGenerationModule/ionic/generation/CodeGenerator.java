package tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation;

import org.apache.commons.io.FileUtils;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.IonApp;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

public class CodeGenerator {

    public static File getHtml(IonApp app) throws JAXBException, IOException, URISyntaxException {
        JAXBContext jc = JAXBContext.newInstance(IonApp.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        marshaller.marshal(app, sw);
        String body = sw.toString();

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
