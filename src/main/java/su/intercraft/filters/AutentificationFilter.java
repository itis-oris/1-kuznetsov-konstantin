package su.intercraft.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AutentificationFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if (httpServletRequest.getServletPath().startsWith("/assets/")) {
            filterChain.doFilter(request, response);
        } else {
            HttpSession session = httpServletRequest.getSession(false);
            long currentTime = System.currentTimeMillis();
            long oneHourInMillis = 60 * 60 * 1000; // 1 час

            if (session != null && session.getAttribute("player") != null &&
                    currentTime - session.getCreationTime() < oneHourInMillis) {

                request.setAttribute("player", session.getAttribute("player"));

                if (httpServletRequest.getServletPath().startsWith("/auth") ||
                        httpServletRequest.getServletPath().startsWith("/register")) {
                    httpServletResponse.sendRedirect("/lk");
                    return;
                }
                filterChain.doFilter(request, response);
            } else {
                if (session != null) {
                    session.invalidate();
                }
                if (httpServletRequest.getServletPath().startsWith("/lk")) {
                    httpServletResponse.sendRedirect("/auth");
                    return;
                }
                filterChain.doFilter(request, response);
            }
        }
    }
}