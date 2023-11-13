package com.seminfo.utils;

import com.seminfo.api.dto.LoginInputDTO;
import com.seminfo.api.dto.LoginInputGoogleDTO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Log {

    public static void createSimpleLog(LoginInputDTO loginInputDTO, HttpServletRequest request)
    {
        try
        {
            String dataRequest = getDataRequest(loginInputDTO,request);

            File file = new File("log.txt");
            FileWriter fileWriter = new FileWriter(file,true);

            fileWriter.append(dataRequest);
            fileWriter.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public static void createGoogleLog(LoginInputGoogleDTO loginInputGoogleDTO, HttpServletRequest request)
    {
        try
        {
            String dataRequest = getDataRequest(loginInputGoogleDTO,request);

            File file = new File("log.txt");
            FileWriter fileWriter = new FileWriter(file,true);

            fileWriter.append(dataRequest);
            fileWriter.close();
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    private static String getDataRequest(LoginInputDTO loginInputDTO,HttpServletRequest request)
    {
        Date date = new Date();

        // Obtém o User-Agent do cabeçalho da requisição
        String userAgentString = request.getHeader("User-Agent");

        // Analisa o User-Agent usando a biblioteca UserAgentUtils
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // Obtém informações sobre o sistema operacional e navegador
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();

        // Obtém o nome do sistema operacional
        String so = operatingSystem.getName();

        // Obtém o nome do navegador
        String browserClient = browser.getName();

        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        String ipClient = "";
        if (xForwardedForHeader == null)
        {
            ipClient = request.getRemoteAddr();
        }
        else
        {
            // O cabeçalho X-Forwarded-For pode conter uma lista de endereços IP, onde o primeiro endereço é o endereço do cliente.
            ipClient = xForwardedForHeader.split(",")[0].trim();
        }

        return "Date: "+date.getTime()+", IP: "+ipClient+", Browser: "+browserClient+", SO: "+so+" -> Login = [username: "+loginInputDTO.getUsername()+", password: "+loginInputDTO.getPassword()+"]\n";

    }

    private static String getDataRequest(LoginInputGoogleDTO loginInputGoogleDTO,HttpServletRequest request)
    {
        Date date = new Date();

        // Obtém o User-Agent do cabeçalho da requisição
        String userAgentString = request.getHeader("User-Agent");

        // Analisa o User-Agent usando a biblioteca UserAgentUtils
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentString);

        // Obtém informações sobre o sistema operacional e navegador
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        Browser browser = userAgent.getBrowser();

        // Obtém o nome do sistema operacional
        String so = operatingSystem.getName();

        // Obtém o nome do navegador
        String browserClient = browser.getName();

        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        String ipClient = "";
        if (xForwardedForHeader == null)
        {
            ipClient = request.getRemoteAddr();
        }
        else
        {
            // O cabeçalho X-Forwarded-For pode conter uma lista de endereços IP, onde o primeiro endereço é o endereço do cliente.
            ipClient = xForwardedForHeader.split(",")[0].trim();
        }

        return "Date: "+date.getTime()+", IP: "+ipClient+", Browser: "+browserClient+", SO: "+so+" -> LoginGoogle = [name: "+loginInputGoogleDTO.getName()+", email: "+loginInputGoogleDTO.getEmail()+", username: "+loginInputGoogleDTO.getUsername()+", password: "+loginInputGoogleDTO.getPassword()+"]\n";

    }


}
