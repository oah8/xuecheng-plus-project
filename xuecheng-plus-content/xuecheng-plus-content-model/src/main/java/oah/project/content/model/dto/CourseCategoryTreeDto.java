package oah.project.content.model.dto;

import lombok.Data;
import oah.project.content.model.po.CourseCategory;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CourseCategoryTreeDto
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.11 20:09
 * @Version 1.0
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory implements Serializable {

    // 子节点
    List<CourseCategoryTreeDto> childrenTreeNodes;
}
