package wishlistgr6.wishlist.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Wish {
    private String name;
    private String description;
    private String productURL;
    private String comments;
    private double price;
    private List<Event> events = new ArrayList<>();
    private boolean isReserved;
    private Image image;

    public Wish(String name, String description, String productURL, String comments, double price, List<Event> events, boolean isReserved) {
        this.name = name;
        this.description = description;
        this.productURL = productURL;
        this.comments = comments;
        this.price = price;
        this.events = events;
        this.isReserved = isReserved;
    }

    public Wish(){};
    public Wish(String name, String description, String productURL, String comments, double price, boolean isReserved) {
        this.name = name;
        this.description = description;
        this.productURL = productURL;
        this.comments = comments;
        this.price = price;
        this.isReserved = isReserved;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public List<Event> getEvents() {return events;}

    public void setEvents(List<Event> events) {this.events = events;}

    public void addEvent(Event event) {events.add(event);}

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Double.compare(getPrice(), wish.getPrice()) == 0 && isReserved() == wish.isReserved() && Objects.equals(getName(), wish.getName()) && Objects.equals(getDescription(), wish.getDescription()) && Objects.equals(getProductURL(), wish.getProductURL()) && Objects.equals(getComments(), wish.getComments()) && Objects.equals(getEvents(), wish.getEvents()) && Objects.equals(getImage(), wish.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getProductURL(), getComments(), getPrice(), getEvents(), isReserved(), getImage());
    }
}


