package com.smallgroupnetwork.web.controller.components;

import com.smallgroupnetwork.validation.ValidationException;
import com.smallgroupnetwork.validation.ValidationResult;
import com.smallgroupnetwork.web.exception.ForbiddenException;
import com.smallgroupnetwork.web.exception.ResourceNotFoundException;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.InternalErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

@ControllerAdvice
public class JsonViewExceptionResolver extends ResponseEntityExceptionHandler
{
	private Logger logger = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	ServletContext servletContext;
	@Autowired
	protected ObjectMapper jacksonObjectMapper;
	@Autowired
	public MessageSource messageSource;

	@ExceptionHandler( Throwable.class )
	public void handler( Exception ex, HttpServletRequest request, HttpServletResponse servletResponse )
	{

		logger.error( "MVC exception", ex );
		writeResponseEntity( handleInternal( ex ), servletResponse );
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal( Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request )
	{
		logger.error( "MVC Internal exception", ex );
		return handleInternal( ex );
	}

	private ResponseEntity handleInternal( Exception ex )
	{
		if( ex instanceof ResourceNotFoundException )
		{
			return jsonEntity( DefaultMessages.NOT_FOUND_RESPONSE, HttpStatus.NOT_FOUND );
		}
		else if( ex instanceof ValidationException )
		{
			Collection<ValidationResult> items = ((ValidationException) ex).getItems();
			for( ValidationResult item : items )
			{
				item.setMessage( messageSource.getMessage( item.getMessageCode(), item.getParameters(), null ) );
			}
			return jsonEntity( ex, HttpStatus.INTERNAL_SERVER_ERROR );
		}
		else if( ex instanceof UnauthorizedException )
		{
			return jsonEntity( ex, HttpStatus.UNAUTHORIZED );
		}
		else if( ex instanceof ForbiddenException )
		{
			return jsonEntity( ex, HttpStatus.FORBIDDEN );
		}
		else
		{
			return jsonEntity( new InternalErrorResponse( ex.toString(), ex.getStackTrace() ), HttpStatus.INTERNAL_SERVER_ERROR );
		}
	}


	private void writeResponseEntity( ResponseEntity entity, HttpServletResponse servletResponse )
	{
		servletResponse.setStatus( entity.getStatusCode().value() );

		HttpHeaders entityHeaders = entity.getHeaders();
		if( !entityHeaders.isEmpty() )
		{
			for( String key : entityHeaders.keySet() )
			{
				for( String value : entityHeaders.get( key ) )
				{
					servletResponse.addHeader( key, value );
				}
			}
		}

		Object content = entity.getBody();
		try
		{
			ServletOutputStream outputStream = servletResponse.getOutputStream();
			if( entity instanceof JsonResponseEntity )
			{
				jacksonObjectMapper.writeValue( outputStream, content );
			}
			else if( entity instanceof HttpResponseEntity )
			{
				HttpResponseEntity httpResponseEntity = (HttpResponseEntity) entity;
				FileCopyUtils.copy( httpResponseEntity.getBody(), outputStream );
			}
			else
			{
				throw new UnsupportedOperationException( "Response entity is usupported" );
			}
			outputStream.flush();
		}
		catch( Exception e )
		{
			logger.error( "Output writing failed", e );
		}
	}


	private JsonResponseEntity jsonEntity( Object content, HttpStatus statusCode )
	{
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.APPLICATION_JSON );
		return new JsonResponseEntity( content, headers, statusCode );
	}

	private HttpResponseEntity resourceEntity( String name, HttpStatus statusCode )
	{
		ServletContextResource resource = new ServletContextResource( servletContext, name );
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType( MediaType.TEXT_HTML );

		ByteArrayOutputStream body = new ByteArrayOutputStream();
		try
		{
			FileCopyUtils.copy( resource.getInputStream(), body );
			return new HttpResponseEntity( body.toByteArray(), headers, statusCode );
		}
		catch( IOException e )
		{
			logger.error( "Resource loading error", e );
			return new HttpResponseEntity( "Internal server error".getBytes(), headers, statusCode );
		}
	}
}

class JsonResponseEntity extends ResponseEntity
{
	public JsonResponseEntity( HttpStatus statusCode )
	{
		super( statusCode );
	}

	public JsonResponseEntity( Object body, HttpStatus statusCode )
	{
		super( body, statusCode );
	}

	public JsonResponseEntity( MultiValueMap<String, String> headers, HttpStatus statusCode )
	{
		super( headers, statusCode );
	}

	public JsonResponseEntity( Object body, MultiValueMap<String, String> headers, HttpStatus statusCode )
	{
		super( body, headers, statusCode );
	}
}

class HttpResponseEntity extends ResponseEntity<byte[]>
{
	public HttpResponseEntity( HttpStatus statusCode )
	{
		super( statusCode );
	}

	public HttpResponseEntity( byte[] body, HttpStatus statusCode )
	{
		super( body, statusCode );
	}

	public HttpResponseEntity( MultiValueMap<String, String> headers, HttpStatus statusCode )
	{
		super( headers, statusCode );
	}

	public HttpResponseEntity( byte[] body, MultiValueMap<String, String> headers, HttpStatus statusCode )
	{
		super( body, headers, statusCode );
	}
}