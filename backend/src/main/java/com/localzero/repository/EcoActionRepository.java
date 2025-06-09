package com.localzero.repository;

import com.localzero.model.EcoAction;
import com.localzero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EcoActionRepository extends JpaRepository<EcoAction, Long> {
    List<EcoAction> findByUser(User user);
    List<EcoAction> findByUserOrderByActionDateDesc(User user); // spring uses the java property (actionDate) not the column name (action_date)
}
