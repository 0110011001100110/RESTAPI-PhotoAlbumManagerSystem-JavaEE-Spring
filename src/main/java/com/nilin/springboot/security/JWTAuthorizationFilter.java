package com.nilin.springboot.security;

import com.nilin.springboot.service.UserService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private final String SECRET = "mySecretKey";

	@Autowired
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		try {
			if (checkJWTToken(request)) {
				Claims claims = validateToken(request);
				if (claims.get("authorities") != null) {
					setUpSpringAuthentication(claims);
				} else {
					SecurityContextHolder.clearContext();
				}
			}
			chain.doFilter(request, response);

		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	/**
	 * Authentication method in Spring flow
	 *
	 * @param claims
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	public boolean checkJWTToken(HttpServletRequest request) {
		String authenticationHeader = request.getHeader(HEADER);

//		Map pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//		String username = (String)pathVariables.get("username");
//		User existing = userService.findByUsername(username);
//		String userTokenInDB = existing.getToken();

		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX) )
			return false;
		return true;
	}




}




//public class JWTAuthorizationFilter extends OncePerRequestFilter {
//
//@Autowired
//private UserDetailsService userDetailsService;
//
//@Autowired
//private JwtTokenUtil jwtTokenUtil;
//
//	public static final String SIGNING_KEY = "devglan123r";
//	public static final String TOKEN_PREFIX = "Bearer ";
//	public static final String HEADER_STRING = "Authorization";
//
//@Override
//protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//		String header = req.getHeader(HEADER_STRING);
//		String username = null;
//		String authToken = null;
//		if (header != null && header.startsWith(TOKEN_PREFIX)) {
//		authToken = header.replace(TOKEN_PREFIX,"");
//		try {
//		username = jwtTokenUtil.getUsernameFromToken(authToken);
//		} catch (IllegalArgumentException e) {
//		logger.error("an error occured during getting username from token", e);
//		} catch (ExpiredJwtException e) {
//		logger.warn("the token is expired and not valid anymore", e);
//		} catch(SignatureException e){
//		logger.error("Authentication Failed. Username or Password not valid.");
//		}
//		} else {
//		logger.warn("couldn't find bearer string, will ignore the header");
//		}
//		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//		if (jwtTokenUtil.validateToken(authToken, userDetails)) {
//		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
//		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//		logger.info("authenticated user " + username + ", setting security context");
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		}
//		}
//
//		chain.doFilter(req, res);
//		}
//		}