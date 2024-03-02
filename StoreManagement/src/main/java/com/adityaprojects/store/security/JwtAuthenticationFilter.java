package com.adityaprojects.store.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//## Note : -
//For any incoming request this filter class gets executed.It checks
//if the request has a valid JWT token.If it has a valid JWT Token
//then it sets the Authentication in the context,
//to specify that the current user is authenticated.

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter  {

	private Logger logger=LoggerFactory.getLogger(OncePerRequestFilter.class);
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private UserDetailsService userDetailService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//Authorization
		
		String requestHeader=request.getHeader("Authorization");
		//Bearer 23454345645435tgfdefghdsf
		logger.info("Header : {}",requestHeader);
		String username=null;
		String token=null;
		
		if(requestHeader!=null&& requestHeader.startsWith("Bearer")) {
			//looking good
			token=requestHeader.substring(7);
			
			try {
				
				username=this.jwtHelper.getUsernameFromToken(token);
				
			}catch(IllegalArgumentException e) {
				logger.info("Illegal Argument while fetching the username !!");
				e.printStackTrace();
				
				
			}catch(ExpiredJwtException e) {
				logger.info("Given jwt token is expired");
				e.printStackTrace();
			}catch(MalformedJwtException e) {
				logger.info("Some changes hass done in token !! Invalid Token");
				e.printStackTrace();
			}catch(Exception e) {
				e.printStackTrace();
			}
				
		}else {
			logger.info("Invalid Header Value !!");
		}
		
		//
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			
			//fetch user detail from user name
			UserDetails userDetails=this.userDetailService.loadUserByUsername(username);
			Boolean validateToken=this.jwtHelper.validateToken(token, userDetails);
			
			if(validateToken) {
				//set Authentication 
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}else {
				logger.info("Validate fails !!");
			}
		}
		
		filterChain.doFilter(request, response);
	}
}
