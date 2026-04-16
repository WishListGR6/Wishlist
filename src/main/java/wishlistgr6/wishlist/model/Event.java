package wishlistgr6.wishlist.model;

import java.util.Date;
import java.util.Objects;

public record Event(String title, Date date) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(date(), event.date()) && Objects.equals(title(), event.title());
    }

    @Override
    public int hashCode() {
        return Objects.hash(title(), date());
    }
}
