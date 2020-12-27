package io.github.thesixonenine.ware.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * 库存工作单
 * 
 * @author thesixonenine
 * @date 2020-06-06 01:52:04
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName("wms_ware_order_task_detail")
public class WareOrderTaskDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * sku_id
	 */
	private Long skuId;
	/**
	 * sku_name
	 */
	private String skuName;
	/**
	 * 购买个数
	 */
	private Integer skuNum;
	/**
	 * 工作单id
	 */
	private Long taskId;
	/**
	 * 仓库id
	 */
	private Long wareId;
	/**
	 * 状态: 1-已锁定 2-已解锁 3-已扣减
	 */
	private Integer lockStatus;

	@Getter
	@AllArgsConstructor
	public enum LockStatusEnum{
		/**
		 * 1-锁定
		 */
		LOCKED(1, "已锁定"),
		/**
		 * 2-解锁
		 */
		UNLOCK(2, "已解锁"),
		/**
		 * 3-已扣减
		 */
		REDUCED(3, "已扣减");
		private Integer code;
		private String status;
	}
}
