package oah.project.content.model.dto;

import lombok.Data;
import oah.project.content.model.po.Teachplan;

import java.util.List;

/**
 * @ClassName CoursePreviewDto
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.13 15:57
 * @Version 1.0
 */
@Data
public class CoursePreviewDto {

    // 课程基本信息，营销信息
    private CourseBaseInfoDto courseBase;


    // 课程计划信息
    private List<TeachplanDto> teachplans;

    // 课程师资信息




}
