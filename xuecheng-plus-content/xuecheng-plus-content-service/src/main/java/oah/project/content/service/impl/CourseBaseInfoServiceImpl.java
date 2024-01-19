package oah.project.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import oah.project.base.exception.XueChengPlusException;
import oah.project.base.model.PageParams;
import oah.project.base.model.PageResult;
import oah.project.content.mapper.CourseBaseMapper;
import oah.project.content.mapper.CourseCategoryMapper;
import oah.project.content.mapper.CourseMarketMapper;
import oah.project.content.model.dto.AddCourseDto;
import oah.project.content.model.dto.CourseBaseInfoDto;
import oah.project.content.model.dto.EditCourseDto;
import oah.project.content.model.dto.QueryCourseParamsDto;
import oah.project.content.model.po.CourseBase;
import oah.project.content.model.po.CourseCategory;
import oah.project.content.model.po.CourseMarket;
import oah.project.content.service.CourseBaseInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName CourseBaseInfoServiceImpl
 * @Description TODO
 * @Author _oah
 * @Date 2023.11.10 20:09
 * @Version 1.0
 */
@Slf4j
@Service
public class CourseBaseInfoServiceImpl implements CourseBaseInfoService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;


    @Autowired
    private CourseCategoryMapper courseCategoryMapper;
    

    @Override
    public PageResult<CourseBase> queryCourseBaseList(Long companyId, PageParams pageParams, QueryCourseParamsDto courseParamsDto) {

        // 拼装查询条件
        LambdaQueryWrapper<CourseBase> queryWrapper = new LambdaQueryWrapper<>();
        // 根据名称模糊查询，在sql中拼接course_base.name like '%值%'
        queryWrapper.like(StringUtils.isNotEmpty(courseParamsDto.getCourseName()), CourseBase::getName, courseParamsDto.getCourseName());
        // 根据课程审核状态查询，在sql中拼接course_base.audit_status = ?
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getAuditStatus()), CourseBase::getAuditStatus, courseParamsDto.getAuditStatus());
        // TODO:按课程发布状态查询
        queryWrapper.eq(StringUtils.isNotEmpty(courseParamsDto.getPublishStatus()), CourseBase::getStatus, courseParamsDto.getPublishStatus());
        // TODO:根据培训机构id拼装查询条件
        queryWrapper.eq(CourseBase::getCompanyId,companyId);

        // 创建page分页参数对象，参数：当前页码，每页记录数
        Page<CourseBase> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        // 开始进行分页查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, queryWrapper);

        // 数据列表
        List<CourseBase> items = pageResult.getRecords();

        // 总记录数
        long total = pageResult.getTotal();


        // List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> courseBasePageResult = new PageResult<CourseBase>(items, total, pageParams.getPageNo(), pageParams.getPageSize());

        return courseBasePageResult;
    }

    @Transactional
    @Override
    public CourseBaseInfoDto createCourseBase(Long companyId, AddCourseDto dto) {

        //参数的合法性校验
//        if (StringUtils.isBlank(dto.getName())) {
////            throw new RuntimeException("课程名称为空");
//            XueChengPlusException.cast("课程名称为空");
//        }
//
//        if (StringUtils.isBlank(dto.getMt())) {
//            throw new RuntimeException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getSt())) {
//            throw new RuntimeException("课程分类为空");
//        }
//
//        if (StringUtils.isBlank(dto.getGrade())) {
//            throw new RuntimeException("课程等级为空");
//        }
//
//        if (StringUtils.isBlank(dto.getTeachmode())) {
//            throw new RuntimeException("教育模式为空");
//        }
//
//        if (StringUtils.isBlank(dto.getUsers())) {
//            throw new RuntimeException("适应人群为空");
//        }
//
//        if (StringUtils.isBlank(dto.getCharge())) {
//            throw new RuntimeException("收费规则为空");
//        }


        // 向课程基本信息表course_base写入数据
        CourseBase courseBase = new CourseBase();
        // 将传入的页面的参数放到courseBase对象
//        courseBase.setName(dto.getName());
//        courseBase.setDescription(dto.getDescription());
        // 上边的从原始对象中get拿数据向新对象set，比较复杂
        BeanUtils.copyProperties(dto, courseBase); // 只要属性名称一致就可以拷贝
        courseBase.setCompanyId(companyId);
        courseBase.setCreateDate(LocalDateTime.now());
        // 审核状态默认为未提交
        courseBase.setAuditStatus("202002");
        // 发布状态为未发布
        courseBase.setStatus("203001");
        // 插入数据库
        int insert = courseBaseMapper.insert(courseBase);
        if(insert <= 0) {
            throw new RuntimeException("添加课程失败");
        }

        // 向课程营销信息表course_market写入数据
        CourseMarket courseMarket = new CourseMarket();
        // 将页面输入的数据拷贝到courseMarket
        BeanUtils.copyProperties(dto, courseMarket);
        // 主键的课程id
        Long courseId = courseBase.getId();
        courseMarket.setId(courseId);
        // 保存营销信息
        saveCourseMarket(courseMarket);

        // 从数据库查询课程的详细信息，包括两部分
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);

        return courseBaseInfo;
    }

    // 查询课程信息
    public CourseBaseInfoDto getCourseBaseInfo(Long courseId) {
        // 从课程基本信息表查询
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            return null;
        }
        // 从课程营销表查询
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);

        // 组装在一起
        CourseBaseInfoDto courseBaseInfoDto = new CourseBaseInfoDto();
        BeanUtils.copyProperties(courseBase, courseBaseInfoDto);
        if(courseMarket != null) {
            BeanUtils.copyProperties(courseMarket, courseBaseInfoDto);
        }
        // 通过courseCategoryMapper查询分类信息，将分类名称放在courseBaseInfoDto对象
        // TODO: 课程分类的名称设置到对象中
        String mt = courseBase.getMt();
        String st = courseBase.getSt();
        CourseCategory mtName = courseCategoryMapper.selectById(mt);
        CourseCategory stName = courseCategoryMapper.selectById(st);
        if(mtName != null) {
            courseBaseInfoDto.setMtName(mtName.getName());
        }
        if(stName != null) {
            courseBaseInfoDto.setStName(stName.getName());
        }

        return courseBaseInfoDto;
    }


    @Override
    public CourseBaseInfoDto updateCourseBase(Long companyId, EditCourseDto editCourseDto) {


        // 拿到课程id
        Long courseId = editCourseDto.getId();
        // 查询课程信息
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        if(courseBase == null) {
            XueChengPlusException.cast("课程不存在");
        }

        // 数据的合法性校验
        // 根据具体的业务逻辑修改本机构的课程
        // 本机构只能修改本机构的课程
        if(!companyId.equals(courseBase.getCompanyId())) {
            XueChengPlusException.cast("本机构只能修改本机构的课程");
        }

        // 封装数据
        BeanUtils.copyProperties(editCourseDto, courseBase);
        // 修改时间
        courseBase.setChangeDate(LocalDateTime.now());

        // 更新数据库
        int i = courseBaseMapper.updateById(courseBase);

        if(i < 0) {
            XueChengPlusException.cast("修改课程失败");
        }

        // 更新营销信息
        CourseMarket courseMarket = new CourseMarket();
        BeanUtils.copyProperties(editCourseDto, courseMarket);
        saveCourseMarket(courseMarket);

        // 查询课程信息
        CourseBaseInfoDto courseBaseInfo = getCourseBaseInfo(courseId);

        return courseBaseInfo;
    }

    // 单独写一个方法保存营销信息，逻辑：存在则更新，不存在则添加
    private int saveCourseMarket(CourseMarket courseMarketNew) {

        // 参数的合法性校验
        String charge = courseMarketNew.getCharge();
        if(StringUtils.isEmpty(charge)) {
            throw new RuntimeException("收费规则为空");
        }

        // 如果课程收费，价格没有填写也需要抛出异常
        if(charge.equals("201001")) {
            if(courseMarketNew.getCharge() == null || courseMarketNew.getPrice().floatValue() <= 0) {
//                throw new RuntimeException("课程的价格不能为空并且必须大于0");
                XueChengPlusException.cast("课程的价格不能为空并且必须大于0");
            }
        }

        // 从数据库查询营销信息，存在则更新，不存在则添加
        Long id = courseMarketNew.getId(); // 主键
        CourseMarket courseMarket = courseMarketMapper.selectById(id);
        if(courseMarket == null) {
            // 插入数据库
            int insert = courseMarketMapper.insert(courseMarketNew);
            return insert;
        } else {
            // 将courseMarketNew拷贝到courseMarket
            BeanUtils.copyProperties(courseMarketNew, courseMarket);
            courseMarket.setId(courseMarketNew.getId());
            // 更新
            int i = courseMarketMapper.updateById(courseMarket);
            return i;
        }
    }
}




















