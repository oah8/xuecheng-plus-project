package oah.project.content.service;

import oah.project.content.model.dto.CoursePreviewDto;
import oah.project.content.model.po.CoursePublish;

import java.io.File;

/**
 * @ClassName CoursePublishService
 * @Description 课程发布相关的接口
 * @Author _oah
 * @Date 2023.12.13 16:01
 * @Version 1.0
 */
public interface CoursePublishService {


    /**
     * @description 获取课程预览信息
     * @param courseId 课程id
     * @return com.xuecheng.content.model.dto.CoursePreviewDto
     * @author Mr.M
     * @date 2022/9/16 15:36
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     *  提交审核
     * @param companyId 机构id
     * @param courseId  课程id
     */
    public void commitAudit(Long companyId, Long courseId);

    /**
     * 课程发布接口
     * @param companyId 机构id
     * @param courseId 课程id
     */
    public void publish(Long companyId, Long courseId);


    /**
     * @description 课程静态化
     * @param courseId  课程id
     * @return File 静态化文件
     * @author Mr.M
     * @date 2022/9/23 16:59
     */
    public File generateCourseHtml(Long courseId);
    /**
     * @description 上传课程静态化页面
     * @param file  静态化文件
     * @return void
     * @author Mr.M
     * @date 2022/9/23 16:59
     */
    public void  uploadCourseHtml(Long courseId,File file);


    /**
     * 根据课程id查询课程发布信息
     * @param courseId
     * @return
     */
    public CoursePublish getCoursePublish(Long courseId);


    /**
     * 从缓存中查询
     * @param courseId
     * @return
     */
    public CoursePublish getCoursePublishCache(Long courseId);


}
