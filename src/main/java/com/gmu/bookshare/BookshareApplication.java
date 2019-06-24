package com.gmu.bookshare;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.validation.Cas30ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidator;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.servlet.http.HttpSessionEvent;

@SpringBootApplication
public class BookshareApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookshareApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    FilterRegistrationBean forwardedHeaderFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ForwardedHeaderFilter());
        filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return filterRegistrationBean;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService("https://localhost:9090/login/cas");
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    @Bean
    @Primary
    public CasAuthenticationEntryPoint authenticationEntryPoint(
            ServiceProperties sP) {

        // URL where user will be redirected to for authentication
        CasAuthenticationEntryPoint entryPoint
                = new CasAuthenticationEntryPoint();
//        entryPoint.setLoginUrl("https://boiling-waters-26199.herokuapp.com/https://login.gmu.edu/login");
        entryPoint.setLoginUrl("https://login.gmu.edu/login");
        entryPoint.setServiceProperties(sP);
        return entryPoint;
    }

    /**
     * Validates service ticket given to user upon authentication.
     *
     * @return TicketValidator object
     */
    @Bean
    public TicketValidator ticketValidator() {
        return new Cas30ServiceTicketValidator(
                "https://login.gmu.edu/");
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider() {

        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties());
        provider.setTicketValidator(ticketValidator());
        provider.setUserDetailsService(
                s -> new User("casuser", "Mellon", true, true, true, true,
                        AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
        provider.setKey("CAS_PROVIDER_LOCALHOST_9000");
        return provider;
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter(
                "https://login.gmu.edu/logout",
                securityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl("/logout/cas");
        return logoutFilter;
    }

    @Bean
    public SingleSignOutFilter singleSignOutFilter() {
        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix("https://login.gmu.edu/");
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    @EventListener
    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(
            HttpSessionEvent event) {
        return new SingleSignOutHttpSessionListener();
    }
}
