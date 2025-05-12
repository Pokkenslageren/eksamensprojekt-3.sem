package org.example.eksamensprojekt3sem.User;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Member.MemberModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserModel> getUserById(long id) {
        if(id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return userRepository.findById(id);
    }

    public UserModel addUser(UserModel user) {
        return userRepository.save(user);
    }

    public Optional<UserModel> updateUser(long id, @Valid UserModel userDetails) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        return userRepository.findById(id).map(user1 -> {
            user1.setUsername(userDetails.getUsername());
            user1.setPassword(userDetails.getPassword());
            user1.setUserRole(userDetails.getUserRole());
            return userRepository.save(user1);
        });
    }

    public boolean deleteUser(long id) {
        if (id <= 0){
            throw new IllegalArgumentException("ID must be greater than 0");
        }
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
