package oah.project.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import oah.project.base.model.PageParams;
import oah.project.base.model.PageResult;
import oah.project.content.mapper.CourseBaseMapper;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.model.po.CourseBase;
import oah.project.content.service.CourseBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @ClassName CourseBaseMapperTest
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.10 19:22
 * @Version 1.0
 */
@SpringBootTest
public class CourseBaseInfoServiceTest {

    @Autowired
    private CourseBaseInfoService courseBaseInfoService;

    @Test
    public void testCourseBaseMapper() {

        // 查询条件
        QueryCourseParamsDto courseParamsDto = new QueryCourseParamsDto();
        courseParamsDto.setCourseName("java"); // 课程名称查询条件
        courseParamsDto.setAuditStatus("202004"); // 202004表示课程审核通过

        // 分页参数对象
        PageParams pageParams = new PageParams();
        pageParams.setPageNo(2L);
        pageParams.setPageSize(2L);

        PageResult<CourseBase> courseBasePageResult = courseBaseInfoService.queryCourseBaseList(null, pageParams, courseParamsDto);
        System.out.println(courseBasePageResult);


    }


}





























