@Component
public class CustomHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        
        // Check if the request URI matches a specific path
        if (requestUri.startsWith("/example")) {
            // Handle CORS pre-flight request (OPTIONS)
            if ("OPTIONS".equals(request.getMethod())) {
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
                response.setHeader("Access-Control-Max-Age", "3600");
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            
            // Handle cross-origin request
            response.setHeader("Access-Control-Allow-Origin", "*");
            
            // Make a REST call to a different URL
            ResponseEntity<String> restResponse = restTemplate.getForEntity("https://www.example.com/api/data", String.class);
            String restResponseBody = restResponse.getBody();
            
            // Set the REST response on the HTTP response
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(restResponseBody);
            return;
        }
        
        // Add custom header to response
        response.addHeader("Custom-Header", "Custom-Header-Value");
        
        filterChain.doFilter(request, response);
    }
}
