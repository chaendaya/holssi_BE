package org.example.holssi_be.util;

import org.example.holssi_be.dto.AuthDTO;
import org.example.holssi_be.dto.CollectorDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.entity.domain.Collectors;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.exception.InvalidMemException;
import org.example.holssi_be.exception.InvalidRoleException;
import org.example.holssi_be.service.AuthService;
import org.example.holssi_be.service.CollectorService;
import org.example.holssi_be.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class AuthUtil {

    // 사용자 데이터 생성, 임시 사용자 데이터 검증, 검증 결과 처리
    public static Map<String, String> createTemporaryUser(UserDTO userDTO) {
        Map<String, String> userData = new HashMap<>();
        userData.put("name", userDTO.getName());
        userData.put("userEmail", userDTO.getUserEmail());
        userData.put("password", userDTO.getPassword());
        userData.put("phone", userDTO.getPhone());
        userData.put("address", userDTO.getAddress());
        userData.put("account", userDTO.getAccount());
        userData.put("bank", userDTO.getBank());
        return userData;
    }

    public static Map<String, String> createTemporaryCollector(CollectorDTO collectorDTO) {
        Map<String, String> collectorData = new HashMap<>();
        collectorData.put("name", collectorDTO.getName());
        collectorData.put("collectorEmail", collectorDTO.getCollectorEmail());
        collectorData.put("password", collectorDTO.getPassword());
        collectorData.put("phone", collectorDTO.getPhone());
        collectorData.put("address", collectorDTO.getAddress());
        return collectorData;
    }

    public static Map<Object, Object> getTemporaryUser(String identifier, AuthService authService) {
        Map<Object, Object> userData = authService.getTemporaryUser(identifier);
        if (userData.isEmpty()) {
            throw new InvalidMemException("Temporary user data not found.");
        }

        String storedValue = (String) userData.get("userEmail");
        if (!identifier.equals(storedValue)) {
            throw new InvalidMemException(identifier + " does not match.");
        }

        return userData;
    }

    public static Map<Object, Object> getTemporaryCollector(String identifier, AuthService authService) {
        Map<Object, Object> collectorData = authService.getTemporaryCollector(identifier);
        if (collectorData.isEmpty()) {
            throw new InvalidMemException("Temporary collector data not found.");
        }

        String storedValue = (String) collectorData.get("collectorEmail");
        if (!identifier.equals(storedValue)) {
            throw new InvalidMemException(identifier + " does not match.");
        }

        return collectorData;
    }

    public static String getEmailByRole(AuthDTO authDTO, AuthService authService) {
        String role = authDTO.getRole();
        String email = "";

        switch (role) {
            case "user":
                Map<Object, Object> userData = getTemporaryUser(authDTO.getPrimaryKey(), authService);
                email = (String) userData.get("userEmail");
                break;
            case "collector":
                Map<Object, Object> collectorData = getTemporaryCollector(authDTO.getPrimaryKey(), authService);
                email = (String) collectorData.get("collectorEmail");
                break;
            default:
                throw new InvalidRoleException("Invalid role specified.");
        }
        return email;
    }

    public static String getPhoneByRole(AuthDTO authDTO, AuthService authService) {
        String role = authDTO.getRole();
        String phone = "";

        switch (role) {
            case "user":
                Map<Object, Object> userData = getTemporaryUser(authDTO.getPrimaryKey(), authService);
                phone = (String) userData.get("phone");
                break;
            case "collector":
                Map<Object, Object> collectorData = getTemporaryCollector(authDTO.getPrimaryKey(), authService);
                phone = (String) collectorData.get("phone");
                break;
            default:
                throw new InvalidRoleException("Invalid role specified.");
        }
        return phone;
    }

    public static ResponseDTO verificationResult(AuthDTO authDTO, boolean verified, UserService userService, CollectorService collectorService, AuthService authService) {
        String role = authDTO.getRole();
        Map<Object, Object> tempData;

        switch (role) {
            case "user":
                tempData = getTemporaryUser(authDTO.getPrimaryKey(), authService);
                break;
            case "collector":
                tempData = getTemporaryCollector(authDTO.getPrimaryKey(), authService);
                break;
            default:
                throw new InvalidRoleException("Invalid role specified.");
        }

        if (verified && !tempData.isEmpty()) {
            if (role.equals("user")) {
                Users user = new Users();
                user.setName((String) tempData.get("name"));
                user.setEmail((String) tempData.get("userEmail"));
                user.setPassword((String) tempData.get("password"));
                user.setPhone((String) tempData.get("phone"));
                user.setAddress((String) tempData.get("address"));
                user.setAccount((String) tempData.get("account"));
                user.setBank((String) tempData.get("bank"));
                userService.save(user);
                authService.deleteTemporaryUser(authDTO.getPrimaryKey());
            } else if (role.equals("collector")) {
                Collectors collector = new Collectors();
                collector.setName((String) tempData.get("name"));
                collector.setEmail((String) tempData.get("collectorEmail"));
                collector.setPassword((String) tempData.get("password"));
                collector.setPhone((String) tempData.get("phone"));
                collector.setAddress((String) tempData.get("address"));
                collectorService.save(collector);
                authService.deleteTemporaryCollector(authDTO.getPrimaryKey());
            }
            return new ResponseDTO(true, "Verification Completed.", null);
        } else {
            if (role.equals("user")) {
                authService.deleteTemporaryUser(authDTO.getPrimaryKey());
            } else {
                authService.deleteTemporaryCollector(authDTO.getPrimaryKey());
            }
            throw new InvalidRoleException("Verification Code does not match.");
        }
    }
}
