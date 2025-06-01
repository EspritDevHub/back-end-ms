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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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


    public void activateUser(User user) {
        user.setActive(true);
        userRepository.save(user);
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
            if(!(user.getTempPasswordCode() == otp)){
                return false;
            }
            if(!(user.get_2faExpiryDate().getTime() > System.currentTimeMillis())){
                return false;
            }
        }
            String passWordAfterApplyingSalt = passWordSalter.saltPassWord(newPassword, user.getSalt());
        String hasedPassWordAfterSalt = securityConfig.passwordEncoder().encode(passWordAfterApplyingSalt);
        user.setPassword(hasedPassWordAfterSalt);
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
    public List<User> generateMockUsers() {
        List<User> users = new ArrayList<>();
        String[] classes = { "SE-2A", "GL-3B" };
        Random random = new Random();
        int userCountPerClass = 30;

        for (int i = 0; i < classes.length; i++) {
            for (int j = 1; j <= userCountPerClass; j++) {
                int index = i * userCountPerClass + j;
                User user = new User();
                user.setName("Student " + index);
                user.setEmail("student" + index + "@esprit.tn");
                user.setPassword("Password" + index + "!");
                user.setRole(Roles.STUDENT);
                user.setPhone("210000" + String.format("%02d", index));
                user.setIs2FAEnabled(random.nextBoolean());
                user.setToken("token-" + index);
                user.setEspritId("ESP" + String.format("%03d", index));
                user.setClassName(classes[i]);
                user.set_2faExpiryDate(Date.from(LocalDate.of(2025, 7, 1 + (index % 10))
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
                user.setActive(random.nextBoolean());
                user.setOldNotePi(10 + random.nextFloat() * 10); // between 10 and 20
                users.add(user);
            }
        }
        userRepository.saveAll(users);
        return users;
    }
    public List<List<User>> divideInto6Groups(String className) {
        List<User> allUsers = userRepository.getUsersByClassName(className);

        List<User> highGrades = allUsers.stream()
                .filter(u -> u.getOldNotePi() >= 17)
                .collect(Collectors.toList());

        List<User> midGrades = allUsers.stream()
                .filter(u -> u.getOldNotePi() >= 10 && u.getOldNotePi() <= 15)
                .collect(Collectors.toList());

        List<User> others = allUsers.stream()
                .filter(u -> u.getOldNotePi() < 10 || u.getOldNotePi() > 15 && u.getOldNotePi() < 17)
                .collect(Collectors.toList());

        // Shuffle all lists to randomize
        Collections.shuffle(highGrades);
        Collections.shuffle(midGrades);
        Collections.shuffle(others);

        List<List<User>> groups = new ArrayList<>();
        int groupCount = 6;

        for (int i = 0; i < groupCount; i++) {
            List<User> group = new ArrayList<>();

            // Try to get 3 high grade
            for (int j = 0; j < 3 && !highGrades.isEmpty(); j++) {
                group.add(highGrades.remove(0));
            }

            // Try to get 3 mid-grade
            for (int j = group.size(); j < 6 && !midGrades.isEmpty(); j++) {
                group.add(midGrades.remove(0));
            }

            // Fill remaining with others
            while (group.size() < 6 && !others.isEmpty()) {
                group.add(others.remove(0));
            }

            groups.add(group);
        }

        // Fill incomplete groups with leftovers
        List<User> leftovers = new ArrayList<>();
        leftovers.addAll(highGrades);
        leftovers.addAll(midGrades);
        leftovers.addAll(others);
        Collections.shuffle(leftovers);

        for (List<User> group : groups) {
            while (group.size() < 6 && !leftovers.isEmpty()) {
                group.add(leftovers.remove(0));
            }
        }

        return groups;
    }


}