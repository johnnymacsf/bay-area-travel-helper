package com.hotelapp.backend.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    public static boolean isUserLoggedIn(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SessionContents.USERNAME) != null;
    }

    public static String getLoggedInUsername(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session != null){
            return (String) session.getAttribute(SessionContents.USERNAME);
        }
        return null;
    }
}
