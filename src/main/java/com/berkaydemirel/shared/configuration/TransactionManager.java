package com.berkaydemirel.shared.configuration;

import java.util.HashMap;
import java.util.Map;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * @author berkaydemirel
 */
public class TransactionManager extends JpaTransactionManager {

  private final Map<String, Integer> transactionTimeoutMap = new HashMap<>();

  public <T> void configureTxTimeout(Class<T> clazz, String methodName, Integer timeoutSecond) {
    transactionTimeoutMap.put(clazz.getName() + "." + methodName, timeoutSecond);
  }

  @Override
  protected int determineTimeout(TransactionDefinition definition) {
    ;
    if (transactionTimeoutMap.containsKey(definition.getName())) {
      return transactionTimeoutMap.get(definition.getName());
    } else {
      return super.determineTimeout(definition);
    }
  }
}
