package org.example.holssi_be.util;

import org.example.holssi_be.dto.UserDTO;
import org.example.holssi_be.dto.ResponseDTO;
import org.example.holssi_be.entity.domain.Users;
import org.example.holssi_be.exception.InvalidUserException;
import org.example.holssi_be.exception.VerificationException;
import org.example.holssi_be.service.AuthService;
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

    public static Map<Object, Object> getTemporaryUser(String identifier, AuthService authService) {
        Map<Object, Object> userData = authService.getTemporaryUser(identifier);
        if (userData.isEmpty()) {
            throw new InvalidUserException("Temporary user data not found.");
        }

        String storedValue = (String) userData.get("userEmail");
        if (!identifier.equals(storedValue)) {
            throw new InvalidUserException(identifier + " does not match.");
        }

        return userData;
    }

    public static ResponseDTO createVerifiedUser(boolean verified, Map<Object, Object> userData, String identifier, UserService userService, AuthService authService) {
        if (verified && !userData.isEmpty()) {
            Users user = new Users();
            user.setName((String) userData.get("name"));
            user.setUserEmail((String) userData.get("userEmail"));
            user.setPassword((String) userData.get("password"));
            user.setPhone((String) userData.get("phone"));
            user.setAddress((String) userData.get("address"));
            user.setAccount((String) userData.get("account"));
            user.setBank((String) userData.get("bank"));
            userService.save(user);
            authService.deleteTemporaryUser(identifier);
            return new ResponseDTO(true, "Verification Completed.", null);
        } else {
            authService.deleteTemporaryUser(identifier);
            throw new VerificationException("Verification Code does not match.");
        }
    }
}
