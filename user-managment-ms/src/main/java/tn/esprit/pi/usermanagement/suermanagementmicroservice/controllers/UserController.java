package tn.esprit.pi.usermanagement.suermanagementmicroservice.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserRequestDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.mappers.UserMapper;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.security.JwtUtil;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @GetMapping("GetAllUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        try{
            User user = userService.getUserById(id, token);
            var response =  userMapper.userToUserResponseDTO(user);
            return ResponseEntity.ok(response);
        }
        catch (ResponseStatusException ex){
            return ResponseEntity.status(401).body("can't access another user data");
        }
    }

    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO user) {
        var resposne = userService.createUser(userMapper.userRequestDTOToUser(user));
        return userMapper.userToUserResponseDTO(resposne);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable String id, @RequestBody User userDetails) {
        return userService.updateUser(id, userDetails);
    }
    @GetMapping("/{id}/qr")
    public ResponseEntity<String> getUserQRCode(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String qrCodeBase64 = userService.getUserQrCodeFor2FA(id, token);
        return ResponseEntity.ok(qrCodeBase64);
    }

    @PostMapping("/verify-otp")
    public boolean verifyOtp(@RequestParam String email, @RequestParam int otp) {
     return userService.verifyOtp(email, otp);
    }
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser != null) {
            return ResponseEntity.ok(loggedInUser); // Return user details
        }
        return ResponseEntity.status(401).body("Invalid email or password");
    }

    @GetMapping("/logout")
    public boolean logout(@RequestParam String token) {
        return userService.logOut(token);
    }

    @GetMapping("/verifyToken")
    public void verifyToken() {
        //based on the code of this response we should know the token is good "200 OK" or "403, 401"
        //this should be called before any endpoint execution in the gateway
        //to add in the middleware of the gateway or to be called manually your choice
    }
}
