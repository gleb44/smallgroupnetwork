package com.smallgroupnetwork.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * User: gleb
 * Date: 12/5/13
 * Time: 6:35 PM
 */
@ResponseStatus( value = HttpStatus.UNAUTHORIZED )
public class UnauthorizedException extends RuntimeException
{
}
