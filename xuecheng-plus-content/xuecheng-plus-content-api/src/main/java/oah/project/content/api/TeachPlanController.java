package oah.project.content.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import oah.project.content.model.dto.BindTeachPlanMediaDto;
import oah.project.content.model.dto.SaveTeachPlanDto;
import oah.project.content.model.dto.TeachplanDto;
import oah.project.content.service.TeachPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName TeachPlanController
 * @Description 课程计划管理相关的接口
 * @Author _oah
 * @Date 2023.11.14 17:58
 * @Version 1.0
 */
@Api(value = "课程计划编辑接口", tags = "课程计划编辑接口")
@RestController
public class TeachPlanController {

    @Autowired
    private TeachPlanService teachPlanService;

    // 查询课程计划 GET /teachplan/22/tree-nodes
    @ApiOperation("查询课程计划树形结构")
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        List<TeachplanDto> teachPlanTree = teachPlanService.findTeachPlanTree(courseId);
        return teachPlanTree;
    }

    @ApiOperation("课程计划创建或修改")
    @PostMapping("/teachplan")
    public void saveTeachplan( @RequestBody SaveTeachPlanDto teachplan){

        teachPlanService.saveTeachPlan(teachplan);

    }


    @ApiOperation(value = "课程计划和媒资信息绑定")
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody BindTeachPlanMediaDto bindTeachPlanMediaDto){
        teachPlanService.associationMedia(bindTeachPlanMediaDto);

    }






}



