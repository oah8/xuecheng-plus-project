package oah.project.media;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.io.IOUtil;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MinioTest
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.23 20:58
 * @Version 1.0
 */
public class MinioTest {

    MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://8.222.163.95:9800")
                    .credentials("admin", "oah7629...")
                    .build();


    // 上传文件
    @Test
    public void test_upload() throws Exception {


        // 通过扩展名得到媒体资源类型mimeType
        // 根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".jpg");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // 通用mimeType，字节流
        if(mimeType != null) {
            mimeType = extensionMatch.getMimeType();
        }


        // 上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket") // 桶
                .filename("E:\\cool_background.jpg") // 指定本地文件路径
                .object("test/01/background.jpg") // 对象名 放在子目录下
                .contentType(mimeType) // 设置媒体文件类型
                .build();
        // 上传文件
        minioClient.uploadObject(uploadObjectArgs);

    }


    // 删除文件
    @Test
    public void test_remove() throws Exception {

        // 删除文件的参数信息
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket("testbucket")
                .object("background.jpg")
                .build();
        // 删除文件
        minioClient.removeObject(removeObjectArgs);

    }


    // 查询文件 从minio中下载文件
    @Test
    public void test_getFile() throws Exception {

        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket("testbucket")
                .object("test/01/background.jpg")
                .build();

        // 查询远程服务获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);

        // 指定输出流
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\test.jpg"));
        IOUtils.copy(inputStream, outputStream);

        // 校验文件的完整性对文件的内容进行md5
        String source_md5 = DigestUtils.md5DigestAsHex(inputStream);// minio中文件的md5
        String local_md5 = DigestUtils.md5DigestAsHex(new FileInputStream(new File("E:\\cool_background.jpg")));

        if(source_md5.equals(local_md5)) {
            System.out.println("下载成功");
        }

    }



    // 将分块文件上传到minio
    @Test
    public void uploadChunk() throws Exception{

        for(int i = 0; i < 8; i++) {
            // 上传文件的参数信息
            UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                    .bucket("testbucket") // 桶
                    .filename("D:\\chunk-test\\" + i) // 指定本地文件路径
                    .object("chunk/" + i) // 对象名 放在子目录下
                    .build();
            // 上传文件
            minioClient.uploadObject(uploadObjectArgs);
            System.out.println("上传分块" + i + "成功");
        }





    }

    // 调用minio接口合并分块
    @Test
    public void testMerge() throws Exception {

//        List<ComposeSource> sources = null;
//
//        for(int i = 0; i < 8; i++) {
//            // 指定分块文件的信息
//            ComposeSource composeSource = ComposeSource.builder().bucket("testbucket").object("chunk/" + i).build();
//            sources.add(composeSource);
//        }


        List<ComposeSource> composeSourceList = Stream.iterate(0, i -> ++i).limit(8).map(i -> ComposeSource.builder().bucket("testbucket").object("chunk/" + i).build()).collect(Collectors.toList());


        // 指定合并后的objectName等信息
        ComposeObjectArgs composeObjectArgs = ComposeObjectArgs.builder()
                .bucket("testbucket")
                .object("merge01.mp4")
                .sources(composeSourceList) // 指定源文件
                .build();
        // 合并文件
        // 报错 size 1048576 must be greater than 5242880， minio默认的分块文件大小为5M
        minioClient.composeObject(composeObjectArgs);

    }


    // 批量清理分块文件




}











































