package oah.project.content.service;

import oah.project.base.model.PageParams;
import oah.project.base.model.PageResult;
import oah.project.content.model.dto.AddCourseDto;
import oah.project.content.model.dto.CourseBaseInfoDto;
import oah.project.content.model.dto.EditCourseDto;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.model.po.CourseBase;

/**
 * @ClassName CourseBaseInfoService
 * @Description 课程信息管理接口
 * @Author _oah
 * @Date 2023.11.10 20:06
 * @Version 1.0
 */
public interface CourseBaseInfoService {


    /**
     * 课程分页查询
     * @param companyId 培训机构id
     * @param pageParams 分页查询参数
     * @param courseParamsDto 查询条件
     * @return 查询结果
     */
    public PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParams pageParams, QueryCourseParamsDto courseParamsDto);


    /**
     * 新增课程
     * @param companyId 机构id
     * @param addCourseDto 课程信息
     * @return 课程详细信息
     */
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto addCourseDto);


    /**
     * 根据课程id查询课程信息
     * @param courseId 课程id
     * @return 课程的详细信息
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId);


    /**
     * 修改课程
     * @param companyId 机构id
     * @param editCourseDto 修改课程信息
     * @return 课程详细信息
     */
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto);



}



















