package oah.project.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import oah.project.media.model.po.MediaFiles;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 媒资信息 Mapper 接口
 * </p>
 *
 * @author itcast
 */
public interface MediaFilesMapper extends BaseMapper<MediaFiles> {

}
