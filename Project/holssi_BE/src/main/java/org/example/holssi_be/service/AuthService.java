package org.example.holssi_be.service;

import lombok.RequiredArgsConstructor;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    // 이메일 중복확인
    private final MemberRepository memberRepository;

    public boolean emailCheck(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent();
    }

    // 이메일, 왓츠앱 인증
    private final RedisTemplate<String, String> redisTemplate;

    private final Random random = new Random();

    // 인증번호 생성
    public String generateCode() {
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    // 인증번호 저장
    public void saveCode(String identifier, String code) {
        redisTemplate.opsForValue().set(identifier, code, 5, TimeUnit.MINUTES); // 5분 동안 인증번호 유효
    }

    // 인증번호 확인
    public boolean verifyCode(String identifier, String code) {
        String cachedCode = redisTemplate.opsForValue().get(identifier);
        return code.equals(cachedCode);
    }

    // 임시 데이터 저장
    public void saveTemporaryUser(String identifier, Map<String, String> userData) {
        String key = "tempUser:" + identifier;
        redisTemplate.opsForHash().putAll(key, userData);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES); // 10분 동안 유효
    }

    public void saveTemporaryCollector(String identifier, Map<String, String> collectorData) {
        String key = "tempCollector:" + identifier;
        redisTemplate.opsForHash().putAll(key, collectorData);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES); // 10분 동안 유효
    }

    // 임시 데이터 가져오기
    public Map<Object, Object> getTemporaryUser(String identifier) {
        String key = "tempUser:" + identifier;
        return redisTemplate.opsForHash().entries(key);
    }

    public Map<Object, Object> getTemporaryCollector(String identifier) {
        String key = "tempCollector:" + identifier;
        return redisTemplate.opsForHash().entries(key);
    }

    // 임시 데이터 삭제
    public boolean deleteTemporaryUser(String identifier) {
        String key = "tempUser:" + identifier;
        return redisTemplate.delete(key); // 삭제 성공 여부를 반환
    }

    public boolean deleteTemporaryCollector(String identifier) {
        String key = "tempCollector:" + identifier;
        return redisTemplate.delete(key); // 삭제 성공 여부를 반환
    }
}

