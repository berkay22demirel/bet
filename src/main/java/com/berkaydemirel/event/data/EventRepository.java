package com.berkaydemirel.event.data;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author berkaydemirel
 */
public interface EventRepository extends JpaRepository<Event, Long> {

}
