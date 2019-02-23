package code_generation;

import code_generation.entities.views.ConstraintLayout;
import code_generation.service.CodeGenerator;
import code_generation.service.ShapeDetectionService;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

public class TestUpload {

    public static void main(String[] args) throws Exception {
        File file = new File("C:/Users/Elyes/Downloads/photodf.jpg");
        ShapeDetectionService.upload(file, objects -> {
            ConstraintLayout layout = CodeGenerator.parse(objects).getLayout();
            try {
                CodeGenerator.generateLayoutFile(layout);
            } catch (JAXBException | FileNotFoundException e) {
                e.printStackTrace();
            }
        });
        System.out.println("lol");
    }
}
