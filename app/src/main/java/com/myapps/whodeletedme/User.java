package com.myapps.whodeletedme;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by user on 03.06.15.
 */
@ParseClassName("_User")
public class User extends ParseUser {

    public List<FBFriend> getCurrentFriends() {
        return getList("current_friends");
    }

    public void setCurrentFriends(List<FBFriend> pictures) {
        put("current_friends", pictures);
    }

    public void addFriend(FBFriend friend) {
        getCurrentFriends().add(friend);
    }

    public List<FBFriend> getDeletedMeFriends() {
        return getList("deleted_me_friends");
    }

    public void setDeletedMeFriends(List<FBFriend> pictures) {
        put("deleted_me_friends", pictures);
    }

    public List<FBFriend> getDeletedByMeFriends() {
        return getList("deleted_by_me_friends");
    }

    public void setDeletedByMeFriends(List<FBFriend> pictures) {
        put("deleted_by_me_friends", pictures);
    }

    public List<FBFriend> getNewFriends() {
        return getList("new_friends");
    }

    public void setNewFriends(List<FBFriend> pictures) {
        put("new_friends", pictures);
    }



    public String getGender() {
        return getString("gender");
    }

    public void setGender(String gender) {
        put("gender",gender);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name",name);
    }

    public String getProfilePicture() {
        return getString("profile_picture");
    }

    public void setProfilePicture(String name) {
        put("profile_picture",name);
    }

    public String getBirthday() {
        return getString("birthday");
    }

    public void setBirthday(String birthday) {
        put("birthday",birthday);
    }
}
