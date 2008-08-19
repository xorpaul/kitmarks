/*
 * Copyright 2003 Jayson Falkner (jayson@jspinsider.com)
 * This code is from "Servlets and JavaServer pages; the J2EE Web Tier",
 * http://www.jspbook.com. You may freely use the code both commercially
 * and non-commercially. If you like the code, please pick up a copy of
 * the book and help support the authors, development of more free code,
 * and the JSP/Servlet/J2EE community.
 */
package bookmarks.filters.gzip;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookmarks.Utilities;

public class GZIPFilter implements Filter {

  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    if (req instanceof HttpServletRequest) {
      HttpServletRequest request = (HttpServletRequest) req;
      HttpServletResponse response = (HttpServletResponse) res;
      request.setCharacterEncoding(Utilities.REQUEST_ENCODING);
      response.setCharacterEncoding(Utilities.REQUEST_ENCODING);
      String ae = request.getHeader("accept-encoding");
      if (ae != null && ae.indexOf("gzip") != -1) {
        
        GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);
	try {
	   chain.doFilter(req, wrappedResponse);
	}
	catch(ServletException ex) {throw ex;}
	catch(IOException ex) {throw ex;}
	catch(Exception ex) {throw new ServletException(ex);}
	finally{
	    wrappedResponse.finishResponse();
	}
      }
      else chain.doFilter(req, res);
    }
  }

  public void init(FilterConfig filterConfig) {
    // noop
  }

  public void destroy() {
    // noop
  }
}
