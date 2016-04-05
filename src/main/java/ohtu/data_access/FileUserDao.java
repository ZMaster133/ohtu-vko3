package ohtu.data_access;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import ohtu.domain.User;

public class FileUserDao implements UserDao {

    private String url;

    public FileUserDao(String url) {
        this.url = url;
    }

    @Override
    public List<User> listAll() {
        List<User> users = new ArrayList<User>();

        try {
            Scanner scanner = new Scanner(new File(url));
            while (scanner.hasNext()) {
                users.add(getUser(scanner.nextLine()));
            }
        } catch (FileNotFoundException ex) {}

        return users;
    }

    private User getUser(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line);
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();

        return new User(username, password);
    }

    @Override
    public User findByName(String name) {
        List<User> users = listAll();

        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    @Override
    public void add(User user) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(url);
            writer.write(user.getUsername() + " " + user.getPassword() + "\n");
        } catch (IOException ex) {
        } finally {
            closeWriter(writer);
        }
    }

    private void closeWriter(FileWriter writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException ex) {
            }
        }
    }
}
