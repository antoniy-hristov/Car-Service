package com.carservice.services;

import com.carservice.data.entities.Role;
import com.carservice.data.entities.User;
import com.carservice.data.repositories.UserRepository;
import com.carservice.web.dto.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserService extends BaseService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public List<User> getAllEmployees() {
        Role role = new Role();
        List<User> allUsers = this.userRepository.findAll();
        allUsers.removeIf(user -> !user.getRole_id().getAuthority().equals("EMPLOYEE"));
        return allUsers;
    }

    public Optional<User> getUserById(Long user_id) {
        return this.userRepository.findById(user_id);
    }

    public User createUser(UserDto dto) {
        User user = map(dto, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreationTime(Timestamp.valueOf(LocalDateTime.now()));

        return userRepository.save(user);
    }
}
