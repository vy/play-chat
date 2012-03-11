package controllers;

import com.google.gson.reflect.TypeToken;
import models.Room;
import play.Logger;
import play.libs.F.IndexedEvent;
import play.mvc.Controller;
import play.mvc.With;

import java.util.List;

@With(Secure.class)
public class Chat extends Controller {
    public static void index() {
        renderArgs.put("username", Security.getSessionUser().name);
        render();
    }

    public static void join() {
        Room.getInstance().publish(
                new Room.JoinEvent(Security.getSessionUser()));
    }

    public static void leave() {
        Room.getInstance().publish(
                new Room.LeaveEvent(Security.getSessionUser()));
    }

    public static void say(String text) {
        Room.getInstance().publish(
                new Room.MessageEvent(Security.getSessionUser(), text));
    }

    public static void waitMessages(long lastReceived) {
        renderJSON(
                // Here we use continuation to suspend the execution until a
                // new message arrives.
                await(Room.getInstance().nextMessages(lastReceived)),
                new TypeToken<List<IndexedEvent<Room.Event>>>() {}.getType());
    }
}
