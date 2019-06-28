package com.dosimple.common.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class BingImageUtils {
    /**
     * 同步必应壁纸
     * @param index 起始点，0表示今天，1表示昨天，2前天，以此类推
     * @param sum 同步壁纸的数量，最多7张
     */
    public static Integer download(Integer index, Integer sum){
        Integer i = 0;
        String result = HttpUtil.get("https://www.bing.com/HPImageArchive.aspx?format=js&idx="+index+"&n="+sum);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        JSONArray array = JSONUtil.parseArray(jsonObject.get("images"));
        for (int j = 0; j < array.size(); j++) {
            String url = "http://s.cn.bing.net"+(JSONUtil.parseObj(array.get(j)).get("url").toString());
            //TODO 处理
        }
        return i;
    }
}
