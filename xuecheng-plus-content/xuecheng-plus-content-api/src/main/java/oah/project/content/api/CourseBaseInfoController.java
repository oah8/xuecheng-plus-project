package oah.project.content.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oah.project.base.exception.ValidationGroups;
import oah.project.base.model.PageParams;
import oah.project.base.model.PageResult;
import oah.project.content.model.dto.AddCourseDto;
import oah.project.content.model.dto.CourseBaseInfoDto;
import oah.project.content.model.dto.EditCourseDto;
import oah.project.content.model.po.CourseBase;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.service.CourseBaseInfoService;
import oah.project.content.utils.SecurityUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CourseBaseInfoController
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.10 17:20
 * @Version 1.0
 */
@Api(value = "课程信息管理接口", tags = "课程信息管理接口")
@RestController
public class CourseBaseInfoController {


    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @ApiOperation("课程分页查询接口")
    @PreAuthorize("hasAnyAuthority('xc_teachmanager_course_list')") // 指定权限标识符，拥有此权限才可以访问此方法
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(PageParams pageParams, @RequestBody(required = false) QueryCourseParamsDto courseParamsDto) {

        // 当前登录用户
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        // 用户所属机构id
        Long companyId = null;
        if(StringUtils.isNotEmpty(user.getCompanyId())) {
            companyId = Long.parseLong(user.getCompanyId());
        }

        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(companyId, pageParams, courseParamsDto);

        return courseBasePageResult;
    }


    @ApiOperation("新增课程")
    @PostMapping("/course")
    public CourseBaseInfoDto createCourseBase(@RequestBody @Validated(ValidationGroups.Insert.class) AddCourseDto addCourseDto) {

        // 单点登录可以直接获取到所属机构的id
        Long companyId = 1232141425L;

//        int i = 1 / 0;

        CourseBaseInfoDto courseBase = courseBaseInfoService.createCourseBase(companyId, addCourseDto);

        return courseBase;
    }

    @ApiOperation("根据课程id查询接口")
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseBaseById(@PathVariable Long courseId) {
        // 获取当前用户的身份
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        System.out.println(principal);
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        System.out.println(user.getUsername());
        CourseBaseInfoDto courseBaseInfo = courseBaseInfoService.getCourseBaseInfo(courseId);
        return courseBaseInfo;
    }


    @ApiOperation("修改课程")
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(@RequestBody @Validated(ValidationGroups.Update.class) EditCourseDto editCourseDto) {

        // 单点登录可以直接获取到所属机构的id
        Long companyId = 1232141425L;

        CourseBaseInfoDto courseBaseInfoDto = courseBaseInfoService.updateCourseBase(companyId, editCourseDto);
        return courseBaseInfoDto;
    }



}


























