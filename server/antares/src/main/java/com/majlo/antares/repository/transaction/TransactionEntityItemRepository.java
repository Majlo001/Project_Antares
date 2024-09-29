package com.majlo.antares.repository.transaction;

import com.majlo.antares.model.transaction.TransactionEntityItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionEntityItemRepository extends JpaRepository<TransactionEntityItem, Long> {
}
