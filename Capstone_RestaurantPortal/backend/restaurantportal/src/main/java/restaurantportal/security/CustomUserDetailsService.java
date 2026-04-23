package restaurantportal.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;

// Loads user from database and converts it to UserDetails which is used by spring security for authentication and authorization
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
// fetch user detail from db if not found authentication not successful.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return new CustomUserDetails(user);
    }
}