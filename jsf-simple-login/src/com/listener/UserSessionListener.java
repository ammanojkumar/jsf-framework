/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.listener;

import com.auth.LoginAuth;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Manoj
 */
public class UserSessionListener implements HttpSessionListener {

    private int sessionCnt = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        synchronized (this) {
            ++sessionCnt;
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        try {
            HttpSession session = se.getSession();
            String id = session.getId();
            LoginAuth controller = (LoginAuth) session.getAttribute("userInfo");

            String userName = "";
            if (controller != null) {
                userName = controller.getUsername();
            }

            ServletContext context = session.getServletContext();

            Object obj = context.getAttribute("USERS");

            if (obj instanceof Map) {
                Map map = (Map) obj;
                if (map.containsKey(userName)) {
                    map.remove(userName);
                }
            }

            synchronized (this) {
                --sessionCnt;
            }

        } catch (Exception ex) {
        }
    }
}
