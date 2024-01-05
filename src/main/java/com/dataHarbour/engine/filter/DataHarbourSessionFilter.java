package com.dataHarbour.engine.filter;

import com.dataHarbour.engine.Service.impl.SessionService;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1)
public class DataHarbourSessionFilter extends OncePerRequestFilter{

    private static final Logger LOG = LoggerFactory.getLogger(DataHarbourSessionFilter.class);

    @Autowired
    private Environment environment;

    @Autowired
    private SessionService sessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if(request.getRequestURI().contains("api/v1")){
            String dataBaseId = request.getParameter(DataHarbourConstants.DATA_BASE_ID);
            if(StringUtils.isEmpty(dataBaseId)) {
                LOG.warn("No DataBaseId Found, Updating Changes to Default Data Base.");
                sessionService.setAttributeInSession(DataHarbourConstants.DATA_BASE_ID, DataHarbourConstants.DEFAULT);
            }else{
                sessionService.setAttributeInSession(DataHarbourConstants.DATA_BASE_ID, dataBaseId);
            }


        }

        filterChain.doFilter(request, response);


    }
}
