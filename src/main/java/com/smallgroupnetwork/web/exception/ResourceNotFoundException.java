package com.smallgroupnetwork.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Date: 10/4/13
 * Time: 8:33 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
@ResponseStatus( value = HttpStatus.NOT_FOUND )
public class ResourceNotFoundException extends RuntimeException
{
}
