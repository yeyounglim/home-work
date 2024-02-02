package hello.hellospringboot;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component // SpringConfig에서 직접 등록해주려다가 실패...,
public class LocalCache {
    private static final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();
    public static Object readCache(String cacheKey){
        return cache.get(cacheKey); //캐시 읽어오기
    }

    public static void writeCache(String cacheKey, Object cacheVal){
        cache.put(cacheKey, cacheVal); // 캐시 등록하기
    }

    public static void evictCache(String cacheKey) {
        cache.remove(cacheKey); // 캐시 지우기
    }

    public static void clear() {
        cache.clear();      //캐시 전체 지우기
    }
}
