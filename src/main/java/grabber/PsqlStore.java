package grabber;

import ru.job4j.quartz.HabrCareerParse;
import ru.job4j.utils.HabrCareerDateTimeParser;
import ru.job4j.utils.Post;
import ru.job4j.utils.Store;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private Connection connection;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement p = connection.prepareStatement("insert into post(name,text,link,created) "
                + "values(?,?,?,?) ON CONFLICT (link) DO NOTHING", Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, post.getTitle());
            p.setString(2, post.getDescription());
            p.setString(3, post.getLink());
            p.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            p.execute();
            try (ResultSet generatedKeys = p.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    post.setId(generatedKeys.getInt("id"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Post generatePost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("text"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement p = connection.prepareStatement("select * from post")) {
            try (ResultSet resultSet = p.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(generatePost(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement p = connection.prepareStatement("select * from post where id = ?")) {
            p.setInt(1, id);
            try (ResultSet resultSet = p.executeQuery()) {
                if (resultSet.next()) {
                    post = generatePost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }


    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        HabrCareerDateTimeParser timeParser = new HabrCareerDateTimeParser();
        HabrCareerParse parse = new HabrCareerParse(timeParser);
        Properties properties = new Properties();
        try (InputStream input = PsqlStore.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PsqlStore psqlStore = new PsqlStore(properties)) {
            List<Post> result = parse.list("https://career.habr.com/vacancies?page=1&q=Java%20developer&type=all");
            for (Post post : result) {
                psqlStore.save(post);
            }
            System.out.println(psqlStore.findById(15));
            System.out.println(psqlStore.getAll());
        }
    }
}
