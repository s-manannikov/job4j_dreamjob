package ru.job4j.store;

import ru.job4j.model.Candidate;
import ru.job4j.model.Post;

public class PsqlMain {
    public static void main(String[] args) {
        Store store = PsqlStore.instOf();
        store.savePost(new Post(0, "Java Job"));
        store.saveCandidate(new Candidate(0, "Candidate"));
        for (Post post : store.findAllPosts()) {
            System.out.println(post.getId() + " " + post.getName());
        }
        for (Candidate candidate : store.findAllCandidates()) {
            System.out.println(candidate.getId() + " " + candidate.getName());
        }
        store.savePost(new Post(1, "Java Job1"));
        store.saveCandidate(new Candidate(1, "Candidate1"));
        System.out.println(store.findPostById(1));
        System.out.println(store.findCandidateById(1));
    }
}