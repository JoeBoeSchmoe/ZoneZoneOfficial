package com.zonezone.backend.accountHandler.accountControllers;

import com.zonezone.backend.accountHandler.accountRelatedModels.UserAccountModel;
import com.zonezone.backend.accountHandler.accountRelatedRepositories.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accountUsers")
public class GeneralAccountController {

    private final UserAccountRepository userAccountRepository;
    private static final Logger logger = LoggerFactory.getLogger(GeneralAccountController.class);

    public GeneralAccountController(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    // GET Function To Return A List Of All The Users
    @GetMapping("/listUsers")
    public ResponseEntity<Map<String, Object>> listAllUsers() {
        try {
            List<UserAccountModel> users = userAccountRepository.findAll();

            if (users.isEmpty()) {
                logger.warn("User list request: No users found.");
                return buildResponse(true, "No users found.", null);
            }

            logger.info("User list request successful. {} users retrieved.", users.size());
            return buildResponse(true, "User list retrieved successfully.", users);

        } catch (Exception e) {
            logger.error("Error retrieving user list", e);
            return buildResponse(false, "An error occurred while retrieving users.", null);
        }
    }

    // Standardizes My API Responses
    private ResponseEntity<Map<String, Object>> buildResponse(boolean success, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", message);
        response.put("data", data);
        return ResponseEntity.ok(response);
    }
}
