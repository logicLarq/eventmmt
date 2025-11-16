package CollegeEventSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String date;
    private String venue;
    private int maxParticipants;
    private List<Participant> participants;

    public Event(int id, String name, String date, String venue, int maxParticipants) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.venue = venue;
        this.maxParticipants = maxParticipants;
        this.participants = new ArrayList<>();
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public int getMaxParticipants() { return maxParticipants; }
    public List<Participant> getParticipants() { return participants; }

    public boolean addParticipant(Participant p) {
        if (participants.size() >= maxParticipants) return false;
        for (Participant existing : participants)
            if (existing.getRollNo().equalsIgnoreCase(p.getRollNo())) return false;
        participants.add(p);
        return true;
    }

    public boolean removeParticipant(String rollNo) {
        return participants.removeIf(p -> p.getRollNo().equalsIgnoreCase(rollNo));
    }

    public int getRegisteredCount() { return participants.size(); }

    @Override
    public String toString() {
        return String.format("%d - %s (%s) [%d/%d]", id, name, date, getRegisteredCount(), maxParticipants);
    }
}
   

