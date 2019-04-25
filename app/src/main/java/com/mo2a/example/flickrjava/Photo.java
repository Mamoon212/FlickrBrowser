package com.mo2a.example.flickrjava;

import java.io.Serializable;

class Photo implements Serializable {
    private static final long SerialVersionUID= 1L;

    private String title;
    private String author;
    private String authorId;
    private String link;
    private String tags;
    private String image;

    public Photo(String title, String author, String authorId, String link, String tags, String image) {
        this.title = title;
        this.author = author;
        this.authorId = authorId;
        this.link = link;
        this.tags = tags;
        this.image = image;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    String getAuthorId() {
        return authorId;
    }

    void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    String getLink() {
        return link;
    }

    void setLink(String link) {
        this.link = link;
    }

    String getTags() {
        return tags;
    }

    void setTags(String tags) {
        this.tags = tags;
    }

    String getImage() {
        return image;
    }

    void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", authorId='" + authorId + '\'' +
                ", link='" + link + '\'' +
                ", tags='" + tags + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
