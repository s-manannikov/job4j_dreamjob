package ru.job4j.store;

import ru.job4j.model.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MemStore implements Store {
    private static final MemStore INST = new MemStore();
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private final static AtomicInteger POST_ID = new AtomicInteger(4);
    private final static AtomicInteger CANDIDATE_ID = new AtomicInteger(4);
    private final static AtomicInteger USER_ID = new AtomicInteger();

    private MemStore() {
        posts.put(1, new Post(1, "Junior Java Job", "junior description"));
        posts.put(2, new Post(2, "Middle Java Job", "middle description"));
        posts.put(3, new Post(3, "Senior Java Job", "senior description"));
        candidates.put(1, new Candidate(1, "Junior Java"));
        candidates.put(2, new Candidate(2, "Middle Java"));
        candidates.put(3, new Candidate(3, "Senior Java"));
    }

    public static MemStore instOf() {
        return INST;
    }

    public Collection<Post> findAllPosts() {
        return posts.values();
    }

    public Collection<Candidate> findAllCandidates() {
        return candidates.values();
    }

    public void savePost(Post post) {
        if (post.getId() == 0) {
            post.setId(POST_ID.incrementAndGet());
        }
        posts.put(post.getId(), post);
    }

    public Post findPostById(int id) {
        return posts.get(id);
    }

    public void saveCandidate(Candidate candidate) {
        if (candidate.getId() == 0) {
            candidate.setId(CANDIDATE_ID.incrementAndGet());
        }
        candidates.put(candidate.getId(), candidate);
    }

    public Candidate findCandidateById(int id) {
        return candidates.get(id);
    }

    public void deleteCandidate(int id) {
        if (findCandidateById(id) != null) {
            candidates.remove(id);
        }
    }

    public void deletePost(int id) {
        if (findPostById(id) != null) {
            posts.remove(id);
        }
    }

    @Override
    public void saveUser(User user) {
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public void deleteUser(int id) {
    }

    @Override
    public List<City> findAllCities() {
        return null;
    }

    @Override
    public City findCityById(int id) {
        return null;
    }

    @Override
    public Photo savePhoto(Photo photo) {
        return null;
    }

    @Override
    public Photo findPhotoById(int id) {
        return null;
    }

    @Override
    public void deletePhoto(int id) {

    }
}