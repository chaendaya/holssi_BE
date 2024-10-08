package org.example.holssi_be.util;

import org.example.holssi_be.dto.AuthDTO;
import org.example.holssi_be.dto.CollectorDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Member;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.exception.InvalidCodeException;
import org.example.holssi_be.exception.InvalidException;
import org.example.holssi_be.exception.InvalidRoleException;
import org.example.holssi_be.repository.MemberRepository;
import org.example.holssi_be.service.AuthService;
import org.example.holssi_be.service.CollectorService;
import org.example.holssi_be.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthUtil {

    // 사용자 데이터 생성, 임시 사용자 데이터 검증, 검증 결과 처리
    public static Map<String, String> createTemporaryUser(UserDTO userDTO) {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", userDTO.getName());
        userData.put("userEmail", userDTO.getEmail());
        userData.put("password", userDTO.getPassword());
        userData.put("phone", userDTO.getPhone());
        userData.put("location", userDTO.getLocation());
        userData.put("account", userDTO.getAccount());
        userData.put("bank", userDTO.getBank());
        return userData;
    }

    public static Map<String, String> createTemporaryCollector(CollectorDTO collectorDTO) {
        Map<String, String> collectorData = new HashMap<>();
        collectorData.put("name", collectorDTO.getName());
        collectorData.put("collectorEmail", collectorDTO.getEmail());
        collectorData.put("password", collectorDTO.getPassword());
        collectorData.put("phone", collectorDTO.getPhone());
        collectorData.put("location", collectorDTO.getLocation());
        return collectorData;
    }

    public static Map<Object, Object> getTemporaryUser(String identifier, AuthService authService) {
        Map<Object, Object> userData = authService.getTemporaryUser(identifier);
        if (userData.isEmpty()) {
            throw new InvalidException("Temporary user data not found.");
        }

        String storedValue = (String) userData.get("userEmail");
        if (!identifier.equals(storedValue)) {
            throw new InvalidException(identifier + " does not match.");
        }

        return userData;
    }

    public static Map<Object, Object> getTemporaryCollector(String identifier, AuthService authService) {
        Map<Object, Object> collectorData = authService.getTemporaryCollector(identifier);
        if (collectorData.isEmpty()) {
            throw new InvalidException("Temporary collector data not found.");
        }

        String storedValue = (String) collectorData.get("collectorEmail");
        if (!identifier.equals(storedValue)) {
            throw new InvalidException(identifier + " does not match.");
        }

        return collectorData;
    }

    public static String getEmailByRole(AuthDTO authDTO, AuthService authService) {
        String role = authDTO.getRole();
        String email = "";

        switch (role) {
            case "user":
                Map<Object, Object> userData = getTemporaryUser(authDTO.getEmail(), authService);
                email = (String) userData.get("userEmail");
                break;
            case "collector":
                Map<Object, Object> collectorData = getTemporaryCollector(authDTO.getEmail(), authService);
                email = (String) collectorData.get("collectorEmail");
                break;
            default:
                throw new InvalidRoleException();
        }
        return email;
    }

    public static String getPhoneByRole(AuthDTO authDTO, AuthService authService) {
        String role = authDTO.getRole();
        String phone = "";

        switch (role) {
            case "user":
                Map<Object, Object> userData = getTemporaryUser(authDTO.getEmail(), authService);
                phone = (String) userData.get("phone");
                break;
            case "collector":
                Map<Object, Object> collectorData = getTemporaryCollector(authDTO.getEmail(), authService);
                phone = (String) collectorData.get("phone");
                break;
            default:
                throw new InvalidRoleException();
        }
        return phone;
    }

    public static ResponseDTO verificationResult(AuthDTO authDTO, boolean verified, UserService userService, CollectorService collectorService, AuthService authService, MemberRepository memberRepository) {
        String role = authDTO.getRole();
        Map<Object, Object> tempData;

        switch (role) {
            case "user":
                tempData = getTemporaryUser(authDTO.getEmail(), authService);
                break;
            case "collector":
                tempData = getTemporaryCollector(authDTO.getEmail(), authService);
                break;
            default:
                throw new InvalidRoleException();
        }

        if (verified && !tempData.isEmpty()) {
            Member member = new Member();
            member.setEmail(authDTO.getEmail());
            member.setPassword((String) tempData.get("password"));
            member.setName((String) tempData.get("name"));
            member.setPhone((String) tempData.get("phone"));
            member.setRole(role);
            memberRepository.save(member);

            if (role.equals("user")) {
                Users user = new Users();
                user.setMember(member);
                user.setLocation((String) tempData.get("location"));
                user.setAccount((String) tempData.get("account"));
                user.setBank((String) tempData.get("bank"));
                userService.save(user);
                authService.deleteTemporaryUser(authDTO.getEmail());
            } else if (role.equals("collector")) {
                Collectors collector = new Collectors();
                collector.setMember(member);
                collector.setLocation((String) tempData.get("location"));
                collectorService.save(collector);
                authService.deleteTemporaryCollector(authDTO.getEmail());
            }
            return new ResponseDTO(true, "Verification Completed.", null);
        } else {
            if (role.equals("user")) {
                authService.deleteTemporaryUser(authDTO.getEmail());
            } else {
                authService.deleteTemporaryCollector(authDTO.getEmail());
            }
            throw new InvalidCodeException();
        }
    }
}
