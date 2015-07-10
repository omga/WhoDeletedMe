package com.myapps.whodeletedme;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by user on 10.07.15.
 */
@ParseClassName("FBFriend")
public class FBFriend extends ParseObject {

    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        put("id",id);
    }


    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getProfilePicture() {
        return getString("picture");
    }

    public void setProfilePicture(String name) {
        put("picture",name);
    }
}
