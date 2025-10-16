package com.b00tc4mp.app.logic;

import com.b00tc4mp.app.data.Data;
import com.b00tc4mp.app.data.UserData;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LogicTest {

    private Logic logic;

    @BeforeEach
    void setUp() throws Exception {
        logic = Logic.get();
        logic.userId = null; // Reset userId before each test

        Data.get().removeUsers(); // Clear users before each test
    }

    // Tests for registerUser
    @Test
    void testRegisterUserSuccess() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "password123";

        logic.registerUser(name, username, password, confirmPassword);

        // Verify user was added by checking Data
        UserData user = Data.get().findUserByUsername(username);
        assertNotNull(user);

        assertEquals(name, user.getName());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
    }

    @Test
    void testRegisterUserEmptyName() {
        String name = "";
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "password123";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserNullName() {
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "password123";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(null, username, password, confirmPassword));

        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserEmptyUsername() {
        String name = "Test User";
        String username = "";
        String password = "password123";
        String confirmPassword = "password123";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserNullUsername() {
        String name = "Test User";
        String password = "password123";
        String confirmPassword = "password123";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, null, password, confirmPassword));

        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserEmptyPassword() {
        String name = "Test User";
        String username = "testuser";
        String password = "";
        String confirmPassword = "";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserNullPassword() {
        String name = "Test User";
        String username = "testuser";
        String confirmPassword = "password123";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, null, confirmPassword));
        assertEquals("Password cannot be empty", exception.getMessage());

    }

    @Test
    void testRegisterUserEmptyConfirmPassword() {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("Confirm Password cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserNullConfirmPassword() {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";

        Exception exception = assertThrows(Exception.class, () -> logic.registerUser(name, username, password, null));

        assertEquals("Confirm Password cannot be empty", exception.getMessage());
    }

    @Test
    void testRegisterUserPasswordsDoNotMatch() {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "different";

        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void testRegisterUserDuplicateUsername() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";
        String confirmPassword = "password123";

        // Register user first
        Data.get().addUser(new UserData(name, username, password));

        // Try registering same username again
        Exception exception = assertThrows(Exception.class,
                () -> logic.registerUser(name, username, password, confirmPassword));

        assertEquals("User already exists", exception.getMessage());
    }

    // Tests for loginUser
    @Test
    void testloginUserSuccess() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";

        // Register user first
        Data.get().addUser(new UserData(name, username, password));

        // Authenticate
        logic.loginUser(username, password);
        // No exception means success

        assertEquals(logic.userId, Data.get().findUserByUsername(username).getId());
    }

    @Test
    void testloginUserEmptyUsername() {
        String username = "";
        String password = "password123";

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(username, password));

        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testloginUserNullUsername() {
        String password = "password123";

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(null, password));

        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void testloginUserEmptyPassword() {
        String username = "testuser";
        String password = "";

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(username, password));

        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void testloginUserNullPassword() {
        String username = "testuser";

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(username, null));

        assertEquals("Password cannot be empty", exception.getMessage());
    }

    @Test
    void testloginUserUserNotFound() {
        String username = "testuser";
        String password = "password123";

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(username, password));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testloginUserInvalidPassword() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";
        String wrongPassword = "wrongpassword";

        // Register user first
        Data.get().addUser(new UserData(name, username, password));

        Exception exception = assertThrows(Exception.class, () -> logic.loginUser(username, wrongPassword));

        assertEquals("Invalid password", exception.getMessage());
    }

    // Tests for logoutUser
    @Test
    void testLogoutUser() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";

        // Register user first
        UserData user = new UserData(name, username, password);

        logic.userId = user.getId();

        logic.logoutUser();

        assertNull(logic.userId);
    }

    // Tests for isAuthenticated
    @Test
    void testIsAuthenticatedWhenLoggedIn() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";

        // Register user first
        UserData user = new UserData(name, username, password);
        Data.get().addUser(user);

        logic.userId = user.getId();
        // No exception means success

        assertTrue(logic.isUserLoggedIn());

        assertEquals(logic.userId, Data.get().findUserByUsername(username).getId());
    }

    @Test
    void testIsAuthenticatedWhenNotLoggedIn() {
        assertFalse(logic.isUserLoggedIn());
    }

    // Tests for getCurrentUser
    @Test
    void testGetCurrentUserWhenLoggedIn() throws Exception {
        String name = "Test User";
        String username = "testuser";
        String password = "password123";

        // Register user first
        UserData user = new UserData(name, username, password);
        Data.get().addUser(user);

        logic.userId = user.getId();

        User currentUser = logic.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(user.getId(), currentUser.getId());
        assertEquals(user.getName(), currentUser.getName());
        assertEquals(user.getUsername(), currentUser.getUsername());
    }

    @Test
    void testGetCurrentUserWhenNotLoggedIn() {
        Exception exception = assertThrows(Exception.class, () -> logic.getCurrentUser());

        assertEquals("No user is currently logged in", exception.getMessage());
    }
}