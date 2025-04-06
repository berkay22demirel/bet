package com.berkaydemirel.shared.configuration;

import com.berkaydemirel.betslip.service.BetSlipService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author berkaydemirel
 */
@Configuration
public class TransactionConfig {

  @Value("${betslip.creation-timeout-second:}")
  private int creationTimeoutSecond;

  @Bean
  public PlatformTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
    final TransactionManager transactionManager = new TransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    transactionManager.configureTxTimeout(BetSlipService.class, "create", creationTimeoutSecond);
    return transactionManager;
  }
}
