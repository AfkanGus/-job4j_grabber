package ru.job4j.utils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 2.2. Модель данных - Post. [#285211].
 */
public class Post {
    public int id;
    public String title;
    public String description;
    public String link;
    public LocalDateTime created;

    public Post() {

    }

    public Post(int id, String title, String description, String link, LocalDateTime created) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.created = created;
    }

    public Post(String title, String description, String link, LocalDateTime created) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.created = created;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Post{"
                +
                "id=" + id
                +
                ", title='" + title + '\''
                + ", description='" + description + '\''
                +
                ", link='" + link + '\''
                +
                ", created=" + created + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return id == post.id && Objects.equals(link, post.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
