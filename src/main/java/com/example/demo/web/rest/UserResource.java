package com.example.demo.web.rest;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthoritiesConstants;
import com.example.demo.service.UserService;
import com.example.demo.service.dto.UserDTO;
import com.example.demo.web.rest.errors.InvalidPasswordException;
import com.example.demo.web.rest.errors.LoginAlreadyUsedException;
import com.example.demo.web.rest.vm.ManagedUserVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserResource {

    private UserService userService;
    private UserRepository userRepository;

    public UserResource(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {throw new LoginAlreadyUsedException();});
        userService.registerUser(managedUserVM, managedUserVM.getPassword());
    }

    @GetMapping("/users")
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllUsers(pageable);
        return new ResponseEntity<>(page.getContent(), new HttpHeaders(), HttpStatus.OK);
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
                password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
                password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
