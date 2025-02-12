package su.intercraft.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import su.intercraft.model.Player;
import su.intercraft.repository.PlayerRepository;
import su.intercraft.service.PlayerService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("jsp/auth.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PlayerService playerService = new PlayerService();

        String nickname = request.getParameter("nickname");
        String password = request.getParameter("password");

        try {
            Player player = playerService.authPlayer(nickname, password);
            HttpSession session = request.getSession();
            session.setAttribute("player", player);
            response.sendRedirect("/lk");
        } catch (IllegalArgumentException | SQLException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}