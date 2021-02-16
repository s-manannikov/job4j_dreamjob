package ru.job4j.model;

import java.util.Objects;

public class Candidate {
    private int id;
    private String name;
    private String photoId;
    private int cityId;

    public Candidate(int id, String name, String photoId, int cityId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
        this.cityId = cityId;
    }

    public Candidate(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Candidate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Candidate candidate = (Candidate) o;
        return id == candidate.id && cityId == candidate.cityId && Objects.equals(name, candidate.name) && Objects.equals(photoId, candidate.photoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, photoId, cityId);
    }
}