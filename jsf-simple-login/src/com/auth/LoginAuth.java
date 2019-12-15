/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.auth;

import java.util.HashMap;
import java.util.Map;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Manoj
 */
@ManagedBean
@SessionScoped
public class LoginAuth {

    private String username;
    private String password;
    private String status;
    private boolean loggedIn;
    private String sessionId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void checkAuth() {
        if (getUsername() == null || getUsername().isEmpty()) {
            setStatus("Username should not be empty!!");
            return;
        }
        if (getPassword() == null || getPassword().isEmpty()) {
            setStatus("Password should not be empty!!");
            return;
        }

        if (getUsername().equals(getPassword())) {
            setStatus("");
        } else {
            setStatus("Authentication failed!!");
            return;
        }

        setLoggedIn(true);
        Map appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        Map<String, HttpSession> userSessionMap = (Map<String, HttpSession>) appMap.get("USERS");

        if (userSessionMap == null) {
            userSessionMap = new HashMap();
            appMap.put("USERS", userSessionMap);

        } else if (userSessionMap.containsKey(getUsername())) {
            HttpSession sess = userSessionMap.get(getUsername());
            try {
                sess.invalidate();
            } catch (Exception ex) {
            }
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
        nh.handleNavigation(facesContext, null, "success");

        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        userSessionMap.put(getUsername(), httpSession);
        setSessionId(httpSession.getId());

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("userInfo", LoginAuth.this);
    }

    public void signout() throws ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        Map<String, Object> sessionMap = externalContext.getSessionMap();
        if (sessionMap.get("userInfo") != null) {
            sessionMap.remove("userInfo");
        }

        NavigationHandler navHand = facesContext.getApplication().getNavigationHandler();
        navHand.handleNavigation(facesContext, null, "home");
        setLoggedIn(false);

        Map<?, ?> appMap = externalContext.getApplicationMap();
        Map<String, HttpSession> userSessionMap = (Map<String, HttpSession>) appMap.get("USERS");

        HttpSession curSes = userSessionMap.get(getUsername());
        userSessionMap.remove(getUsername());
        curSes.invalidate();
    }
}
