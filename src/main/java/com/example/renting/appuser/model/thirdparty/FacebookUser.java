package com.example.renting.appuser.model.thirdparty;

public class FacebookUser extends ThirdPartyUser {

    public static FacebookUser of(String name, String email) {

        FacebookUser facebookUser = new FacebookUser();
        facebookUser.name = name;
        facebookUser.email = email;
        return facebookUser;
    }

    public static FacebookUser of(String email) {

        FacebookUser facebookUser = new FacebookUser();
        facebookUser.email = email;
        return facebookUser;
    }
}
