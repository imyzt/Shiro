package top.imyzt.shiro.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Created by imyzt on 2018/9/25 20:13
 */
@Component
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    private Jedis getResources() {
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResources();
        try {
            jedis.set(key, value);
            return value;
        } finally {
            jedis.close();
        }
    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResources();
        try {
            jedis.expire(key, i);
        } finally {
            jedis.close();
        }
    }


    public byte[] get(byte[] key) {
        Jedis jedis = getResources();
        try {
            return jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getResources();
        try {
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(byte[] s) {
        Jedis jedis = getResources();
        try {
            return jedis.keys(s);
        } finally {
            jedis.close();
        }
    }
}
