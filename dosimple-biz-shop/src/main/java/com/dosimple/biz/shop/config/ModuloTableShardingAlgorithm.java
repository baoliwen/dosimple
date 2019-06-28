package com.dosimple.biz.shop.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.SingleKeyTableShardingAlgorithm;
import com.google.common.collect.Range;
import lombok.Data;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author baolw
 */
@Data
public class ModuloTableShardingAlgorithm implements SingleKeyTableShardingAlgorithm<Long> {
    private String id;
    private String name;

    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        for (String tableName : collection) {
            if (tableName.endsWith(shardingValue.getValue() % 2 + "")) {
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        for (Long value : shardingValue.getValues()) {
            for (String table : collection) {
                if (table.endsWith(value % 2 + "")) {
                    result.add(table);
                }
            }
        }
        return result;
    }

    @Override
    public Collection<String> doBetweenSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i < range.upperEndpoint(); i++) {
            for (String each : collection) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
