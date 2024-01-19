package oah.project.content.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @ClassName SearchServiceClient
 * @Description TODO
 * @Author _oah
 * @Date 2023.12.23 20:18
 * @Version 1.0
 */
@FeignClient(value = "search", fallbackFactory = SearchServiceClientFallbackFactory.class)
public interface SearchServiceClient {


	@PostMapping("/search/index/course")
	public Boolean add(@RequestBody CourseIndex courseIndex);



}
