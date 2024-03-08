/**
 * 
 */
package com.parserlabs.phr.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAuthTransactionEntity;

/**
 * @author Rajesh
 *
 */
@Repository
@CustomSpanned
public interface AuthTransactionRepository extends JpaRepository<PhrAuthTransactionEntity, Long> {

	@Transactional
	Optional<PhrAuthTransactionEntity> findByAuthTransactionId(UUID authTransactionId);

	Optional<PhrAuthTransactionEntity> findByAuthTransactionIdAndStatus(UUID authTransactionId, String status);

	@Transactional
	Optional<PhrAuthTransactionEntity> findTop1ByPhrAddressOrderByIdDesc(String  phrAddress);
}
