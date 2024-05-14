package ua.wms.warehouse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.wms.warehouse.dto.RegistrationForm;
import ua.wms.warehouse.entity.Client;
import ua.wms.warehouse.entity.User;
import ua.wms.warehouse.repository.ClientRepository;
import ua.wms.warehouse.repository.UserRepository;
import ua.wms.warehouse.security.Authority;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository<User, Long> userRepository;

    private final ClientRepository clientRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean isUserExist( RegistrationForm registrationForm) {
        return userRepository.existsByEmail(registrationForm.getEmail()) || userRepository.existsByTelephone(registrationForm.getTelephone());
    }

    public Client registerClient(RegistrationForm registrationForm) {
        var client = new Client();

        client.setFirstname(registrationForm.getFirstname());
        client.setLastname(registrationForm.getLastname());
        client.setPatronymic(registrationForm.getPatronymic());
        client.setEmail(registrationForm.getEmail().toLowerCase());
        client.setTelephone(registrationForm.getTelephone());

        client.setAuthorities(Collections.singleton(Authority.ROLE_CLIENT));
        client.setPassword(passwordEncoder.encode(registrationForm.getPassword()));

        clientRepository.save(client);

        return client;
    }

}
