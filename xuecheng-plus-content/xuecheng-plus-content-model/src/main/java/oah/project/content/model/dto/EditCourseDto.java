package oah.project.content.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName EditCourseDto
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.14 16:15
 * @Version 1.0
 */
@Data
public class EditCourseDto extends AddCourseDto{

    @ApiModelProperty(value = "课程id", required = true)
    private Long id;

}


