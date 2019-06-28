package com.dosimple.biz.shop.constant;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * @author baolw
 */
public interface Constant {
    HashMap<String, BlockingQueue<Long>> CACHE = new HashMap<>();
}
