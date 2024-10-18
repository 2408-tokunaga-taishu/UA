package com.example.UA.filter;

import com.example.UA.controller.form.AccountForm;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SuperVisorFilter implements Filter {

    @Autowired
    private HttpSession session;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        session = httpRequest.getSession();

        AccountForm account = (AccountForm) session.getAttribute("loginAccount");
        if (account.getSuperVisor() == 0) {
            List<String> errorMessages = new ArrayList<>();
            errorMessages.add("無効なアクセスです");
            session.setAttribute("errorMessages", errorMessages);
            httpResponse.sendRedirect("/UA");
        } else {
            chain.doFilter(httpRequest,httpResponse);
        }

    }

    @Override
    public void init(FilterConfig config) {
    }

    @Override
    public void destroy() {
    }

}
