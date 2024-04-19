package com.pellegrinoprincipe;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "PersistingDataServlet", urlPatterns = {"/persistingdata"})
public class PersistingDataServlet extends HttpServlet
{
    private HttpSession session;
    private ServletContext context;

    protected void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        // ottenimento dei parametri
        String colore_back = request.getParameter("colore_back") != null ? request.getParameter("colore_back") : "black";
        String colore_front = request.getParameter("colore_front") != null ? request.getParameter("colore_front") : "white";

        // utilizzo delle sessioni per memorizzare i colori
        session = request.getSession();
        session.setAttribute("colore_back", colore_back);
        session.setAttribute("colore_front", colore_front);

        // utilizzo del contesto per memorizzare quanti utenti sono collegati
        context = request.getServletContext();
        Integer users = (Integer) context.getAttribute("users");
        context.setAttribute("users", users == null ? 1 : ++users);
        
        // ridirigo la risposta ad un'altra servlet
        response.sendRedirect("managedata");        
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        doRequest(request, response);
    }

    public String getServletInfo()
    {
        return "PersistingDataServlet By Pellegrino ~thp~ Principe V 1.0";
    }
}
