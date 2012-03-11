package models;

import play.libs.F.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Room {
    private static Room instance = null;
    public static final int EVENT_STREAM_SIZE = 100;
    private final ArchivedEventStream<Event> eventStream =
            new ArchivedEventStream<Event>(EVENT_STREAM_SIZE);

    public void publish(Event event) {
        eventStream.publish(event);
    }

    public Promise<List<IndexedEvent<Event>>> nextMessages(long lastReceived) {
        return eventStream.nextEvents(lastReceived);
    }

    public static abstract class Event {
        public final String date;
        public final String user;
        public final Type type;

        public final static DateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public enum Type {
            JOIN,
            LEAVE,
            MESSAGE
        }

        public Event(User user, Type type) {
            this.date = dateFormat.format(new Date());
            this.user = user.name;
            this.type = type;
        }
    }

    public static class JoinEvent extends Event {
        public JoinEvent(User user) {
            super(user, Type.JOIN);
        }
    }

    public static class LeaveEvent extends Event {
        public LeaveEvent(User user) {
            super(user, Type.LEAVE);
        }
    }

    public static class MessageEvent extends Event {
        public final String text;

        public MessageEvent(User user, String text) {
            super(user, Type.MESSAGE);
            this.text = text;
        }
    }

    public static Room getInstance() {
        if (instance == null)
            instance = new Room();
        return instance;
    }
}
