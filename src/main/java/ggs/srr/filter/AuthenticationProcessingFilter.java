package ggs.srr.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ggs.srr.api.ApiResponse;
import ggs.srr.filter.dto.LoginInfoDto;
import ggs.srr.oauth.client.Client;
import ggs.srr.oauth.client.ClientManager;
import ggs.srr.oauth.provider.ProviderManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
public class AuthenticationProcessingFilter extends GenericFilterBean {

    private final ClientManager clientManager;
    private final ProviderManager providerManager;

    public AuthenticationProcessingFilter(ClientManager clientManager,
                                          ProviderManager providerManager) {
        this.clientManager = clientManager;
        this.providerManager = providerManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!isLoginPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        /**
         * todo
         * 인증부 재설계 필요
         * 인증 성공 handler 구현 필요
         * 인증 실패 handler 구현 필요
         */
        String code = request.getParameter("code");
        Client client = clientManager.getClient("42");
        LoginInfoDto loginInfoDto = providerManager.attemptAuthentication(code, client);
        ApiResponse<LoginInfoDto> result = ApiResponse.ok(loginInfoDto);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));

    }

    private boolean isLoginPath(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        return servletPath.equals("/login");
    }
}
