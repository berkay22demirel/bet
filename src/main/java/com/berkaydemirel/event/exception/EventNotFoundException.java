package com.berkaydemirel.event.exception;

import com.berkaydemirel.shared.exception.ServiceException;

/**
 * @author berkaydemirel
 */
public class EventNotFoundException extends ServiceException {

  public EventNotFoundException() {
    super("Selected event is not found!");
  }
}
