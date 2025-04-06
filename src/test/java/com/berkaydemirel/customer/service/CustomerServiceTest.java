package com.berkaydemirel.customer.service;

import com.berkaydemirel.customer.exception.InvalidCustomerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author berkaydemirel
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @InjectMocks
  private CustomerService customerService;

  @Test
  void checkCustomerShouldDoNothingWhenCustomerIsFound() {
    customerService.checkCustomer(1L);
  }

  @Test
  void checkEventVersionShouldThrowExceptionWhenCustomerIsNotFound() {
    assertThatThrownBy(() -> customerService.checkCustomer(100L))
        .isInstanceOf(InvalidCustomerException.class)
        .hasMessage("Invalid customer! Please check your customer info.");
  }
}
