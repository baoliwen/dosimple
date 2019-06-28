package com.dosimple.biz.shop.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author baolw
 */
@Data
@TableName("order_test")
public class OrderInfo extends Model<OrderInfo> {
    @Id
    private Long id;
    private String orderId;

    private String name;


}
