package com.berkaydemirel.customer.exception;

import com.berkaydemirel.shared.exception.ServiceException;

/**
 * @author berkaydemirel
 */
public class InvalidCustomerException extends ServiceException {

  public InvalidCustomerException() {
    super("Invalid customer! Please check your customer info.");
  }
}
