package com.berkaydemirel.shared.configuration;

import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;

/**
 * @author berkaydemirel
 */
@Service
public class RandomProvider {

  public boolean getRandom() {
    return ThreadLocalRandom.current().nextDouble() < 0.1;
  }
}
