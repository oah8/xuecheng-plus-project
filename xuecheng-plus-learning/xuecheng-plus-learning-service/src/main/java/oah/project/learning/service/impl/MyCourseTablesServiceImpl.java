package oah.project.learning.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import oah.project.base.exception.XueChengPlusException;
import oah.project.base.model.PageResult;
import oah.project.content.model.po.CoursePublish;
import oah.project.learning.feignclient.ContentServiceClient;
import oah.project.learning.mapper.XcChooseCourseMapper;
import oah.project.learning.mapper.XcCourseTablesMapper;
import oah.project.learning.model.dto.MyCourseTableParams;
import oah.project.learning.model.dto.XcChooseCourseDto;
import oah.project.learning.model.dto.XcCourseTablesDto;
import oah.project.learning.model.po.XcChooseCourse;
import oah.project.learning.model.po.XcCourseTables;
import oah.project.learning.service.MyCourseTablesService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName MyCourseTablesServiceImpl
 * @Description 选课相关接口实现
 * @Author _oah
 * @Date 2023.12.31 21:05
 * @Version 1.0
 */
@Slf4j
@Service
public class MyCourseTablesServiceImpl implements MyCourseTablesService {

    @Autowired
    private XcChooseCourseMapper xcChooseCourseMapper;


    @Autowired
    private XcCourseTablesMapper xcCourseTablesMapper;


    @Autowired
    private ContentServiceClient contentServiceClient;


    @Transactional
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {

        // 调用选课内容管理查询课程的收费规则
        CoursePublish coursepublish = contentServiceClient.getCoursepublish(courseId);
        if(coursepublish == null) {
            XueChengPlusException.cast("课程不存在");
        }
        // 收费规则
        String charge = coursepublish.getCharge();
        // 选课记录
        XcChooseCourse xcChooseCourse = null;
        if("201000".equals(charge)) { // 免费课程
            // 如果课程免费，会向选课记录表、我的课程表写数据
            xcChooseCourse = addFreeCourse(userId, coursepublish); // 向选课记录表写
            // 向我的课程表写
            XcCourseTables xcCourseTables = addCourseTables(xcChooseCourse);

        } else {
            // 如果收费课程，会向课程记录表写数据
            xcChooseCourse = addChargeCourse(userId, coursepublish);
        }

        // 判断学生的学习资格
        XcCourseTablesDto xcCourseTablesDto = getLearningStatus(userId, courseId);
        // 构造返回值
        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();
        BeanUtils.copyProperties(xcChooseCourse, xcChooseCourseDto);
        // 设置学习资格状态
        xcChooseCourseDto.setLearnStatus(xcCourseTablesDto.getLearnStatus());

        return xcChooseCourseDto;
    }

    // 添加免费课程，免费课程加入选课记录表、我的课程表
    public XcChooseCourse addFreeCourse(String userId, CoursePublish coursePublish) {
        // 课程id
        Long courseId = coursePublish.getId();
        // 判断，如果存在免费的选课记录且选课状态为成功，直接返回了
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getOrderType, "700001") // 免费课程
                .eq(XcChooseCourse::getStatus, "701001");// 选课成功
        List<XcChooseCourse> xcChooseCourses = xcChooseCourseMapper.selectList(queryWrapper);

        if(xcChooseCourses.size() > 0) {
            return xcChooseCourses.get(0);
        }

        // 向选课记录表写数据
        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(courseId);
        xcChooseCourse.setCourseName(coursePublish.getName());
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
        xcChooseCourse.setOrderType("700001"); // 免费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setCoursePrice(coursePublish.getPrice());
        xcChooseCourse.setValidDays(365);
        xcChooseCourse.setStatus("701001"); // 选课成功
        xcChooseCourse.setValidtimeStart(LocalDateTime.now()); // 有效期的开始时间
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365)); // 有效期的结束时间

        int insert = xcChooseCourseMapper.insert(xcChooseCourse);
        if(insert <= 0) {
            XueChengPlusException.cast("添加选课记录失败");
        }
        return xcChooseCourse;
    }

    // 添加到我的课程表
    public XcCourseTables addCourseTables(XcChooseCourse xcChooseCourse) {


        // 选课成功了才可以向我的课程表添加
        String status = xcChooseCourse.getStatus();
        if(!"701001".equals(status)) {
            XueChengPlusException.cast("选课没有成功，无法添加到课程表");
        }
        XcCourseTables xcCourseTables = getXcCourseTables(xcChooseCourse.getUserId(), xcChooseCourse.getCourseId());
        if(xcCourseTables != null) {
            return xcCourseTables;
        }

        xcCourseTables = new XcCourseTables();
        BeanUtils.copyProperties(xcChooseCourse, xcCourseTables);
        xcCourseTables.setChooseCourseId(xcChooseCourse.getId()); // 记录选课表的主键
        xcCourseTables.setCourseType(xcChooseCourse.getOrderType()); // 选课类型
        xcCourseTables.setUpdateDate(LocalDateTime.now());

        int insert = xcCourseTablesMapper.insert(xcCourseTables);
        if(insert <= 0) {
            XueChengPlusException.cast("添加我的课程表失败");
        }
        return xcCourseTables;
    }


    // 添加收费课程
    public XcChooseCourse addChargeCourse(String userId, CoursePublish coursePublish) {

        // 课程id
        Long courseId = coursePublish.getId();
        // 判断，如果存在收费的选课记录且选课状态为待支付，直接返回了
        LambdaQueryWrapper<XcChooseCourse> queryWrapper = new LambdaQueryWrapper<XcChooseCourse>()
                .eq(XcChooseCourse::getCourseId, courseId)
                .eq(XcChooseCourse::getOrderType, "700002") // 收费课程
                .eq(XcChooseCourse::getStatus, "701002");// 待支付
        List<XcChooseCourse> xcChooseCourses = xcChooseCourseMapper.selectList(queryWrapper);

        if(xcChooseCourses.size() > 0) {
            return xcChooseCourses.get(0);
        }

        // 向选课记录表写数据
        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        xcChooseCourse.setCourseId(courseId);
        xcChooseCourse.setCourseName(coursePublish.getName());
        xcChooseCourse.setUserId(userId);
        xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
        xcChooseCourse.setOrderType("700002"); // 收费课程
        xcChooseCourse.setCreateDate(LocalDateTime.now());
        xcChooseCourse.setCoursePrice(coursePublish.getPrice());
        xcChooseCourse.setValidDays(365);
        xcChooseCourse.setStatus("701002"); // 待支付
        xcChooseCourse.setValidtimeStart(LocalDateTime.now()); // 有效期的开始时间
        xcChooseCourse.setValidtimeEnd(LocalDateTime.now().plusDays(365)); // 有效期的结束时间

        int insert = xcChooseCourseMapper.insert(xcChooseCourse);
        if(insert <= 0) {
            XueChengPlusException.cast("添加选课记录失败");
        }
        return xcChooseCourse;

    }


    /**
     * @description 根据课程和用户查询我的课程表中某一门课程
     * @param userId
     * @param courseId
     * @return com.xuecheng.learning.model.po.XcCourseTables
     * @author Mr.M
     * @date 2022/10/2 17:07
     */
    public XcCourseTables getXcCourseTables(String userId, Long courseId){
        XcCourseTables xcCourseTables = xcCourseTablesMapper.selectOne(new LambdaQueryWrapper<XcCourseTables>().eq(XcCourseTables::getUserId, userId).eq(XcCourseTables::getCourseId, courseId));
        return xcCourseTables;

    }


    /**
     * 判断选课资格
     * @param userId
     * @param courseId
     * @return //学习资格，[{"code":"702001","desc":"正常学习"},
     * {"code":"702002","desc":"没有选课或选课后没有支付"},
     * {"code":"702003","desc":"已过期需要申请续期或重新支付"}]
     */
    @Override
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        // 返回的结果
        XcCourseTablesDto xcCourseTablesDto = new XcCourseTablesDto();

        // 查询我的课程表，如果查不到说明没有选课
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        if(xcCourseTables == null) {
            // {"code":"702002","desc":"没有选课或选课后没有支付"}
            xcCourseTablesDto.setLearnStatus("702002");
            return xcCourseTablesDto;
        }
        // 如果查到了，判断是否过期，如果过期不能继续学习，如果没过期可以继续学习
        boolean before = xcCourseTables.getValidtimeEnd().isBefore(LocalDateTime.now());
        if(before) {
            // {"code":"702003","desc":"已过期需要申请续期或重新支付"}]
            BeanUtils.copyProperties(xcCourseTables, xcCourseTablesDto);
            xcCourseTablesDto.setLearnStatus("702003");
            return xcCourseTablesDto;
        } else {
            // [{"code":"702001","desc":"正常学习"},
            BeanUtils.copyProperties(xcCourseTables, xcCourseTablesDto);
            xcCourseTablesDto.setLearnStatus("702001");
            return xcCourseTablesDto;
        }
    }


    @Override
    public boolean saveChooseCourseSuccess(String chooseCourseId) {

        // 根据选课id查询选课表
        XcChooseCourse xcChooseCourse = xcChooseCourseMapper.selectById(chooseCourseId);
        if(xcChooseCourse == null) {
            log.debug("接收购买课程的消息，根据选课id从数据库找不到选课记录,选课id:{}", chooseCourseId);
            return false;
        }
        // 选课状态
        String status = xcChooseCourse.getStatus();
        if("701002".equals(status)) {
            // 更新选课记录的状态为支付成功
            xcChooseCourse.setStatus("701001");
            int i = xcChooseCourseMapper.updateById(xcChooseCourse);
            if(i <= 0) {
                log.debug("添加选课记录失败:{}", xcChooseCourse);
                XueChengPlusException.cast("添加选课记录失败");
            }
            // 向我的课程表插入记录
            XcCourseTables xcCourseTables = addCourseTables(xcChooseCourse);
            return true;
        }
        return false;
    }


    @Override
    public PageResult<XcCourseTables> myCourseTables(MyCourseTableParams params) {
        // 用户id
        String userId = params.getUserId();
        // 当前页码
        int pageNo = params.getPage();
        // 每页记录数
        int size = params.getSize();

        Page<XcCourseTables> courseTablesPage = new Page<>(pageNo, size);
        LambdaQueryWrapper<XcCourseTables> queryWrapper = new LambdaQueryWrapper<XcCourseTables>().eq(XcCourseTables::getUserId, userId);

        // 查询数据
        Page<XcCourseTables> result = xcCourseTablesMapper.selectPage(courseTablesPage, queryWrapper);
        // 数据列表
        List<XcCourseTables> records = result.getRecords();

        // 总记录数
        long total = result.getTotal();

        // List<T> items, long counts, long page, long pageSize
        PageResult pageResult = new PageResult(records, total, pageNo, size);
        return pageResult;
    }
}
