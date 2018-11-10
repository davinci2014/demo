package com.example.demo.web.rest;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SmsService;
import com.example.demo.service.UserService;
import com.example.demo.service.dto.UserDTO;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.web.rest.errors.BadRequestAlertException;
import com.example.demo.web.rest.errors.LoginAlreadyUsedException;
import com.example.demo.web.rest.util.HeaderUtil;
import com.example.demo.web.rest.vm.ManagedUserVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);
    private final UserRepository userRepository;
    private final UserService userService;
    private final SmsService smsService;
    private final UserMapper userMapper;

    public AccountResource(UserRepository userRepository, UserService userService, SmsService smsService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.smsService = smsService;
        this.userMapper = userMapper;
    }

    /**
     * POST  /users : Creates a new user.
     *
     * Creates a new user if the login and email are not already used, and sends a sms notification.
     * The user needs to be activated on creation.
     *
     * @param managedUserVM the managed user View Model
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     *
     * @throws BadRequestAlertException 400 (Bad Request) if the login or email is already in use
     * @throws LoginAlreadyUsedException 400 (Bad Request) if the login is already used
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.info("REST request to create User : {}", managedUserVM);

        if (managedUserVM.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
        } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else {
            User user = userService.createUser(managedUserVM, managedUserVM.getPassword());

            String phoneNumber = new String(Base64.getDecoder().decode(user.getPhoneNumber()));
            smsService.sendRegisterSms(phoneNumber, user.getNickname());

            return ResponseEntity.created(new URI("/api/users/" + managedUserVM.getLogin()))
                    .headers(HeaderUtil.createAlert( "userManagement.created", managedUserVM.getLogin()))
                    .body(this.userMapper.userToUserDTO(user));
        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.info("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

}
