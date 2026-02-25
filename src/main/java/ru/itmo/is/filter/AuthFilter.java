package ru.itmo.is.filter;

import ru.itmo.is.controller.AuthBean;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"*.xhtml"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String requestURI = req.getRequestURI();

        if (requestURI.contains("login.xhtml") ||
            requestURI.contains("javax.faces.resource")) {
            chain.doFilter(request, response);
            return;
        }

        boolean loggedIn = (session != null && Boolean.TRUE.equals(session.getAttribute("loggedIn")));

        if (!loggedIn) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}
