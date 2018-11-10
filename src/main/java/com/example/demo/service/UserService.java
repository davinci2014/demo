package com.example.demo.service;

import com.example.demo.domain.Authority;
import com.example.demo.domain.User;
import com.example.demo.repository.AuthorityRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.AuthoritiesConstants;
import com.example.demo.service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
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

    public Page<UserDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAllByActivatedTrue(pageable).map(UserDTO::new);
    }

    public User createUser(UserDTO userDTO, String password) {
        User newUser = new User();
        newUser.setLogin(userDTO.getLogin());
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setPhoneNumber(Base64.getEncoder().encodeToString(userDTO.getPhoneNumber().getBytes()));
        newUser.setNickname(userDTO.getNickname());
        newUser.setActivated(true);
        newUser.setCreatedBy(userDTO.getCreatedBy());
        newUser.setLastModifiedBy(userDTO.getLastModifiedBy());

        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        return newUser;
    }
}
