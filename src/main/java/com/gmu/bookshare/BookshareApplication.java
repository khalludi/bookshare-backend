package com.gmu.bookshare;

import com.kakawait.spring.boot.security.cas.autoconfigure.CasSecurityProperties;
import com.kakawait.spring.security.cas.client.CasAuthorizationInterceptor;
import com.kakawait.spring.security.cas.client.ticket.ProxyTicketProvider;
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
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.client.RestTemplate;
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
    RestTemplate casRestTemplate(ServiceProperties serviceProperties, ProxyTicketProvider proxyTicketProvider) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add(new CasAuthorizationInterceptor(serviceProperties, proxyTicketProvider));
        return restTemplate;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService("http://localhost:9090/login/cas");
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

//    @Bean
//    public ServiceProperties serviceProperties() {
//        ServiceProperties serviceProperties = new ServiceProperties();
//        serviceProperties.setService("http://localhost:9009/login/cas");
//        serviceProperties.setSendRenew(false);
//        return serviceProperties;
//    }
//
//    @Bean
//    @Primary
//    public AuthenticationEntryPoint authenticationEntryPoint(
//            ServiceProperties sP) {
//
//        // URL where user will be redirected to for authentication
//        CasAuthenticationEntryPoint entryPoint
//                = new CasAuthenticationEntryPoint();
//        entryPoint.setLoginUrl("https://localhost:6443/cas/login");
//        entryPoint.setServiceProperties(sP);
//        return entryPoint;
//    }
//
//    /**
//     * Validates service ticket given to user upon authentication.
//     *
//     * @return TicketValidator object
//     */
//    @Bean
//    public TicketValidator ticketValidator() {
//        return new Cas30ServiceTicketValidator(
//                "https://localhost:6443/cas");
//    }
//
//    @Bean
//    public CasAuthenticationProvider casAuthenticationProvider() {
//
//        CasAuthenticationProvider provider = new CasAuthenticationProvider();
//        provider.setServiceProperties(serviceProperties());
//        provider.setTicketValidator(ticketValidator());
//        provider.setUserDetailsService(
//                s -> new User("casuser", "Mellon", true, true, true, true,
//                        AuthorityUtils.createAuthorityList("ROLE_ADMIN")));
//        provider.setKey("CAS_PROVIDER_LOCALHOST_9000");
//        return provider;
//    }
//
//    @Bean
//    public SecurityContextLogoutHandler securityContextLogoutHandler() {
//        return new SecurityContextLogoutHandler();
//    }
//
//    @Bean
//    public LogoutFilter logoutFilter() {
//        LogoutFilter logoutFilter = new LogoutFilter(
//                "https://localhost:6443/cas/logout",
//                securityContextLogoutHandler());
//        logoutFilter.setFilterProcessesUrl("/logout/cas");
//        return logoutFilter;
//    }
//
//    @Bean
//    public SingleSignOutFilter singleSignOutFilter() {
//        SingleSignOutFilter singleSignOutFilter = new SingleSignOutFilter();
//        singleSignOutFilter.setCasServerUrlPrefix("https://localhost:6443/cas");
//        singleSignOutFilter.setIgnoreInitConfiguration(true);
//        return singleSignOutFilter;
//    }
//
//    @EventListener
//    public SingleSignOutHttpSessionListener singleSignOutHttpSessionListener(
//            HttpSessionEvent event) {
//        return new SingleSignOutHttpSessionListener();
//    }
}
