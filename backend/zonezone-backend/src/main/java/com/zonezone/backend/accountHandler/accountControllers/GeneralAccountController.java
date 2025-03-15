package com.zonezone.backend.accountHandler.accountControllers;

import com.zonezone.backend.accountHandler.accountRelatedModels.UserAccountModel;
import com.zonezone.backend.accountHandler.accountRelatedRepositories.UserAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/accountUsers")
public class GeneralAccountController {

    @Autowired
    private UserAccountRepository userAccountRepository;
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

    @PostMapping("/createUser")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody Map<String, String> userRequest) {
        Map<String, Object> response = new HashMap<>();

        // Define required and optional parameters
        String[] requiredParams = {"accountUsername", "accountPassword", "accountEmail", "inCountry", "userGender", "userBirthday"};
        String[] optionalParams = {"selectedLanguage", "userTimeZone", "accountType"};

        List<String> missingParams = new ArrayList<>();

        // Check for missing required parameters
        for (String param : requiredParams) {
            if (!userRequest.containsKey(param) || userRequest.get(param).trim().isEmpty()) {
                missingParams.add(param);
            }
        }

        // If any required fields are missing, return error response
        if (!missingParams.isEmpty()) {
            response.put("error", "Missing required parameters: " + String.join(", ", missingParams));
            return ResponseEntity.badRequest().body(response);
        }

        // Extract required parameters
        String accountUsername = userRequest.get("accountUsername");
        String accountPassword = userRequest.get("accountPassword");
        String accountEmail = userRequest.get("accountEmail");
        String inCountry = userRequest.get("inCountry");
        String userGender = userRequest.get("userGender");
        String userBirthday = userRequest.get("userBirthday");

        // Check if username or email already exists
        if (userAccountRepository.findByAccountUsername(accountUsername).isPresent()) {
            response.put("error", "Username is already taken");
            return ResponseEntity.badRequest().body(response);
        }
        if (userAccountRepository.findByAccountEmail(accountEmail).isPresent()) {
            response.put("error", "Email is already taken");
            return ResponseEntity.badRequest().body(response);
        }

        // Extract optional parameters (fallback to default values)
        String selectedLanguage = userRequest.getOrDefault("selectedLanguage", "English");
        String userTimeZone = userRequest.getOrDefault("userTimeZone", "UTC");
        String accountType = userRequest.getOrDefault("accountType", "Standard");

        // Create new user
        UserAccountModel newUser = new UserAccountModel();
        newUser.setAccountUsername(accountUsername);
        newUser.setAccountPassword(accountPassword); // Password should be hashed in production
        newUser.setAccountEmail(accountEmail);
        newUser.setInCountry(inCountry);
        newUser.setUserGender(userGender);
        newUser.setUserBirthday(userBirthday);

        // Set optional values
        newUser.setSelectedLanguage(selectedLanguage);
        newUser.setUserTimeZone(userTimeZone);
        newUser.setAccountType(accountType);

        newUser.onCreate(); // Initialize default values

        // Save user to database
        userAccountRepository.save(newUser);

        // Response output
        response.put("message", "User Created Successfully");
        response.put("userAccountID", newUser.getUserAccountID());
        response.put("accountUsername", newUser.getAccountUsername());
        response.put("accountPassword", newUser.getAccountPassword());
        response.put("accountEmail", newUser.getAccountEmail());
        response.put("inCountry", newUser.getInCountry());
        response.put("userGender", newUser.getUserGender());
        response.put("userBirthday", newUser.getUserBirthday());
        response.put("userAge", newUser.getUserAge());
        response.put("selectedLanguage", selectedLanguage);
        response.put("userTimeZone", userTimeZone);
        response.put("accountType", accountType);

        return ResponseEntity.ok(response);
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
