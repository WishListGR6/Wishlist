package wishlistgr6.wishlist.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Wish {
    private String name;
    private String description;
    private String productURL;
    private String comments;
    private double price;
    private List<Event> events = new ArrayList<>();
    private Image image;

    public Wish(String name, String description, String productURL, String comments, double price, List<Event> events) {
        this.name = name;
        this.description = description;
        this.productURL = productURL;
        this.comments = comments;
        this.price = price;
        this.events = events;
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
}


