package wishlistgr6.wishlist.model;

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
