package tn.esprit.pi.usermanagement.suermanagementmicroservice.services;
import io.jsonwebtoken.Claims;
import org.springdoc.core.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.Roles;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.repository.IUseRepository;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.security.*;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private IUseRepository userRepository;
    @Autowired
    private PassWordSalter passWordSalter;
    @Autowired
    private SecurityConfig securityConfig;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;
    @Autowired
    private AuthenticatorService authenticatorService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id,String token) {
        Claims claims = jwtUtil.getClaims(token);
        String role = claims.get("roles", String.class);
        String senderId = claims.get("id", String.class);
        if(!role.equals(Roles.ADMIN.toString()) && !senderId.equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this resource");
        }
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        user.setSalt(PassWordSalter.generateSalt());
        String passWordAfterApplyingSalt = passWordSalter.saltPassWord(user.getPassword(), user.getSalt());
        String hasedPassWordAfterSalt = securityConfig.passwordEncoder().encode(passWordAfterApplyingSalt);
        user.setPassword(hasedPassWordAfterSalt);
        String secretKey = authenticatorService.generateSecretKey();
        user.setSecretKey(secretKey);
        return userRepository.save(user);
    }

    public String getUserQrCodeFor2FA(String id, String token) {
        User user = getUserById(id, token);
        return qrCodeGenerator.generateQRCodeForUser(user);
    }

    public boolean verifyOtp(String email, int otp) {
        User user = userRepository.getUserByEmail(email);
        String secretKey = user.getSecretKey();
        return authenticatorService.verifySecretKey(secretKey, otp);
    }
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            return userRepository.save(user);
        }
        return null;
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }
    public User getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User getUserByID(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            password = passWordSalter.saltPassWord(password, user.getSalt());

            if (securityConfig.passwordEncoder().matches(password, user.getPassword())) {
                user.setToken(jwtUtil.generateToken(user));
                return user;
            }
        }
        return null;
    }

    public boolean logOut(String token) {
        try {
            TokenBlacklistService.addToBlacklist(token);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

}