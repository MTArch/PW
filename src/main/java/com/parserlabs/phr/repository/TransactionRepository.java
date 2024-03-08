/**
 * 
 */
package com.parserlabs.phr.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrTransactionEntity;
import com.parserlabs.phr.model.PhrTransactionEntityLite;

/**
 * @author Rajesh
 *
 */

@Repository
@CustomSpanned
public interface TransactionRepository extends JpaRepository<PhrTransactionEntity, Long> {

	@Transactional
	Optional<PhrTransactionEntity> findByTransactionId(UUID transactionId);

	@Transactional
	Optional<PhrTransactionEntity> findByTransactionIdOrAuthTransactionId(UUID transactionId, UUID authTransactionId);

	Optional<PhrTransactionEntity> findByTransactionIdAndStatus(UUID transactionId, String status);

	@Query(value = "SELECT t.id, t.transaction_id\\:\\:text as transaction_id, t.status FROM phr_transaction t WHERE t.created_date <  (now() - '?1 hours'\\:\\:interval)", nativeQuery = true)
	Optional<List<PhrTransactionEntityLite>> findOverdueTransaction(int intervalTime);

	@Transactional
	void deleteByTransactionId(UUID transactionId);
	
	@Transactional
	Optional<PhrTransactionEntity> findByPhrAddress(String phrAddress);

}
