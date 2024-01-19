package oah.project.media.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName UploadFileParamsDto
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.25 14:53
 * @Version 1.0
 */
@Data
@ToString
public class UploadFileParamsDto {
    /**
     * 文件名称
     */
    private String filename;


    /**
     * 文件类型（文档，音频，视频）
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人
     */
    private String username;

    /**
     * 备注
     */
    private String remark;


}
