package oah.project.media.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oah.project.base.model.RestResponse;
import oah.project.media.model.po.MediaFiles;
import oah.project.media.service.MediaFileService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MediaOpenController
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.13 17:01
 * @Version 1.0
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
@RequestMapping("/open")
public class MediaOpenController {


    @Autowired
    private MediaFileService mediaFileService;

    @ApiOperation("预览文件")
    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        // 查询媒资文件信息
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);
        if(mediaFiles == null) {
            return RestResponse.validfail("找不到视频");
        }

        // 取出视频播放地址
        String url = mediaFiles.getUrl();
        if(StringUtils.isEmpty(url)) {
            return RestResponse.validfail("该视频正在处理中");
        }

        return RestResponse.success(mediaFiles.getUrl());
    }






}
