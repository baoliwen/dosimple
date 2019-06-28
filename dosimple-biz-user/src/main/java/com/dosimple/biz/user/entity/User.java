package com.dosimple.biz.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("user")
@Data
public class User extends Model<User> {

    /**
     * 主键
     */
	private String id;
    /**
     * 手机号
     */
	private String mobile;
    /**
     * 密码
     */
	private String password;
    /**
     * 盐
     */
	private String salt;
    /**
     * session令牌
     */
	@TableField("session_token")
	private String sessionToken;
    /**
     * 创建时间（也就是注册时间）
     */
	@TableField("date_created")
	private Date dateCreated;
    /**
     * 修改时间
     */
	@TableField("date_modified")
	private Date dateModified;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
