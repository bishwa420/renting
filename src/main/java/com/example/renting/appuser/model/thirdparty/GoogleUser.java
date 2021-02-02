package com.example.renting.appuser.model.thirdparty;

public class GoogleUser extends ThirdPartyUser {

    public static GoogleUser of(String name, String email) {
        GoogleUser googleUser = new GoogleUser();
        googleUser.name = name;
        googleUser.email = email;
        return googleUser;
    }
}
