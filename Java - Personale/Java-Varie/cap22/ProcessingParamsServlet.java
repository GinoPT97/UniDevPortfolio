package com.pellegrinoprincipe;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ProcessingParamsServlet", urlPatterns = {"/params"})
public class ProcessingParamsServlet extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        // ottenimento dei parametri
        String nome = request.getParameter("nome") != null ? request.getParameter("nome") : "n.d";
        String cognome = request.getParameter("cognome") != null ? request.getParameter("cognome") : "n.d";
        String sesso = request.getParameter("sesso") != null ? request.getParameter("sesso") : "n.d";
        String linguaggi[] = request.getParameterValues("linguaggi") != null ? request.getParameterValues("linguaggi") : new String[]{"n.d"};
        String sistemi = request.getParameter("sistemi") != null ? request.getParameter("sistemi") : "n.d";

        try (PrintWriter out = response.getWriter())
        {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>:: Dati ricevuti ::</title>");
            out.println("</head>");
            out.println("<body>");

            // tabella di formattazione
            StringBuilder datas = new StringBuilder("<table border=\"1\">");
            datas.append("<tr><td>Nome: </td><td>&nbsp;" + nome + "</td></tr>");
            datas.append("<tr><td>Cognome: </td><td>&nbsp;" + cognome + "</td></tr>");
            datas.append("<tr><td>Sesso: </td><td>&nbsp;" + sesso + "</td></tr>");
            datas.append("<tr><td>Linguaggi di programmazione: </td><td>&nbsp;");
            for (int i = 0; i < linguaggi.length; i++)
            {
                datas.append(linguaggi[i] + (i < linguaggi.length-1 ? ", " : "") );            
            }
            datas.append("</td></tr><tr><td>Sistema operativo: </td><td>&nbsp;" + sistemi + "</td></tr>");
            datas.append("</table>");
            
            out.println(datas.toString());

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
        return "ProcessingParamsServlets By Pellegrino ~thp~ Principe V 1.0";
    }
}
