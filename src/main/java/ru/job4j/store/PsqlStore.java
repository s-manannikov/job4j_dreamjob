package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private static final Logger LOG = LoggerFactory.getLogger(PsqlStore.class.getName());

    private PsqlStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(new FileReader("db.properties"))) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Post> findAllPosts() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime()
                    ));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return posts;
    }

    @Override
    public Collection<Candidate> findAllCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(
                            new Candidate(
                                    it.getInt("id"),
                                    it.getString("name"),
                                    it.getInt("photoId"),
                                    it.getInt("cityId")
                            )
                    );
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return candidates;
    }

    @Override
    public void savePost(Post post) {
        if (post.getId() == 0) {
            createPost(post);
        } else {
            updatePost(post);
        }
    }

    @Override
    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            createCandidate(candidate);
        } else {
            updateCandidate(candidate);
        }
    }

    private void createPost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "insert into post(name, description, created) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void createCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "insert into candidate(name, photoId, cityId) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updatePost(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update post set name = ?, description = ?, created = ? where id = ?")) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
            ps.setInt(4, post.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updateCandidate(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update candidate set name = ?, photoid = ?, cityid = ? where id = ?")) {
            ps.setString(1, candidate.getName());
            ps.setInt(2, candidate.getPhotoId());
            ps.setInt(3, candidate.getCityId());
            ps.setInt(4, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public Post findPostById(int id) {
        Post post = new Post();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "select * from post where id = ?"
             )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getInt("id"));
                    post.setName(resultSet.getString("name"));
                    post.setDescription(resultSet.getString("description"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Post not found", e);
        }
        return post.getId() != 0 ? post : null;
    }

    public Candidate findCandidateById(int id) {
        Candidate candidate = new Candidate();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "select * from candidate where id = ?"
             )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    candidate.setId(resultSet.getInt("id"));
                    candidate.setName(resultSet.getString("name"));
                    candidate.setPhotoId(resultSet.getInt("photoId"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Candidate not found", e);
        }
        return candidate.getId() != 0 ? candidate : null;
    }

    public void deleteCandidate(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "delete from candidate where id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void deletePost(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "delete from post where id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void saveUser(User user) {
        if (user.getId() == 0) {
            createUser(user);
        } else {
            updateUser(user);
        }
    }

    private void createUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "insert into users(name, email, password) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void updateUser(User user) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update users set name = ?, email = ?, password = ? where id = ?")) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setInt(4, user.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public User findUserByEmail(String email) {
        User user = new User();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "select * from users where email = ?"
             )) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("User not found", e);
        }
        return user.getId() != 0 ? user : null;
    }

    public void deleteUser(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "delete from users where id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public Collection<City> findAllCities() {
        List<City> cities = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from city")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    cities.add(new City(it.getInt("id"), it.getString("name")));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return cities;
    }

    public City findCityById(int id) {
        City city = new City();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "select * from city where id = ?"
             )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    city.setId(resultSet.getInt("id"));
                    city.setName(resultSet.getString("name"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("City not found", e);
        }
        return city.getId() != 0 ? city : null;
    }

    public Photo savePhoto(Photo photo) {
        return photo.getId() == 0 ? createPhoto(photo) : updatePhoto(photo);
    }

    public Photo findPhotoById(int id) {
        Photo photo = new Photo();
        try (Connection cn = pool.getConnection();
             PreparedStatement statement = cn.prepareStatement(
                     "select * from photo where id = ?"
             )) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    photo.setId(resultSet.getInt("id"));
                    photo.setPicture(resultSet.getBytes("picture"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Photo not found", e);
        }
        return photo.getId() != 0 ? photo : null;
    }

    public void deletePhoto(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "delete from photo where id = ?")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private Photo createPhoto(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "insert into photo(picture) values (?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setBytes(1, photo.getPicture());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    photo.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return photo;
    }

    private Photo updatePhoto(Photo photo) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "update photo set picture = ? where id = ?")) {
            ps.setBytes(1, photo.getPicture());
            ps.setInt(2, photo.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return photo;
    }
}