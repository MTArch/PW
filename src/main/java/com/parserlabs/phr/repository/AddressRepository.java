package com.parserlabs.phr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrAddressEntity;

@Repository
@CustomSpanned
public interface AddressRepository extends JpaRepository<PhrAddressEntity, Long> {
	@Modifying
	@Query("delete from PhrAddressEntity u where u.id = ?1")
	void deleteById(Long id);
	
	
}
