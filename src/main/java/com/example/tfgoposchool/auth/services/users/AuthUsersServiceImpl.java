package com.example.tfgoposchool.auth.services.users;

import com.example.tfgoposchool.auth.repository.AuthUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUsersServiceImpl implements AuthUsersService{
    private final AuthUsersRepository repository;

    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe"));
    }
}
