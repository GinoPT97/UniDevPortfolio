package com.pellegrinoprincipe;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "ManagingDataServlet", urlPatterns = {"/managedata"})
public class ManagingDataServlet extends HttpServlet
{
    private HttpSession session;
    private ServletContext context;

    protected void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        // ottenimento dalla sessione per sapere i colori scelti
        session = request.getSession();
        String colore_back = (String) session.getAttribute("colore_back");
        String colore_front = (String) session.getAttribute("colore_front");

        // ottenimento dal contesto per sapere il numero di utenti connessi
        context = request.getServletContext();
        Integer users = (Integer) context.getAttribute("users");

        // formattazione della risposta
        try (PrintWriter out = response.getWriter())
        {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>:: Personal Web Page ::</title>");
            out.println("</head>");
            out.println("<body style=\"background-color:" + colore_back + "\">");
            out.print("<h1 style=\"color:" + colore_front + "\">Benvenuto nella tua personal home page");
            out.println(" [utenti online: " + users + "]</h1>");
            out.println("</body>");
            out.println("</html>");
        }

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
        return "ManagingDataServlet By Pellegrino ~thp~ Principe V 1.0";
    }
}
