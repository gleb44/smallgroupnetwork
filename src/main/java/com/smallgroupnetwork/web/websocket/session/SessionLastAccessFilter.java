package com.smallgroupnetwork.web.websocket.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

@WebFilter( filterName = "sessionLastAccessFilter", asyncSupported = true, urlPatterns = "/*" )
@Component
public class SessionLastAccessFilter implements Filter
{
	private static final Logger logger = LoggerFactory.getLogger( SessionLastAccessFilter.class );

	@Override
	public void init( FilterConfig filterConfig ) throws ServletException
	{
		// none
	}

	@Override
	public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) request;

		HttpSession session = httpRequest.getSession( false );
		if( session == null )
		{
			// Set LastAccessTime if session created.
			chain.doFilter( new SessionAccessAwareRequest( httpRequest ), response );
		}
		else
		{
			try
			{
				// Update LastAccessTime if session already exist.
				session.setAttribute( SessionRegistry.getLastAccessSessionAttrName(), System.currentTimeMillis() );
			}
			catch( IllegalStateException ignored )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug( "Cannot update LastAccessSession value. The Session has already been invalidated" );
				}
			}

			chain.doFilter( request, response );
		}
	}

	@Override
	public void destroy()
	{
		// none
	}

	private static class SessionAccessAwareRequest extends HttpServletRequestWrapper
	{
		/**
		 * Constructs a request object wrapping the given request.
		 *
		 * @param request HttpServletRequest
		 * @throws IllegalArgumentException if the request is null
		 */
		public SessionAccessAwareRequest( HttpServletRequest request )
		{
			super( request );
		}

		@Override
		public HttpSession getSession()
		{
			return getSession( true );
		}

		@Override
		public HttpSession getSession( boolean create )
		{
			HttpSession session = super.getSession( create );
			if( session != null )
			{
				try
				{
					session.setAttribute( SessionRegistry.getLastAccessSessionAttrName(), System.currentTimeMillis() );
				}
				catch( IllegalStateException ignored )
				{
					// The Session has already been invalidated.
				}
			}
			return session;
		}
	}
}
