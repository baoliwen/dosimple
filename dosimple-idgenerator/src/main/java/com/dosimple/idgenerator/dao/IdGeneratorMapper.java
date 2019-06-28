package com.dosimple.idgenerator.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dosimple.idgenerator.entity.IdGenerator;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author dosimple
 * @since 2019-05-13
 */
@Repository
public interface IdGeneratorMapper extends BaseMapper<IdGenerator> {
}