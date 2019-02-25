package code_generation;

import code_generation.entities.Box;
import code_generation.entities.DetectedObject;
import code_generation.entities.views.ConstraintLayout;
import code_generation.service.CodeGenerator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws JAXBException, FileNotFoundException {
        List<DetectedObject> objects = new ArrayList<>();
        objects.add(new DetectedObject(DetectedObject.FRAME, new Box(0, 0, 10, 2)));
        objects.add(new DetectedObject(DetectedObject.IMAGE, new Box(3.5, 2, 6.5, 3)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2.5, 3, 7.5, 4)));
        objects.add(new DetectedObject(DetectedObject.EditText, new Box(2, 6, 4, 8)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(5, 6.4, 8, 7.4)));
        objects.add(new DetectedObject(DetectedObject.BUTTON, new Box(1, 10, 5, 13)));
        JAXBContext jc = JAXBContext.newInstance(ConstraintLayout.class);
        Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        ConstraintLayout layout = CodeGenerator.parse(objects).getLayout();

        marshaller.marshal(layout, System.out);
        OutputStream os = new FileOutputStream("../AndroidTest/app/src/main/res/layout/layout.xml");
        marshaller.marshal(layout, os);
    }
}
