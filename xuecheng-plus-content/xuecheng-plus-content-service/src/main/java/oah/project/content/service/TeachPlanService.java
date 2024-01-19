package oah.project.content.service;

import oah.project.content.model.dto.BindTeachPlanMediaDto;
import oah.project.content.model.dto.SaveTeachPlanDto;
import oah.project.content.model.dto.TeachplanDto;
import oah.project.content.model.po.TeachplanMedia;

import java.util.List;

/**
 * @ClassName TeachPlanService
 * @Description 课程计划管理相关接口
 * @Author _oah
 * @Date 2023.11.14 18:45
 * @Version 1.0
 */
public interface TeachPlanService {
    /**
     * 根据课程id查询课程计划
     * @param courseId 课程id
     * @return 课程计划
     */
    public List<TeachplanDto> findTeachPlanTree(Long courseId);

    /**
     * 新增/修改/保存课程计划
     * @param saveTeachPlanDto
     */
    public void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto);

    /**
     * 教学计划绑定媒资
     * @param bindTeachPlanMediaDto
     * @return
     */
    public void associationMedia(BindTeachPlanMediaDto bindTeachPlanMediaDto);
}
