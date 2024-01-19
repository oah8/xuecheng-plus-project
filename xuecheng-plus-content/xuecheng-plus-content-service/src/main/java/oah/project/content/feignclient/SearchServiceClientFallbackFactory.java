package oah.project.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName SearchServiceClientFallbackFactory
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.23 20:21
 * @Version 1.0
 */
@Component
@Slf4j
public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {

	@Override
	public SearchServiceClient create(Throwable throwable) {
		return new SearchServiceClient() {
			@Override
			public Boolean add(CourseIndex courseIndex) {
				log.debug("添加课程索引发生课程熔断,索引信息:{},熔断异常:{}", courseIndex, throwable.toString(), throwable);
				// 走降级了返回false
				return false;
			}
		};
	}
}
