package code_generation.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.ws.rs.core.MediaType;
import java.io.File;

public class Upload {

    public static void upload(String url, File file) throws Exception {
        Thread thread = new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(url);

                try {
                    //Set various attributes
                    MultipartEntity multiPartEntity = new MultipartEntity();
                    FileBody fileBody = new FileBody(file, MediaType.APPLICATION_OCTET_STREAM);
                    //Prepare payload
                    multiPartEntity.addPart("image", fileBody);

                    //Set to request body
                    postRequest.setEntity(multiPartEntity);

                    //Send request
                    HttpResponse response = client.execute(postRequest);

                    //Verify response if any
                    if (response != null) {
                        System.out.println(response.getStatusLine().getStatusCode());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        thread.start();
    }
}
