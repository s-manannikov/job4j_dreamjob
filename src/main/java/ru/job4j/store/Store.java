package ru.job4j.store;

import ru.job4j.model.Candidate;
import ru.job4j.model.City;
import ru.job4j.model.Post;
import ru.job4j.model.User;

import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    void savePost(Post post);

    Post findPostById(int id);

    void deletePost(int id);

    Collection<Candidate> findAllCandidates();

    void saveCandidate(Candidate candidate);

    Candidate findCandidateById(int id);

    void deleteCandidate(int id);

    void saveUser(User user);

    User findUserByEmail(String email);

    void deleteUser(int id);

    Collection<City> findAllCities();

    City findCityById(int id);
}