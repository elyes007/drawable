package code_generation;

import code_generation.entities.DetectedObject;
import code_generation.entities.views.ConstraintLayout;
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

        ConstraintLayout layout = CodeGenerator.parse(objects).getLayout();

        JAXBContext jc = JAXBContext.newInstance(ConstraintLayout.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        marshaller.marshal(layout, System.out);
        OutputStream os = new FileOutputStream("../AndroidTest/app/src/main/res/layout/layout.xml");
        marshaller.marshal(layout, os);
    }
}
