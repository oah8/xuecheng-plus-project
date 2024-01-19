package oah.project.content.api;

import oah.project.content.model.dto.CourseCategoryTreeDto;
import oah.project.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName CourseCategoryController
 * @Description 课程分类相关接口
 * @Author _oah
 * @Date 2023.11.11 20:13
 * @Version 1.0
 */
@RestController
public class CourseCategoryController {

    @Autowired
    private CourseCategoryService courseCategoryService;

    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return courseCategoryService.queryTreeNodes("1");
    }


}


















