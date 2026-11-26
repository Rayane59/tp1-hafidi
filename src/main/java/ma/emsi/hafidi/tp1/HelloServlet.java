package ma.emsi.hafidi.tp1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().println("""
                <!doctype html>
                <html lang="fr">
                <head><meta charset="UTF-8"><title>TP 1 de Hafidi</title></head>
                <body><h1>Bienvenue dans le TP 1 de Rayane Hafidi !</h1></body>
                </html>
                """);
    }
}
