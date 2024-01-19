package oah.project.content.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName QueryCourseParamsDto
 * @Description 课程查询条件模型类
 * @Author _oah
 * @Date 2023.11.10 17:13
 * @Version 1.0
 */
@Data
@ToString
public class QueryCourseParamsDto {

    //审核状态
    private String auditStatus;
    //课程名称
    private String courseName;
    //发布状态
    private String publishStatus;

}

