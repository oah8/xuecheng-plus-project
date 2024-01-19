package oah.project.content.api;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import oah.project.content.model.dto.CourseBaseInfoDto;
import oah.project.content.model.dto.CoursePreviewDto;
import oah.project.content.model.dto.TeachplanDto;
import oah.project.content.model.po.CoursePublish;
import oah.project.content.service.CoursePublishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @ClassName CoursePublishController
 * @Description 课程发布相关的接口
 * @Author _oah
 * @Date 2023.12.13 15:16
 * @Version 1.0
 */
@Controller
public class CoursePublishController {


    @Autowired
    private CoursePublishService coursePublishService;



    @ApiOperation("获取课程发布信息")
    @ResponseBody
    @GetMapping("/course/whole/{courseId}")
    public CoursePreviewDto getCoursepublish(@PathVariable("courseId") Long courseId) {

        // 封装数据
        CoursePreviewDto coursePreviewDto = new CoursePreviewDto();

        // 查询课程发布表
//        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        // 先从缓存中查询，缓存中有直接返回，没有在查询数据库
        CoursePublish coursePublish = coursePublishService.getCoursePublishCache(courseId);
        if(coursePublish == null) {
            return coursePreviewDto;
        }

        // 开始向coursePreviewDto填充数据
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(coursePublish, courseBaseInfoDto);
        // 课程计划信息
        String teachplanJson = coursePublish.getTeachplan();
        // 转成List<TeachplanDto>
        List<TeachplanDto> teachplanDtos = JSON.parseArray(teachplanJson, TeachplanDto.class);
        coursePreviewDto.setCourseBase(courseBaseInfoDto);
        coursePreviewDto.setTeachplans(teachplanDtos);
        return coursePreviewDto;
    }

    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {

        ModelAndView modelAndView = new ModelAndView();
        // 查询课程的信息作为模型数据
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);

        // 指定模型
        modelAndView.addObject("model", coursePreviewInfo);
        // 指定模板
        modelAndView.setViewName("course_template"); // 根据视图名称加上.ftl找到模板
        return modelAndView;

    }

    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId, courseId);
    }


    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping("/coursepublish/{courseId}")
    public void coursepublish(@PathVariable("courseId") Long courseId) {
        Long companyId = 1232141425L;
        coursePublishService.publish(companyId, courseId);
    }

    @ApiOperation("查询课程发布信息")
    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        CoursePublish coursePublish = coursePublishService.getCoursePublish(courseId);
        return coursePublish;
    }


}








