package t1.study.springsecurity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import t1.study.springsecurity.dto.UserDTO;
import t1.study.springsecurity.exception.NotFoundException;
import t1.study.springsecurity.mapper.UserMapper;
import t1.study.springsecurity.model.RoleType;
import t1.study.springsecurity.model.User;
import t1.study.springsecurity.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User createUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(Collections.singleton(RoleType.ROLE_USER)));
        return userRepository.save(user);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким username не существует"));
    }

    public Optional<User> findOptionalByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким username не существует"));
    }
}
