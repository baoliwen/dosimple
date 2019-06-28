package com.dosimple.idgenerator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author dosimple
 * @since 2019-05-13
 */
@TableName("t_id_generator")
@Data
public class IdGenerator extends Model<IdGenerator> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	@TableField("biz_tag")
	private String bizTag;
	@TableField("max_id")
	private Long maxId;
	private Integer step;
	private String bizDesc;
	@TableField("date_udpated")
	private Date dateUdpated;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
