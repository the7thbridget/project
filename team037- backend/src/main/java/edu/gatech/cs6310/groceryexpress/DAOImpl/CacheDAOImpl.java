package edu.gatech.cs6310.groceryexpress.DAOImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gatech.cs6310.groceryexpress.DAO.CacheDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Repository
public class CacheDAOImpl implements CacheDAO {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public void refreshStoreList(Map<String, Integer> storeMap) throws JsonProcessingException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String mapStr = objectMapper.writeValueAsString(storeMap);
        ops.set("storeMap", mapStr);
    }

    @Override
    public Map<String, Integer> getStoreMap() throws JsonProcessingException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String mapStr = ops.get("storeMap");
        if (mapStr == null) return null;
        Map<String, Integer> map = objectMapper.readValue(mapStr, Map.class);
        return map;
    }

    @Override
    public void refreshFunctionList() {

    }

    @Override
    public void writeSession(String username, Integer userType, Set<String> functions) throws JsonProcessingException {
        ValueOperations ops = stringRedisTemplate.opsForValue();
        String key = username + "_" + userType;
        String str = objectMapper.writeValueAsString(functions);
        ops.set(key, str, 1200, TimeUnit.SECONDS);
    }

    @Override
    public Set<String> checkSession(String username, Integer userType) throws JsonProcessingException {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        logger.info("Checked Redis for info...");
        String key = username + "_" + userType;
        String functionsStr = ops.get(key);
        if (functionsStr == null) return null;
        Set set = objectMapper.readValue(functionsStr, Set.class);
//        return (Set<String>) functionMap.get(key);
        return set;
    }

    public Object readTest(String key) {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public void deleteStoreMap() {
        stringRedisTemplate.delete("storeMap");
    }
}
