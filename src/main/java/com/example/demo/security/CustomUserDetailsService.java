package com.example.demo.security;

import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.debug("Authenticating {}", s);

        String lowercaseLogin = s.toLowerCase(Locale.ENGLISH);
        Optional<User> userByLoginFromDatabase = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin);

        // 普通写法
//        return userByLoginFromDatabase.map(new Function<User, org.springframework.security.core.userdetails.User>() {
//            @Override
//            public org.springframework.security.core.userdetails.User apply(User user) {
//                return CustomUserDetailsService.this.createSpringSecurityUser(lowercaseLogin, user);
//            }
//        }).orElseThrow(new Supplier<UsernameNotFoundException>() {
//            @Override
//            public UsernameNotFoundException get() {
//                return new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
//            }
//        });
        // lambda写法
        return userByLoginFromDatabase.map(user -> createSpringSecurityUser(lowercaseLogin, user))
                .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.isActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }

        // 遍历user权限, 并将其放置GrantedAuthority集合中
        // 普通写法
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        for (Authority authority : user.getAuthorities()) {
//            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority.getName());
//            grantedAuthorities.add(simpleGrantedAuthority);
//        }

        // lambda写法
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuthorities);
    }
}
