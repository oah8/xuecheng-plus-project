package oah.project.learning.service;

import oah.project.base.model.PageResult;
import oah.project.learning.model.dto.MyCourseTableParams;
import oah.project.learning.model.dto.XcChooseCourseDto;
import oah.project.learning.model.dto.XcCourseTablesDto;
import oah.project.learning.model.po.XcCourseTables;

/**
 * @ClassName MyCourseTablesService
 * @Description 选课相关的接口
 * @Author _oah
 * @Date 2023.12.31 21:00
 * @Version 1.0
 */
public interface MyCourseTablesService {


    /**
     * 添加选课
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId);


    /**
     * 判断学习资格
     * @param userId
     * @param courseId
     * @return
     */
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId);


    /**
     * 保存选课成功状态
     * @param chooseCourseId
     * @return
     */
    public boolean saveChooseCourseSuccess(String chooseCourseId);


    /**
     * 我的课程表
     * @param params
     * @return
     */
    public PageResult<XcCourseTables> myCourseTables(MyCourseTableParams params);

}
