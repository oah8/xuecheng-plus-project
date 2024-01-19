package oah.project.content.service;

import oah.project.content.model.po.CourseMarket;

/**
 * @ClassName CourseMarketService
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.16 22:00
 * @Version 1.0
 */
public interface CourseMarketService {

    /**
     * 查询营销信息
     * @param courseId
     */
    public CourseMarket findById(Long courseId);
}
