package code_generation.service;

import code_generation.entities.DetectedObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShapeDetectionService {

    private static final String URL = "http://172.16.167.215:5000";

    public static void upload(File file, UploadCallback callback) {
        new Thread(() -> {
            System.out.println(new Date());
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(3 * 1000).build();
            HttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            HttpPost postRequest = new HttpPost(URL);
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
                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    System.out.println(response.getStatusLine().getStatusCode());
                    String jsonString = EntityUtils.toString(response.getEntity());
                    List<DetectedObject> objects = new Gson().fromJson(jsonString, new TypeToken<List<DetectedObject>>() {
                    }.getType());
                    callback.onUploaded(objects);
                } else {
                    System.out.println("Request failed");
                    callback.onUploaded(new ArrayList<>());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                callback.onUploaded(new ArrayList<>());
            }
        }).start();
    }

    public interface UploadCallback {
        void onUploaded(List<DetectedObject> objects);
    }
}
