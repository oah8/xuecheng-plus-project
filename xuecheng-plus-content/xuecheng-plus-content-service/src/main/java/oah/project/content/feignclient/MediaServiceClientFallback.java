package oah.project.content.feignclient;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ClassName MediaServiceClientFallback
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.22 16:41
 * @Version 1.0
 */
public class MediaServiceClientFallback implements MediaServiceClient {


    @Override
    public String upload(MultipartFile filedata, String objectName) throws IOException {
        return null;
    }
}
