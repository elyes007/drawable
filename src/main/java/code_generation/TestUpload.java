package code_generation;

import code_generation.service.Upload;

import java.io.File;

public class TestUpload {
    public static void main(String[] args) throws Exception {
        File file = new File("C:/Users/Elyes/Downloads/1.jpg");
        String url = "http://localhost:3000/recipes/upload_file";
        Upload.upload(url, file);
        System.out.println("lol");
    }
}
