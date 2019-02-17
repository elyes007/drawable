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
                CodeGenerator.generateLayoutFile(layout);
            } catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        System.out.println("lol");
    }
}
