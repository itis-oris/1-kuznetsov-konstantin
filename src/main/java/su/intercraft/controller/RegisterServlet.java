package su.intercraft.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;
import su.intercraft.model.Player;
import su.intercraft.model.PlayerSkin;
import su.intercraft.repository.PlayerRepository;
import su.intercraft.repository.PlayerSkinRepository;
import su.intercraft.service.PlayerService;
import su.intercraft.service.PlayerSkinService;

import java.io.IOException;
import java.util.Map;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(RegisterServlet.class);
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("jsp/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PlayerService playerService = new PlayerService();
        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");
        String password2 = request.getParameter("password2");

        Map<String, String> errors = playerService.registerPlayer(nickname, password, password2);

        if (errors.isEmpty()) {
            response.sendRedirect("/auth");
        } else {
            errors.forEach(request::setAttribute);
            doGet(request, response);
        }
    }
}