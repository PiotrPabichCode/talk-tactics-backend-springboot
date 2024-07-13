package com.piotrpabich.talktactics.repository;

import com.piotrpabich.talktactics.entity.Role;
import com.piotrpabich.talktactics.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    private User user;
    private List<User> userList;

    @BeforeEach
    public void init() {
        user = User.builder()
                .email("waynerooney@email.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .role(Role.USER)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build();
        userList = List.of(User.builder()
                .email("wayne_rooney@gmail.com")
                .firstName("Wayne")
                .lastName("Rooney")
                .role(Role.USER)
                .username("wayne_rooney")
                .bio("Passionate language enthusiast dedicated to unlocking the secrets of English fluency, one word at a time. Join me on a journey of linguistic exploration and mastery!")
                .build(),
        User.builder()
                .email("dwayne_johnson@gmail.com")
                .firstName("Dwayne")
                .lastName("Johnson")
                .role(Role.USER)
                .username("dwayne_johnson")
                .bio("Devoted language aficionado committed to unraveling the intricacies of English fluency, one word at a time. Embark on a voyage of linguistic discovery and expertise with me!")
                .build());
    }

    @Test
    public void UserRepository_Save_ReturnsUser() {
        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepository_FindAll_ReturnsUserList() {
        userRepository.saveAll(userList);
        List<User> foundUsers = userRepository.findAll();

        Assertions.assertThat(foundUsers).hasSize(userList.size());
        Assertions.assertThat(foundUsers).containsExactlyInAnyOrderElementsOf(userList);
    }

    @Test
    public void UserRepository_FindById_ReturnsUser() {
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get()).isEqualTo(savedUser);
    }

    @Test
    public void UserRepository_FindByUsername_ReturnsUser() {
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByUsername(savedUser.getUsername());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get()).isEqualTo(savedUser);
    }

    @Test
    public void UserRepository_FindByEmail_ReturnsUser() {
        User savedUser = userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail(savedUser.getEmail());

        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get()).isEqualTo(savedUser);
    }

    @Test
    public void UserRepository_Delete_RemovesUser() {
        User savedUser = userRepository.save(user);
        userRepository.delete(savedUser);

        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        Assertions.assertThat(foundUser).isEmpty();
    }
}
