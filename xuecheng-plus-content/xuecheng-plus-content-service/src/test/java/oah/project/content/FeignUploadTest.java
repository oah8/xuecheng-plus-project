package oah.project.content;

import oah.project.content.config.MultipartSupportConfig;
import oah.project.content.feignclient.MediaServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName CourseBaseMapperTest
 * @Description 测试freemarker页面静态化方法
 * @Author _oah
 * @Date 2023.11.10 19:22
 * @Version 1.0
 */
@SpringBootTest
public class FeignUploadTest {

    @Autowired
    private MediaServiceClient mediaServiceClient;

    @Test
    public void test() throws IOException {

        // 将file转成MultipartFile
        File file = new File("E:\\develop\\upload\\120.html");
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        // 远程调用
        String upload = mediaServiceClient.upload(multipartFile, "course/120.html");
        if(upload == null) {
            System.out.println("走了降级逻辑");
        }
    }

}





























