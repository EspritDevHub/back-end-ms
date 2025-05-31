package tn.esprit.pi.usermanagement.suermanagementmicroservice.controllers;
import lombok.AllArgsConstructor;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private final UserMapper userMapper;



    @GetMapping("GetAllUsers")
    public List<UserResponseDTO> getAllUsers() {
        var response = userService.getAllUsers();
        return userMapper.usersListToUserResponseDTO(response);
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
    public UserResponseDTO updateUser(@PathVariable String id, @RequestBody UserRequestDTO userDetailsRequest) {
        var userDetails = userMapper.userRequestDTOToUser(userDetailsRequest);
        var response = userService.updateUser(id, userDetails);
        return userMapper.userToUserResponseDTO(response);
    }
    @GetMapping("/{id}/qr")
    public ResponseEntity<Map<String, String>> getUserQRCode(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String qrCodeBase64 = userService.getUserQrCodeFor2FA(id, token);

        Map<String, String> response = new HashMap<>();
        response.put("qrCode", qrCodeBase64);

        return ResponseEntity.ok(response);
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
    public ResponseEntity<?> login(@RequestBody UserRequestDTO user) {
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        if (loggedInUser != null) {
            var loggedInUserDTO = userMapper.userToUserResponseDTO(loggedInUser);
            return ResponseEntity.ok(loggedInUserDTO); // Return user details
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
    @GetMapping("/espritid/{id}")
    public UserResponseDTO getUserByEspritId(@PathVariable String id) throws Exception {
        var user = userService.getStudentByEspritId(id);
        if(user.isPresent()) {
            return userMapper.userToUserResponseDTO(user.get());
        }
        throw new Exception();
    }
    @GetMapping("/getUserRole")
    public String getUserRole(@RequestParam String token) {
        return userService.GetUserRole(token);
    }

    @GetMapping("/SendforgetPasswordMail")
    public ResponseEntity<?> sendForgetPasswordMail(@RequestParam String email) {
        try{
            userService.sendForgetPasswordEmailIf2faIsDisabled(email);
            return ResponseEntity.status(200).body("mail sent");
        }
        catch (Exception e){
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/resetPassword")
    public ResponseEntity<?> ResetPassWord(@RequestParam String email, @RequestParam String password, @RequestParam int otp) {
        try{
            var result = userService.ResetPassWord(email, password, otp);
            if(result){
                Map<String, String> response = new HashMap<>();
                response.put("message", "saved");
                return ResponseEntity.ok(response);

            }
            Map<String, String> response = new HashMap<>();
            response.put("message", "wrong otp");
            return ResponseEntity.internalServerError().body(response);
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Password reset failed");
        }
    }
}
