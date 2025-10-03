package com.greenplore.Backend.user_service.auth;

import com.greenplore.Backend.user_service.entity.RefreshToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService ;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    private Optional<Cookie> getCookies(HttpServletRequest request , String name){
        if (request.getCookies()==null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c->c.getName().equals(name))
                .findFirst();
    }

    private void setAuthentication(UserDetails userDetails , HttpServletRequest request){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/v1/auth/") || path.startsWith("/api/v1/public/") || path.equals("/");
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    
        String accessToken = getCookies(request,"accessToken")
                .map(Cookie::getValue)
                .orElse(null);
        String refreshToken = getCookies(request , "refreshToken")
                .map(Cookie::getValue)
                .orElse(null);
        String username = null;

        try {
            // 1. Try Access Token
            if(accessToken !=null ){
                username=jwtService.extractUsername(accessToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtService.validateToken(accessToken,userDetails)){
                    setAuthentication(userDetails , request);
                    filterChain.doFilter(request,response);
                    return;
                }

            }
            // 2. Access token is missing/expired → Try Refresh Token
            if(refreshToken!=null){
                RefreshToken storedToken = refreshTokenService.findByToken(refreshToken)
                        .orElseThrow(()-> new RuntimeException("Refresh token not found"));

                refreshTokenService.isRefreshTokenExpired(storedToken);

                username=storedToken.getUser().getEmail();
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                //Set new access token
                String newAccessToken = jwtService.generateToken(username);
                Cookie newAccessCookie = new Cookie("accessToken",newAccessToken);
                newAccessCookie.setHttpOnly(true);
                newAccessCookie.setSecure(true);
                newAccessCookie.setPath("/");
                newAccessCookie.setMaxAge(60*60);
                response.addCookie(newAccessCookie);

                setAuthentication(userDetails,request);
                filterChain.doFilter(request,response);
            }

            // 3. No valid token
            response.setStatus(440); // Custom code: Login Required
        }catch (Exception e){
            response.setStatus(440);
        }
    }
}
