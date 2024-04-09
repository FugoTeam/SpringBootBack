package com.fugoworld.backend.controller;

import com.fugoworld.backend.model.User;
import com.fugoworld.backend.model.UserPartial;
import com.fugoworld.backend.repository.UserRepository;
import com.fugoworld.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;
import javax.validation.Valid;

import static com.fugoworld.backend.service.PasswordService.encrypt;
import static com.fugoworld.backend.service.PasswordService.isSecurePassword;


@RestController
@CrossOrigin
@RequestMapping(value={"/fugoworld"})
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping(value={"/users"})
    public ResponseEntity<?>  getAllUsers() {
        List<User> users = this.userRepository.findAll();
        if(users.isEmpty()){
            return new ResponseEntity<>("No user found with these names.", HttpStatus.NOT_FOUND);
        }else{
            List<UserPartial> usersPartial = new ArrayList<>();
            for(User user : users){
                usersPartial.add(user.toUserPartial());
            }
            return new ResponseEntity<>(usersPartial, HttpStatus.FOUND);
        }
    }

    @PostMapping(value={"/user"})
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (user != null) {
            try {
                this.userRepository.findUserByEmail(user.getEmail()).get();
                return new ResponseEntity<>((Object)"Already exist with this email.", HttpStatus.IM_USED);
            }
            catch (Exception e) {
                try {
                    user.setPassword(encrypt(user.getPassword()));
                    System.out.println(user.getPassword());
                }
                catch (UnsupportedEncodingException | GeneralSecurityException e1) {
                    System.out.println("Error while encrypting password.");
                }
                user.setAdmin(false);
                user.setVerified(false);
                user.setCode(null);
                user.setCodeExpiration(null);
                user.setLastConnexion(null);
                user.setUnsuccessfulAttempt(0);
                user.setCurrentToken(null);
                this.userRepository.save(user);
                return new ResponseEntity<>((Object)"User well created.", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>((Object)"Wrong parameters.", HttpStatus.FORBIDDEN);
    }

    @GetMapping(value={"/user/{id}"})
    public ResponseEntity<?> getUserById(@PathVariable(value="id") Long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if(user.isPresent()){
            user.get().setPassword("");
            user.get().setCurrentToken("");
            user.get().setCode("");
            return user.map(value -> new ResponseEntity<>((Object) value, HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>((Object) "User not found.", HttpStatus.NOT_FOUND));
        }else{
            return new ResponseEntity<>("No user found with this ID : "+userId, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value={"/user/{firstname}/{lastname}"})
    public ResponseEntity<?> getUserByName(@PathVariable(value="firstname") String firstname, @PathVariable(value="lastname") String lastname) {
        Optional<List<User>> users = this.userRepository.getUsersByNames(firstname, lastname);
        if(users.isEmpty()){
            return new ResponseEntity<>("No user found with these names.", HttpStatus.NOT_FOUND);
        }else{
            List<User> usersList = users.get();
            List<UserPartial> usersPartial = new ArrayList<>();
            for(User user : usersList){
                usersPartial.add(user.toUserPartial());
            }
            return new ResponseEntity<>(usersPartial, HttpStatus.FOUND);
        }
    }

    @GetMapping(value={"/user/mail/{mail}"})
    public ResponseEntity<?> getUserByMail(@PathVariable(value="mail") String mail) {
        Optional<User> user = this.userRepository.getUser(mail);
        return user.map(value -> new ResponseEntity<>((Object) value.toUserPartial(), HttpStatus.FOUND)).orElseGet(() -> new ResponseEntity<>((Object) "User not found.", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value={"/user/{id}"})
    public ResponseEntity<?> deleteUser(@PathVariable(value="id") Long userId) throws Exception {
        this.userRepository.deleteById(userId);
        Optional<User> user = this.userRepository.findById(userId);
        return user.map(value -> new ResponseEntity<>("User with ID" +userId+" is still remaining.", HttpStatus.BAD_REQUEST)).orElseGet(() -> new ResponseEntity<>("User with ID" +userId+" has been deleted.", HttpStatus.OK));
    }

    @PutMapping(value={"/user/firstname/{id}"})
    public ResponseEntity<?> updateUserFirstname(@PathVariable(value="id") Long userId, @Valid @RequestBody Map<String, String> body) throws Exception {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found."));
            String firstname = body.get("firstname");
            if (firstname != null && !firstname.isEmpty()) {
                user.setFirstname(firstname);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser.toUserPartial(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Firstname is required.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value={"/user/lastname/{id}"})
    public ResponseEntity<?> updateUserLastname(@PathVariable(value="id") Long userId,  @Valid @RequestBody Map<String, String> body) throws Exception {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found."));
            String lastname = body.get("lastname");
            if (lastname != null && !lastname.isEmpty()) {
                user.setLastname(lastname);
                User updatedUser = userRepository.save(user);
                return new ResponseEntity<>(updatedUser.toUserPartial(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Lastname is required.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value={"/user/password/{id}"})
    public ResponseEntity<?> updateUserPassword(@PathVariable(value="id") Long userId,  @Valid @RequestBody Map<String, String> body) throws Exception {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found."));
            String password = body.get("password");
            if (password != null && !password.isEmpty()) {
                if(isSecurePassword(password)){
                    user.setPassword(encrypt(password));
                    User updatedUser = userRepository.save(user);
                    return new ResponseEntity<>(updatedUser.toUserPartial(), HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Password must contain at least 8 characters, one uppercase letter, one lowercase letter and one digit.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>("Password is required.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value={"/user/login"})
    public ResponseEntity<?> login(@RequestBody Map<String, Object> payload) throws Exception {
        String mail = (String)payload.get("mail");
        String password = (String)payload.get("password");
        try {
            User user = this.userRepository.getUser(mail).get();
            boolean isTheSamePassword = false;
            System.out.println(user.getPassword());
            isTheSamePassword = PasswordService.check(user.getPassword(), password);
            if (isTheSamePassword) {
                String token = TokenService.getToken(user);
                user.setLastConnexion(new Date());
                user.setCurrentToken(token);
                user.setUnsuccessfulAttempt(0);
                this.userRepository.save(user);
                return new ResponseEntity<>((Object)token, HttpStatus.ACCEPTED);
            }
            user.setUnsuccessfulAttempt(user.getUnsuccessfulAttempt() + 1);
            this.userRepository.save(user);
            return new ResponseEntity<>((Object)"Wrong password", HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>((Object)("Error while log in : " + e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value={"/user/logout"})
    public ResponseEntity<?> logout(@RequestBody Map<String, Object> payload) throws Exception {
        String mail = (String)payload.get("mail");
        try {
            User user = this.userRepository.getUser(mail).get();
            user.setCurrentToken("");
            this.userRepository.save(user);
            return new ResponseEntity<>((Object)"Log out went well.", HttpStatus.ACCEPTED);
        }
        catch (Exception e) {
            return new ResponseEntity<>((Object)("Error while logout : " + e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value={"/user/token"})
    public ResponseEntity<?> token(@RequestBody Map<String, Object> payload) throws Exception {
        String mail = (String)payload.get("mail");
        String token = (String)payload.get("token");
        try {
            User user = this.userRepository.getUser(mail).get();
            boolean isTheSameToken = false;
            if (user.getCurrentToken() == null) {
                return new ResponseEntity<>((Object)"User not found or no current token.", HttpStatus.NOT_FOUND);
            }
            isTheSameToken = user.getCurrentToken().equals(token);
            if (isTheSameToken) {
                return new ResponseEntity<>((Object)"Token valid.", HttpStatus.OK);
            }
            return new ResponseEntity<>((Object)"Token not valid.", HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<>((Object)("Error verifying token : " + e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }
}
