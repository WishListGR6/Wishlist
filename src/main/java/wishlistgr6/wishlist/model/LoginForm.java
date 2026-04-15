package wishlistgr6.wishlist.model;

public class LoginForm {
    public String getLoginURL() {
        return loginURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    private String loginURL;

    public LoginForm(String loginURL) {
        this.loginURL = loginURL;
    }
}
