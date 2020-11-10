package com.apply.ism.common.constants;

public interface RedisKey {
    /**
     * 生成订单号时，用来保存末三位随机数
     */
    String ORDER_NO_GENERATOR = "ORDER_NO:";
}
