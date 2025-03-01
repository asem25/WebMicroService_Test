package ru.semavin.microservice.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.semavin.microservice.dtos.UserDTO;
import ru.semavin.microservice.repositrories.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDTO createUser(UserDTO userDto) {
        return null;
    }

    public UserDTO getUserById(Long id) {
        return null;
    }

    public UserDTO updateUser(Long id, UserDTO userDto) {
        return null;
    }

    public void deleteUser(Long id) {
    }

    public List<UserDTO> getAllUsers() {
        return new ArrayList<>();
    }
}
