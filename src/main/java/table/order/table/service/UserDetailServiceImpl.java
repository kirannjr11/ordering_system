package table.order.table.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import table.order.table.exception.InvalidUserDataException;
import table.order.table.model.User;
import table.order.table.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        user.orElseThrow(() -> new InvalidUserDataException("User not found with phone number :" +phoneNumber));
        return user.get();
    }
}
