package hello.hellospringboot.service;

import hello.hellospringboot.LocalCache;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static hello.hellospringboot.LocalCache.*;
import static hello.hellospringboot.LocalCache.evictCache;

@Service
public class LocalCacheService {

    private final MemberService memberService;

    public LocalCacheService( MemberService memberService) {

        this.memberService = memberService;
    }
    /*
    * 캐시에서 데이터 가져오기.
    * */
    public Object getData(String key, String refreshKey) {
        // 캐시에서 데이터 가져오기
        Object cachedData = readCache(key);
        // 캐시 데이터 변경여부,, 회원 등록하면 캐시변경여부 true
        boolean refresh = (readCache(refreshKey) == null) ? false : (boolean) readCache(refreshKey);

        // 캐시에 데이터가 없으면 DB에서 다시 조회
        if ((cachedData == null) || (refresh)) {
            cachedData = getDatabase(key);
            // 조회한 데이터를 캐시에 저장
            writeCache(key, cachedData);
            // 변경여부캐시 지워주기
            evictCache(refreshKey);
        }
        return cachedData;
    }
    /*
    * DB에서 가져오기
    * */
    private Object getDatabase(String cacheKey) {
        Object returnVal = null;
        // 캐시에서 데이터 없을때 조회해 오기(점점 산으로 가는 기분,,)
        if("totCnt".equals(cacheKey)){  //
            returnVal = memberService.totalCnt();
        }
        return returnVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocalCacheService that = (LocalCacheService) o;

        return Objects.equals(memberService, that.memberService);
    }

    @Override
    public int hashCode() {
        return memberService != null ? memberService.hashCode() : 0;
    }
}
