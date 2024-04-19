package com.pellegrinoprincipe;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TimerServlet", urlPatterns = {"/timer"},
            initParams = { @WebInitParam(name = "develop", value = "true")})
public class TimerServlet extends HttpServlet
{
    protected void doRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");

        String devel_txt = "Ok Servlet pronta...";
        String prod_txt = "Ok Servlet in produzione...";

        try (PrintWriter out = response.getWriter())
        {
            boolean is_develop = Boolean.valueOf(getInitParameter("develop"));

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ::TimerServlet::</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + (is_develop ? devel_txt : prod_txt) + "</h1>");
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
        return "TimerServlet By Pellegrino ~thp~ Principe V 1.0";
    }
}
