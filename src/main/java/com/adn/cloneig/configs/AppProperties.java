package com.adn.cloneig.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConfigurationProperties(prefix = "app.baseurl")
public class AppProperties {
    @Value("${app.baseurl.storage}")
    private String BASE_URL_STORAGE;

    private String uploadStorage = "uploads/";
    private String profileStorage = "profile/";
    private String storyStorage = "story/";
    private String imgProfileDefault = "img/default.png";
    private String noImage = "img/noimage.png";

    public String getUploadStorage() {
        return BASE_URL_STORAGE + uploadStorage;
    }
    public String getProfileStorage() {
        return BASE_URL_STORAGE + profileStorage;
    }
    public String getStoryStorage() {
        return BASE_URL_STORAGE + storyStorage;
    }
    public String getImgProfileDefault() {
        return BASE_URL_STORAGE + imgProfileDefault;
    }
    public String getNoImage() {
        return BASE_URL_STORAGE + noImage;
    }

    
}
