package ru.job4j.store;

import ru.job4j.model.Candidate;
import ru.job4j.model.Post;
import ru.job4j.model.User;

import java.util.Collection;

public interface Store {

    Collection<Post> findAllPosts();

    Collection<Candidate> findAllCandidates();

    void savePost(Post post);

    void saveCandidate(Candidate candidate);

    Post findPostById(int id);

    Candidate findCandidateById(int id);

    void deleteCandidate(int id);

    void deletePost(int id);

    void saveUser(User user);

    User findUserByEmail(String email);

    void deleteUser(int id);
}
