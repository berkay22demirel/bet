package com.berkaydemirel.customer.service;

import com.berkaydemirel.customer.exception.InvalidCustomerException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author berkaydemirel
 */
@Service
public class CustomerService {

  private static final List<Long> customers = List.of(1L, 2L, 3L);

  /**
   * A mock method that checks the customer credentials
   */
  public void checkCustomer(Long customerId) {
    customers.stream()
             .filter(customerId::equals)
             .findAny()
             .orElseThrow(InvalidCustomerException::new);
  }
}
