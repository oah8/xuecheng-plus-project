package oah.project.content.service.impl;

import oah.project.content.mapper.CourseMarketMapper;
import oah.project.content.model.po.CourseMarket;
import oah.project.content.service.CourseMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CourseMarketServiceImpl
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.16 22:00
 * @Version 1.0
 */
@Service
public class CourseMarketServiceImpl implements CourseMarketService {
    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Override
    public CourseMarket findById(Long courseId) {
        return courseMarketMapper.selectById(courseId);
    }
}
