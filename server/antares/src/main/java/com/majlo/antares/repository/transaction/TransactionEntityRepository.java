package com.majlo.antares.repository.transaction;

import com.majlo.antares.model.transaction.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionEntityRepository extends JpaRepository<TransactionEntity, Long> {
}
