package org.example.eksamensprojekt3sem.User;

import jakarta.validation.Valid;
import org.example.eksamensprojekt3sem.Member.MemberModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users/add")
    public UserModel createUser(@Valid @RequestBody UserModel user) {
        return userService.addUser(user);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable long id, @RequestBody UserModel userDetails) {
        return userService.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<UserModel> deleteUser(@PathVariable long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
