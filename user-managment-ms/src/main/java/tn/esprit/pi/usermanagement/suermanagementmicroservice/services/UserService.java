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

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;

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
    @Autowired
    private EmailService mailer;

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
        var exsistingUser = getUserByEmail(user.getEmail());
        var existingUserByEspritId = getStudentByEspritId(user.getEspritId());
        if(user.getEspritId().isEmpty() && existingUserByEspritId.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already exists");
        }
        if(exsistingUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already exists");
        }

        user.setSalt(PassWordSalter.generateSalt());
        var passwordWithoutHash = user.getPassword();
        String passWordAfterApplyingSalt = passWordSalter.saltPassWord(user.getPassword(), user.getSalt());
        String hasedPassWordAfterSalt = securityConfig.passwordEncoder().encode(passWordAfterApplyingSalt);
        user.setPassword(hasedPassWordAfterSalt);
        String secretKey = authenticatorService.generateSecretKey();
        user.setSecretKey(secretKey);
        mailer.sendWelcomeEmail(user.getEmail(), user.getName(), user.getEspritId() ,passwordWithoutHash);
        user.setActive(false);
        return userRepository.save(user);
    }

    public String getUserQrCodeFor2FA(String id, String token) {
        User user = getUserById(id, token);
        return qrCodeGenerator.generateQRCodeForUser(user);
    }

    public boolean verifyOtp(String email, int otp) {
        User user = userRepository.getUserByEmail(email);
        String secretKey = user.getSecretKey();
        user.setIs2FAEnabled(true);
        userRepository.save(user);
        return authenticatorService.verifySecretKey(secretKey, otp);
    }
    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            if (userDetails.getName() != null) {
                user.setName(userDetails.getName());
            }
            if (userDetails.getEmail() != null) {
                var exsistingUser = getUserByEmail(userDetails.getEmail());
                if(exsistingUser != null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "This email already exists");
                }
                user.setEmail(userDetails.getEmail());
            }
            if (userDetails.getPhone() != null) {
                user.setPhone(userDetails.getPhone());
            }
            return userRepository.save(user);
        }
        return null;
    }


    public boolean ResetPassWord(String email, String newPassword, int otp){
        User user = userRepository.getUserByEmail(email);
        if(user == null){
            return false;
        }
        if(user.getIs2FAEnabled()){
            String secretKey = user.getSecretKey();
            user.setIs2FAEnabled(true);
            userRepository.save(user);
             if(!authenticatorService.verifySecretKey(secretKey, otp)){
                 return false;
             }
        }
        if(!user.getIs2FAEnabled()){
            if(!(user.getTempPasswordCode() == otp) && !(user.get_2faExpiryDate().getTime() > System.currentTimeMillis())){
                return false;
            }
        }
        String passWordAfterApplyingSalt = passWordSalter.saltPassWord(newPassword, user.getSalt());
        String hasedPassWordAfterSalt = securityConfig.passwordEncoder().encode(passWordAfterApplyingSalt);
        user.setPassword(hasedPassWordAfterSalt);
        user.setActive(true);
        userRepository.save(user);
        return true;
    }
    public boolean sendForgetPasswordEmailIf2faIsDisabled(String email) {
        User user = userRepository.getUserByEmail(email);
        if(user == null){
            return false;
        }
        if(user.getIs2FAEnabled()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This User Must use 2FA to change password");
        }
        user.setTempPasswordCode(1000 + new Random().nextInt(9000));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        user.set_2faExpiryDate(calendar.getTime());
        userRepository.save(user);
        try{
            mailer.sendForgotPasswordEmail(user.getEmail(),user.getName(), user.getTempPasswordCode());
            return true;
        }
        catch(Exception e){
            return false;
        }
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

    public Optional<User> getStudentByEspritId(String espritId) {
        return Optional.ofNullable(userRepository.getUserByEspritId(espritId))
                .filter(user -> user.getRole() == Roles.STUDENT);
    }
    public String GetUserRole(String token) {
        Claims claims = jwtUtil.getClaims(token);
        return claims.get("roles", String.class);
    }

}