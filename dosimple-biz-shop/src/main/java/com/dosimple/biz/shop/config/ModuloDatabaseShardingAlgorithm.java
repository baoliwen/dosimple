package com.dosimple.biz.shop.config;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;
import lombok.Data;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author baolw
 */
@Data
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {
    private String id;
    private String name;
    @Override
    public String doEqualSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        for (String databaseName : collection) {
            if (databaseName.endsWith(shardingValue.getValue() % 2 + "")) {
                return databaseName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doInSharding(Collection<String> collection, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(collection.size());
        for (Long value : shardingValue.getValues()) {
            for (String name : collection) {
                if (name.endsWith(value % 2 + "")) {
                    result.add(name);
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
