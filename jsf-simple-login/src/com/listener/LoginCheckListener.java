/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.listener;

import com.auth.LoginAuth;
import java.util.Map;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Manoj
 */
public class LoginCheckListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
    public void afterPhase(PhaseEvent event) {

        FacesContext context = event.getFacesContext();
        NavigationHandler navHand = context.getApplication().getNavigationHandler();

        if (context.getViewRoot().getViewId().lastIndexOf("login") > -1) {
            return;
        }

        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        if (sessionMap.get("userInfo") != null && ((LoginAuth) sessionMap.get("userInfo")).isLoggedIn()) {
            return;
        }

        navHand.handleNavigation(context, null, "home");
    }

    @Override
    public void beforePhase(PhaseEvent pe) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
