package com.codepath.apps.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akulka2 on 10/24/15.
 */
public class User {

    private Long uid;
    private String userName;
    private String profileImageURL;

    public Long getUid() {
        return uid;
    }

    public String getUserName() {
        return userName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public static User fromJSON(JSONObject jsonObject){
        User user = new User();
        try {
            user.uid = jsonObject.getLong("id");
            user.profileImageURL = jsonObject.getString("profile_image_url");
            user.userName = jsonObject.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }
}
