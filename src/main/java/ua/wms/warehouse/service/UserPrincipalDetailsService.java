package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.entity.User;
import ua.wms.warehouse.security.UserPrincipal;
import ua.wms.warehouse.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {

    private final UserRepository<User, Long> userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userOpt = this.userRepository.findByEmail(email);

        if (userOpt.isEmpty())
            throw new UsernameNotFoundException("Користувач не знайдений");

        return new UserPrincipal(userOpt.get());
    }

}