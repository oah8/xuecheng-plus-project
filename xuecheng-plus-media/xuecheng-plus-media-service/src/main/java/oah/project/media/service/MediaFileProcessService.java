package oah.project.media.service;

import oah.project.media.model.po.MediaFiles;
import oah.project.media.model.po.MediaProcess;

import java.util.List;

/**
 * @ClassName MediaFileProcessService
 * @Description 任务处理
 * @Author _oah
 * @Date 2023.12.02 22:42
 * @Version 1.0
 */
public interface MediaFileProcessService {


    /**
     * 获取待处理任务
     * @param shardIndex 分片序号
     * @param shardTotal 分片总数
     * @param count 获取记录数
     * @return
     */
    public List<MediaProcess> getMediaProcessList(int shardIndex, int shardTotal, int count);


    /**
     *  开启一个事务
     * @param id 任务id
     * @return true开启任务成功，false开启任务失败
     */
    public boolean startTask(long id);


    /**
     * 保存任务结果
     * @param taskId 任务id
     * @param status 任务状态
     * @param fileId 文件id
     * @param url
     * @param errorMsg 错误信息
     */
    public void saveProcessFinishStatus(Long taskId, String status, String fileId, String url, String errorMsg);

}
