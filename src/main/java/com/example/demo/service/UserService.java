package com.example.demo.service;

import com.example.demo.domain.Authority;
import com.example.demo.domain.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthoritiesConstants;
import com.example.demo.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@CacheConfig
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

//    @Cacheable(value = "getUsers")
//    public List<User> getUsers() {
//        return userRepository.findAll();
//    }

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByActivatedTrue(pageable).map(UserDTO::new);
    }

    public User registerUser(UserDTO userDTO, String password) {
        User newUser = new User();
        newUser.setLogin(userDTO.getLogin());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNickname(userDTO.getNickname());
        newUser.setActivated(true);

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        return newUser;
    }
}
