package com.b00tc4mp.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Data extends JpaRepository<UserData, String> {
    void addUser(UserData user);
    List<UserData>getUsers();
    UserData findUserByUsername(String username);
    UserData findUserById(String id);
    void removeUsers();
}
