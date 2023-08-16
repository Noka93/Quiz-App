package com.remidiousE.service;

import com.remidiousE.exceptions.UserNotFoundException;
import com.remidiousE.repository.UserRepository;
import com.remidiousE.security.UserRegistrationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return userRepository.findByEmail(email)
                    .map(UserRegistrationDetails::new)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
