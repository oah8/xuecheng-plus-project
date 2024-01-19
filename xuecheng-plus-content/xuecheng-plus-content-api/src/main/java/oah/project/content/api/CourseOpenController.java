package oah.project.content.api;

import io.swagger.annotations.Api;
import oah.project.content.model.dto.CoursePreviewDto;
import oah.project.content.service.CourseBaseInfoService;
import oah.project.content.service.CoursePublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CourseOpenController
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.13 16:56
 * @Version 1.0
 */
@Api(value = "课程公开查询接口",tags = "课程公开查询接口")
@RequestMapping("/open")
@RestController
public class CourseOpenController {


    @Autowired
    private CourseBaseInfoService courseBaseInfoService;


    @Autowired
    private CoursePublishService coursePublishService;

    // 根据课程id查询课程信息
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getPreviewInfo(@PathVariable("courseId") Long courseId) {
        // 获取课程预览信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        return coursePreviewInfo;
    }





}
