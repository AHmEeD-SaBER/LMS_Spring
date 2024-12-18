//package dev.FCAI.LMS_Spring.security;
//
//import dev.FCAI.LMS_Spring.entities.User;
//import dev.FCAI.LMS_Spring.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//// Custom UserDetailsService to load user-specific data
//@Configuration
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//
//        // Create a list of authorities based on the user's role(s)
//        List<GrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
//
//        return new CustomUserDetails(user.getUsername(), user.getPassword(), roles);
//    }
//
//}
//
