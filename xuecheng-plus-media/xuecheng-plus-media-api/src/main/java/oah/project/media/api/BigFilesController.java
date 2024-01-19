package oah.project.media.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oah.project.base.model.RestResponse;
import oah.project.media.model.dto.UploadFileParamsDto;
import oah.project.media.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @ClassName BigFilesController
 * @Description 上传视频
 * @Author _oah
 * @Date 2023.11.25 19:38
 * @Version 1.0
 */
@Api(value = "大文件上传接口", tags = "大文件上传接口")
@RestController
public class BigFilesController {

    @Autowired
    private MediaFileService mediaFileService;


    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkfile(
            @RequestParam("fileMd5") String fileMd5
    ) throws Exception {
        RestResponse<Boolean> booleanRestResponse = mediaFileService.checkFile(fileMd5);
        return booleanRestResponse;
    }


    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkchunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) throws Exception {
        RestResponse<Boolean> booleanRestResponse = mediaFileService.checkChunk(fileMd5, chunk);
        return booleanRestResponse;
    }

    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws Exception {

        // 创建一个临时文件
        // 这个临时文件夹是在你的本地磁盘中的，是作为一个中转站来用，提高stream流的性能，这是设计参数时候的思想
        File tempFile = File.createTempFile("minio", ".temp");
        file.transferTo(tempFile);
        // 文件路径
        String localFilePath = tempFile.getAbsolutePath();

        RestResponse restResponse = mediaFileService.uploadChunk(fileMd5, chunk, localFilePath);
        return restResponse;
    }

    @ApiOperation(value = "合并文件")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {

        Long companyId = 1232141425L;
        // 文件信息对象
        UploadFileParamsDto uploadFileParamsDto = new UploadFileParamsDto();
        uploadFileParamsDto.setFilename(fileName);
        uploadFileParamsDto.setFileType("视频文件");
        uploadFileParamsDto.setFileType("001002");
        RestResponse restResponse = mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, uploadFileParamsDto);

        return restResponse;

    }


}
