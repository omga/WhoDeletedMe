package com.myapps.whodeletedme;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FacebookDataFetcher {

    private static String TAG = "FacebookDataFetcher";
    private static String PICTURES_PATH = "/photos";
    private Context mContext;
    public FacebookDataFetcher(Context context) {
        mContext = context;
    }

    public void getUserInfo (final User user) {
        GraphRequest graphRequest = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        Log.d(TAG, jsonObject.toString());
                        try {
                            user.setName(jsonObject.getString("first_name"));
                            user.setGender(jsonObject.getString("gender"));
                            user.setBirthday(jsonObject.getString("birthday"));
                            user.setEmail(jsonObject.getString("email"));
                            String profilePic = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                            user.setProfilePicture(profilePic);
                            user.saveInBackground();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","birthday,first_name,last_name,gender,picture,id,email");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    public void getUserFriends() {
        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Updating friend list");
        progressDialog.setMessage("Loading..");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/taggable_friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        System.out.println(response);
                        parseFriendsResp(response);
                        startMainActivity();
                        progressDialog.cancel();
                    }
                }
        ).executeAsync();
    }
    private void parseFriendsResp(GraphResponse response) {
        List<FBFriend> friends;
        try {
            JSONArray friendsArray = response.getJSONObject().getJSONArray("data");
            friends = new ArrayList<>(friendsArray.length());
            for (int i = 0; i < friendsArray.length(); i++) {
                FBFriend fbFriend = new FBFriend();
                fbFriend.setName(friendsArray.getJSONObject(i).getString("name"));
                fbFriend.setId(friendsArray.getJSONObject(i).getString("id"));
                fbFriend.setProfilePicture(friendsArray.getJSONObject(i)
                        .getJSONObject("picture")
                        .getJSONObject("data")
                        .getString("url"));
                fbFriend.saveInBackground();
                friends.add(fbFriend);
            }
            User user = (User)ParseUser.getCurrentUser();
            user.setCurrentFriends(friends);
            user.saveInBackground();

        }catch (JSONException e){
            Log.e(TAG,"e: "+ e.getMessage());
        }
    }


    private void startMainActivity() {

        Intent i = new Intent(mContext, MainActivityTabbed.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);

    }


}
