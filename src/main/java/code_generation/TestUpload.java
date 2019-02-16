package code_generation;

import code_generation.entities.views.ConstraintLayout;
import code_generation.service.CodeGenerator;
import code_generation.service.ShapeDetectionService;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class TestUpload {

    public static void main(String[] args) throws Exception {
        File file = new File("C:/Users/Elyes/Downloads/1.jpg");
        ShapeDetectionService.upload(file, objects -> {
            ConstraintLayout layout = CodeGenerator.parse(objects);
            try {
                JAXBContext jc = JAXBContext.newInstance(ConstraintLayout.class);
                Marshaller marshaller = jc.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                marshaller.marshal(layout, System.out);
                OutputStream os = new FileOutputStream("../AndroidTest/app/src/main/res/layout/layout.xml");
                marshaller.marshal(layout, os);
            } catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        System.out.println("lol");
    }
}
