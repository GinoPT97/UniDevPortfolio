package com.pellegrinoprincipe;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.util.Arrays;

@WebFilter(filterName = "TimerFilter", urlPatterns = {"/timer"})
public class TimerFilter implements Filter
{
    private FilterConfig fc;
    private String valid_ip[] = {"192.168.0.66", "192.168.0.22", "192.168.0.11"};

    public void init(FilterConfig filterConfig) throws ServletException
    {
        fc = filterConfig;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        PrintWriter pw = response.getWriter();

        // controlla se la servlet è invocata da un IP valido e solo in quel caso procedi con la sua esecuzione.
        // Permetti l'accesso se si è in localhost!!!
        String ip = request.getRemoteAddr();

        if (Arrays.asList(valid_ip).contains(ip) || ip.equals(request.getLocalAddr()))
        {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sd_f = new SimpleDateFormat();
            pw.println("Richiesta inoltrata in data: " + sd_f.format(cal.getTime()));           
            chain.doFilter(request, response); // propaga alla servlet
        }
        else
            pw.println("Spiacenti non si è autorizzati ad eseguire l'applicazione dall'IP: " + ip);
    }

    public void destroy() { fc = null; }
}