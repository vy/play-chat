package controllers;

import models.User;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import play.libs.OAuth2;

public class Security extends Secure.Security {
    private static OAuth2 FBOAuth = new OAuth2(
            "https://graph.facebook.com/oauth/authorize",
            "https://graph.facebook.com/oauth/access_token",
            "170146139766604",
            "e6bf6165527ec9e30a7d2aa380e5c23e"
    );

    @SuppressWarnings("unused")
    public static void onAuth() {
        if (OAuth2.isCodeResponse()) {
            OAuth2.Response response = FBOAuth.retrieveAccessToken(onAuthURL());
            FacebookClient fbClient = new DefaultFacebookClient(response.accessToken);
            com.restfb.types.User fbUser = fbClient.fetchObject("me", com.restfb.types.User.class);
            User user = User.findByFBId(fbUser.getId());
            if (user == null)
                user = new User(fbUser.getId(), fbUser.getName()).save();
            session.put("username", user.name);  // Required by Secure.
            session.put("fbid", user.fbid);
            Application.index();
        }
    }

    private static String onAuthURL() {
        return play.mvc.Router.getFullUrl("Security.onAuth");
    }

    public static void auth() {
        FBOAuth.retrieveVerificationCode(onAuthURL());
    }

    @SuppressWarnings("unused")
    static void onDisconnected() {
        flash.success("You have been logged out successfully.");
        Application.index();
    }

    static User getSessionUser() {
        return User.findByFBId(session.get("fbid"));
    }
}
