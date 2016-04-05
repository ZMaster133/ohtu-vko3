package ohtu.services;

import ohtu.domain.User;
import java.util.ArrayList;
import java.util.List;
import ohtu.data_access.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {

    private UserDao userDao;

    @Autowired
    public AuthenticationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean logIn(String username, String password) {
        for (User user : userDao.listAll()) {
            if (checkIfMatch(user, username, password)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkIfMatch(User user, String username, String password) {
        return user.getUsername().equals(username)
                && user.getPassword().equals(password);
    }

    public boolean createUser(String username, String password) {
        if (userDao.findByName(username) != null) {
            return false;
        }

        if (invalid(username, password)) {
            return false;
        }

        userDao.add(new User(username, password));

        return true;
    }

    private boolean invalid(String username, String password) {
        return invalidUsername(username) || invalidPassword(password);
    }

    private boolean invalidUsername(String username) {
        if (username.length() < 3) {
            return true;
        }

        return !isBetweenAZ(username);
    }

    private boolean invalidPassword(String password) {
        if (password.length() < 8) {
            return true;
        }

        return isBetweenAZ(password);
    }

    private boolean isBetweenAZ(String word) {
        for (char c : word.toCharArray()) {
            if (!isBetweenAZ(c)) {
                return false;
            }
        }

        return true;
    }

    private boolean isBetweenAZ(char c) {
        return (isUppercaseAZ(c) || isLowercaseAZ(c));
    }
    
    private boolean isUppercaseAZ(char c) {
        return (c >= 'A' && c <= 'Z');
    }
    
    private boolean isLowercaseAZ(char c) {
        return (c >= 'a' && c <= 'z');
    }
}
