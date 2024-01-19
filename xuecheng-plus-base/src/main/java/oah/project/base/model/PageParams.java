package oah.project.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName PageParams
 * @Description 分页查询分页参数
 * @Author _oah
 * @Date 2023.11.10 17:08
 * @Version 1.0
 */
@Data
@ToString
public class PageParams {

    // 当前页码
    @ApiModelProperty("页码")
    private Long pageNo = 1L;

    // 每页显示记录数
    @ApiModelProperty("每页记录数")
    private Long pageSize = 30L;


    public PageParams() {
    }


    public PageParams(Long pageNo, Long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}





