package com.dosimple.idgenerator.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dosimple.idgenerator.entity.IdGenerator;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dosimple
 * @since 2019-05-13
 */
public interface IIdGeneratorService extends IService<IdGenerator> {

    List<Long> generatorId(String bizTag);

    List<Long> createIdsByBizTag(String bizTag);
}
