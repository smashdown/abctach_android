package com.abctech.blogtalking.model;

import com.abctech.blogtalking.repository.realm.RealmString;
import com.abctech.blogtalking.repository.realm.RealmStringArrayDeserializer;
import com.abctech.blogtalking.repository.realm.RealmStringArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

public class BTBlogEntry extends RealmObject {
    private String                 id;
    private String                 title;
    private String                 content;
    @JsonDeserialize(using = RealmStringArrayDeserializer.class)
    @JsonSerialize(using = RealmStringArraySerializer.class)
    private RealmList<RealmString> imageUrls;
    private String                 link;
    private String                 authorName;
    private Date                   createdDate;

    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RealmList<RealmString> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(RealmList<RealmString> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}