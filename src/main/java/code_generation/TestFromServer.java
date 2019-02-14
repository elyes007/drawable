package code_generation;

import code_generation.entities.DetectedObject;
import code_generation.entities.views.*;
import code_generation.service.CodeGenerator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TestFromServer {
    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://172.16.167.215:5000/");
        Response response = target.request().get();
        String jsonString = response.readEntity(String.class);
        System.out.println(jsonString);

        List<DetectedObject> objects = new Gson().fromJson(jsonString, new TypeToken<List<DetectedObject>>() {
        }.getType());

        JAXBContext jc = JAXBContext.newInstance(ConstraintLayout.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ConstraintLayout layout = new ConstraintLayout();
        layout.setWidth("match_parent");
        layout.setHeight("match_parent");

        for (View view : CodeGenerator.parse(objects)) {
            if (view instanceof Button) {
                if (layout.getButtons() == null) {
                    layout.setButtons(new ArrayList<>());
                }
                layout.getButtons().add((Button) view);
            }
            if (view instanceof ImageView) {
                if (layout.getImageViews() == null) {
                    layout.setImageViews(new ArrayList<>());
                }
                layout.getImageViews().add((ImageView) view);
            }
            if (view instanceof EditText) {
                if (layout.getEditTexts() == null) {
                    layout.setEditTexts(new ArrayList<>());
                }
                layout.getEditTexts().add((EditText) view);
            }
        }
        marshaller.marshal(layout, System.out);
        OutputStream os = new FileOutputStream("../AndroidTest/app/src/main/res/layout/layout.xml");
        marshaller.marshal(layout, os);
    }
}
