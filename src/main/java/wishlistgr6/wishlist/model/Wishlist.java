package wishlistgr6.wishlist.model;

import wishlistgr6.wishlist.exceptions.EventsAlreadyExistException;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wishlist {
    private String name;
    private List<Wish> wishes = new ArrayList<>();
    private List<Event> events = new ArrayList<>();

    public Wishlist(String name, List<Wish> wishes, List<Event> events) {
        this.name = name;
        this.wishes = wishes;
        this.events = events;
    }
    public Wishlist(){}


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

    public void addNoEvent() throws EventsAlreadyExistException {
        if(!events.isEmpty()){throw new EventsAlreadyExistException("Events already exists, hence addNoEvent should not be invoked");}
        events.add(new Event("No event", Date.valueOf("2050-01-01")));
    }

    public void addWish(Wish wish) {
        this.wishes.add(wish);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wishlist wishlist = (Wishlist) o;
        return Objects.equals(getName(), wishlist.getName()) && Objects.equals(getWishes(), wishlist.getWishes()) && Objects.equals(getEvents(), wishlist.getEvents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getWishes(), getEvents());
    }
}
