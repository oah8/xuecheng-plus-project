package oah.project.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import oah.project.base.exception.XueChengPlusException;
import oah.project.content.mapper.TeachplanMapper;
import oah.project.content.mapper.TeachplanMediaMapper;
import oah.project.content.model.dto.BindTeachPlanMediaDto;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.model.dto.SaveTeachPlanDto;
import oah.project.content.model.dto.TeachplanDto;
import oah.project.content.model.po.Teachplan;
import oah.project.content.model.po.TeachplanMedia;
import oah.project.content.service.TeachPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName TeachPlanServiceImpl
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.14 18:47
 * @Version 1.0
 */
@Service
public class TeachPlanServiceImpl implements TeachPlanService {

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

    @Override
    public List<TeachplanDto> findTeachPlanTree(Long courseId) {
        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(courseId);
        return teachplanDtos;
    }

    private int getTeachPlanCount(Long courseId, Long parentId) {
        LambdaQueryWrapper<Teachplan> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper = queryWrapper.eq(Teachplan::getCourseId, courseId).eq(Teachplan::getParentid, parentId);
        Integer count = teachplanMapper.selectCount(queryWrapper);
        return count + 1;
    }


    @Override
    public void saveTeachPlan(SaveTeachPlanDto saveTeachPlanDto) {
        // 通过课程计划id判断是新增和修改
        Long teachPlanId = saveTeachPlanDto.getId();
        if(teachPlanId == null) {
            // 新增
            Teachplan teachplan = new Teachplan();
            BeanUtils.copyProperties(saveTeachPlanDto, teachplan);
            // 确定排序字段，找到同级节点个数，排序字段就是个数加1 select * from teachplan where course_id = 117 and parentid = 268;
            Long parentid = saveTeachPlanDto.getParentid();
            Long courseId = saveTeachPlanDto.getCourseId();
            int teachPlanCount = getTeachPlanCount(courseId, parentid);
            teachplan.setOrderby(teachPlanCount);

            teachplanMapper.insert(teachplan);
        } else {
            // 修改
            Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
            // 将参数复制到teachplan
            BeanUtils.copyProperties(saveTeachPlanDto, teachplan);
            teachplanMapper.updateById(teachplan);

        }
    }

    @Transactional
    @Override
    public void associationMedia(BindTeachPlanMediaDto bindTeachPlanMediaDto) {
        // 课程计划id
        Long teachPlanId = bindTeachPlanMediaDto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(teachPlanId);
        if(teachplan == null) {
            XueChengPlusException.cast("课程计划不存在");
        }

        // 先删除原有记录，根据课程计划id删除它所绑定的媒资

        int delete = teachplanMediaMapper.delete(new LambdaQueryWrapper<TeachplanMedia>().eq(TeachplanMedia::getTeachplanId, bindTeachPlanMediaDto.getTeachplanId()));

        // 再添加新记录
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        BeanUtils.copyProperties(bindTeachPlanMediaDto, teachplanMedia);
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setMediaFilename(bindTeachPlanMediaDto.getFileName());
        teachplanMediaMapper.insert(teachplanMedia);


    }
}
















