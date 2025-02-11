package su.intercraft.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import su.intercraft.exception.SkinUploadException;
import su.intercraft.model.Payment;
import su.intercraft.model.Player;
import su.intercraft.model.PlayerSkin;
import su.intercraft.repository.DataSourceConfig;
import su.intercraft.repository.PlayerRepository;
import su.intercraft.service.PaymentService;
import su.intercraft.service.PlayerService;
import su.intercraft.service.PlayerSkinService;
import su.intercraft.service.SkinService;
import su.intercraft.model.Skin;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@WebServlet("/lk")
@MultipartConfig
public class DashboardServlet extends HttpServlet {
    final static Logger logger = LogManager.getLogger(DashboardServlet.class);

    private void setCostOfPassage(HttpServletRequest request) {
        request.setAttribute("costOfPassage", "100");
    }

    private void doJsp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("jsp/lk.jsp").forward(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("tabSkins", "show active");
        setCostOfPassage(request);
        doJsp(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCostOfPassage(request);

        if (request.getParameter("action") != null) {
            actionWithSkins(request);
            request.setAttribute("tabSkins", "show active");
        } else if (request.getParameter("change_skin") != null) {
            changeSkin(request);
            request.setAttribute("tabSkins", "show active");
        } else if (request.getParameter("do_change_password") != null) {
            changePassword(request);
            request.setAttribute("tabSettings", "show active");
        } else if (request.getParameter("top_up_balance") != null) {
            addPayment(request);
            request.setAttribute("tabPayments", "show active");
        } else if (request.getParameter("buy_passage") != null) {
            extendPlayerExpirationDate(request);
            request.setAttribute("tabPayments", "show active");
        }
        doJsp(request, response);
    }

    public void actionWithSkins(HttpServletRequest request) {
        Player player = (Player)request.getAttribute("player");
        PlayerSkinService playerSkinService = new PlayerSkinService();
        String action = request.getParameter("action");
        try {
            int skinId = Integer.parseInt(request.getParameter("id"));
            switch (action) {
                case "activate":
                    playerSkinService.reactivateSkin(skinId, player.getPlayerId());
                    break;
                case "delete":
                    if (player.getAllSkins().size() > 1) {
                        playerSkinService.deletePlayerSkin(player.getPlayerId(), skinId);
                    }
                    break;
            }
        } catch (SQLException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
            logger.warn("ERROR", e);
        }
    }
    public void changeSkin(HttpServletRequest request) {
        Player player = (Player)request.getAttribute("player");
        PlayerSkinService playerSkinService = new PlayerSkinService();
        try {
            Part filePart = request.getPart("skin_file");
            playerSkinService.createSkinToPlayer(filePart, player.getPlayerId());
        } catch (SQLException | IOException | ServletException e) {
            request.setAttribute("error", e.getMessage());
            logger.warn("ERROR", e);
        } catch (SkinUploadException e) {
            request.setAttribute("error", e.getMessage());
        }
    }
    public void changePassword(HttpServletRequest request) {
        Player player = (Player)request.getAttribute("player");
        PlayerService playerService = new PlayerService();

        Map<String, String> errors = playerService.changePassword(player,
                request.getParameter("oldPassword"),
                request.getParameter("password"),
                request.getParameter("password2")
        );
        if (!errors.isEmpty()) {
            errors.forEach(request::setAttribute);
        } else {
            request.setAttribute("success", "Успешно!");
        }
    }
    public void addPayment(HttpServletRequest request) {
        try {
            Player player = (Player)request.getAttribute("player");
            PaymentService paymentService = new PaymentService();

            BigDecimal amount = new BigDecimal(Integer.parseInt(request.getParameter("amount")));
            paymentService.addPayment(amount, player.getPlayerId());
        } catch (SQLException | NumberFormatException e) {
            request.setAttribute("error", e.getMessage());
        }
    }

    public void extendPlayerExpirationDate(HttpServletRequest request) {
        try {
            Player player = (Player)request.getAttribute("player");
            PlayerService playerService = new PlayerService();
            PaymentService paymentService = new PaymentService();

            int passage = Integer.parseInt((String)request.getAttribute("costOfPassage"));
            double balance = player.getDoubleBalance();
            if (balance < passage) {
                throw  new IllegalArgumentException("Недостаточно денег на балансе");
            }
            paymentService.addPayment(new BigDecimal(-passage), player.getPlayerId());
            playerService.extendPlayerExpirationDate(player, 7);
        } catch (SQLException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        }

    }
}