package org.ssssssss.magicapi.hive.model;

import org.ssssssss.magicapi.core.model.MagicEntity;

public class HiveInfo extends MagicEntity{

    private String id;
    private String url;
    private String username;
    private String password;
    private String database;

    public HiveInfo() {
    }

    public HiveInfo(String url, String username, String password, String database) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HiveInfo{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='***'" +
                ", database='" + database + '\'' +
                '}';
    }
}
