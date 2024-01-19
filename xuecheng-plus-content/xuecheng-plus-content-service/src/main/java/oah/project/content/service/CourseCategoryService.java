package oah.project.content.service;

import oah.project.content.model.dto.CourseCategoryTreeDto;

import java.util.List;

/**
 * @ClassName CourseCategoryService
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.11 21:02
 * @Version 1.0
 */
public interface CourseCategoryService {

    /**
     * 课程分类树形结构查询
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNodes(String id);


    /**
     * 递归查询
     * @param courseCategoryTreeDtos
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> getChildren(List<CourseCategoryTreeDto> courseCategoryTreeDtos, String id);
}
