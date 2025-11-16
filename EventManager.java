package CollegeEventSystem;

import java.io.*;
import java.util.*;

public class EventManager implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Event> events = new ArrayList<>();
    private int nextId = 1;

    public Event createEvent(String name, String date, String venue, int maxParticipants) {
        Event e = new Event(nextId++, name, date, venue, maxParticipants);
        events.add(e);
        return e;
    }
    public List<Event> getEvents() { return events; }
    public Event findEventById(int id) {
        for (Event e : events) if (e.getId() == id) return e;
        return null;
    }
    public boolean registerToEvent(int eventId, Participant p) {
        Event e = findEventById(eventId);
        if (e == null) return false;
        return e.addParticipant(p);
    }
    public boolean cancelRegistration(int eventId, String rollNo) {
        Event e = findEventById(eventId);
        if (e == null) return false;
        return e.removeParticipant(rollNo);
    }
    public List<Event> searchByName(String keyword) {
        List<Event> res = new ArrayList<>();
        for (Event e : events) if (e.getName().toLowerCase().contains(keyword.toLowerCase())) res.add(e);
        return res;
    }

    
    public void saveToFile(String filename) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(this);
        }
    }
    public static EventManager loadFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (EventManager) ois.readObject();
        }
    }
}

