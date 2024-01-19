package oah.project.learning.service;

import oah.project.base.model.RestResponse;

/**
 * @ClassName LearningService
 * @Description 在线学习相关的接口
 * @Author _oah
 * @Date 2024.01.04 21:29
 * @Version 1.0
 */
public interface LearningService {

	/**
	 * 获取教学视频
	 * @param userId 用户id
	 * @param courseId 课程id
	 * @param teachplanId 课程计划id
	 * @param mediaId 视频id
	 * @return
	 */
	public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId);
}
