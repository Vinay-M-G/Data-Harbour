package com.dataHarbour.engine.Service.impl;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionService {

    public Object getAttribute(final String key){
        HttpSession session = getCurrentSession();
        return session.getAttribute(key);
    }

    public void setAttributeInSession(final String key, final Object value){
        HttpSession session = getCurrentSession();
        session.setAttribute(key, value);
    }

    private HttpSession getCurrentSession(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getSession(true);
    }
}
