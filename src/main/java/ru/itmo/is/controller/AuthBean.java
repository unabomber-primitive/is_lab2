package ru.itmo.is.controller;

import ru.itmo.is.entity.User;
import ru.itmo.is.service.UserService;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class AuthBean implements Serializable {
    @Inject
    private UserService userService;

    private String username;
    private String password;
    private User currentUser;

    public String login() {
        currentUser = userService.authenticate(username, password);
        if (currentUser != null) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedIn", true);
            return "index?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", null));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        return currentUser != null && "ADMIN".equals(currentUser.getRole());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
