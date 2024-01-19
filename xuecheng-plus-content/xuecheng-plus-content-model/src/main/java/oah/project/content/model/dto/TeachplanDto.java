package oah.project.content.model.dto;

import lombok.Data;
import lombok.ToString;
import oah.project.content.model.po.Teachplan;
import oah.project.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @ClassName TeachplanDto
 * @Description 课程计划信息模型类
 * @Author _oah
 * @Date 2023.11.14 17:54
 * @Version 1.0
 */
@Data
@ToString
public class TeachplanDto extends Teachplan {

    // 与媒资关联的信息
    private TeachplanMedia teachplanMedia;

    // 小章节list
    List<TeachplanDto> teachPlanTreeNodes;
}











