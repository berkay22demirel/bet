package com.berkaydemirel.event.exception;

import com.berkaydemirel.shared.exception.ServiceException;

/**
 * @author berkaydemirel
 */
public class InvalidEventVersionException extends ServiceException {

  public InvalidEventVersionException() {
    super("The selected event is not current and requires updating!");
  }
}
