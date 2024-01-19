package oah.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @ClassName ContentApplicationTest
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.10 19:16
 * @Version 1.0
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"oah.project.content.feignclient"})
public class ContentApplicationTest {

    public static void main(String[] args) {
        SpringApplication.run(ContentApplicationTest.class, args);
    }

}
