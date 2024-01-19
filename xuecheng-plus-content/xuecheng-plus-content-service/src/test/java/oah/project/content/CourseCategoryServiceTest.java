package oah.project.content;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import oah.project.base.model.PageParams;
import oah.project.base.model.PageResult;
import oah.project.content.mapper.CourseBaseMapper;
import oah.project.content.model.dto.CourseCategoryTreeDto;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.model.po.CourseBase;
import oah.project.content.service.CourseCategoryService;
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
public class CourseCategoryServiceTest {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @Test
    public void testCourseBaseMapper() {
        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryService.queryTreeNodes("1");
        System.out.println(courseCategoryTreeDtos);


    }


}





























