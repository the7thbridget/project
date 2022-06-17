package edu.gatech.cs6310.groceryexpress.DAO;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;
import java.util.Set;

public interface CacheDAO {
    void refreshStoreList(Map<String, Integer> storeMap) throws JsonProcessingException;
    Map<String, Integer> getStoreMap() throws JsonProcessingException;
    void refreshFunctionList();
    void writeSession(String username, Integer userType, Set<String> functions) throws JsonProcessingException;
    Set<String> checkSession(String username, Integer userType) throws JsonProcessingException;
    Object readTest(String key);
    void deleteStoreMap();
}
