package com.dosimple.biz.shop.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author baolw
 */
@Data
@TableName("t_order")
public class Order extends Model<Order> {
    @Id
    private Long id;
    @TableField("user_id")
    private Long userId;
    @TableField("order_id")
    private Long orderId;
    private String orderNo;
    private byte isactive;
    private Date inserttime;
    private Date updatetime;
}
