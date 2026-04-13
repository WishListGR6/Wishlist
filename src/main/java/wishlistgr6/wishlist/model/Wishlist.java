package wishlistgr6.wishlist.model;

import java.util.ArrayList;
import java.util.List;

public class Wishlist {
    private String name;
    private List<Wish> wishes = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public Wishlist(String name, List<Wish> wishes, List<Event> events) {
        this.name = name;
        this.wishes = wishes;
        this.events = events;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Wish> getWishes() {
        return wishes;
    }

    public void setWishes(List<Wish> wishes) {
        this.wishes = wishes;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
