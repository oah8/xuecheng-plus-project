package oah.project.content.service.impl;

import lombok.extern.slf4j.Slf4j;
import oah.project.content.mapper.CourseCategoryMapper;
import oah.project.content.model.dto.CourseCategoryTreeDto;
import oah.project.content.service.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName CourseCategoryServiceImpl
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.11 21:04
 * @Version 1.0
 */
@Slf4j
@Service
public class CourseCategoryServiceImpl implements CourseCategoryService {

    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    @Override
    public List<CourseCategoryTreeDto> queryTreeNodes(String id) {
        // 调用mapper递归查询出分类信息

        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes(id);

        // 找到每个节点的子节点，最终封装成List<CourseCategoryTreeDto>

        // 先将List转成map，key就是结点的id，value就是CourseCategoryTreeDto对象，目的就是为了方便从map获取结点，filter(item->!id.equals(item.getId()))把根节点排除
        Map<String, CourseCategoryTreeDto> mapTemp = courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).collect(Collectors.toMap(key -> key.getId(), value -> value, (key1, key2) -> key2));
        // 定义一个最终返回的list
        List<CourseCategoryTreeDto> courseCategoryList = new ArrayList<>();

        // 从头遍历List<CourseCategoryTreeDto>，一边遍历一边找子节点放在父节点的childrenTreeNodes
        courseCategoryTreeDtos.stream().filter(item->!id.equals(item.getId())).forEach(item-> {
            if(item.getParentid().equals(id)) {
                courseCategoryList.add(item);
            }
            // 找到节点的父节点
            CourseCategoryTreeDto courseCategoryTreeDto = mapTemp.get(item.getParentid());
            if(courseCategoryTreeDto != null) {
                if(courseCategoryTreeDto.getChildrenTreeNodes() == null) {
                    // 如果该父节点的ChildrenTreeNodes属性为空，要new一个集合，因为要向该集合中放它的子节点
                    courseCategoryTreeDto.setChildrenTreeNodes(new ArrayList<CourseCategoryTreeDto>());
                }
                // 找到每个节点的子节点放在父节点的childrenTreeNodes属性中
                courseCategoryTreeDto.getChildrenTreeNodes().add(item);
            }

        });
        return courseCategoryList;
    }

    /**
     * 递归查询
     * @param courseCategoryTreeDtos
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> getChildren(List<CourseCategoryTreeDto> courseCategoryTreeDtos, String id) {
        return courseCategoryTreeDtos.stream()
                .filter(p-> Objects.equals(p.getParentid(), id))
                .peek(f->f.setChildrenTreeNodes(getChildren(courseCategoryTreeDtos, f.getId())))
                .collect(Collectors.toList());
    }
}









