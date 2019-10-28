package pl.trello.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.trello.dto.request.AddUserRequest;
import pl.trello.entity.User;
import pl.trello.repository.UserRepository;
import pl.trello.validated.ValidatedAddUserRequest;
import pl.trello.validators.AddUserRequestValidator;
import pl.trello.validators.ValidatedRequest;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AddUserRequestValidator addUserValidator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AddUserRequestValidator addUserValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.addUserValidator = addUserValidator;
    }

    public ResponseEntity register(AddUserRequest request) {
        ValidatedRequest validate = addUserValidator.validate(request);

        if(validate.isValid()) {
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            User user = User.builder()
                    .login(request.getLogin())
                    .password(encodedPassword)
                    .build();
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }

        return validate.getResponseEntity();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .map(user -> new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), Collections.emptyList()))
                .orElseThrow(() -> new UsernameNotFoundException("Not found username=" + username));
    }
}
