package com.parserlabs.phr.repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrUserEntity;

@CustomSpanned
public interface UserRepository extends JpaRepository<PhrUserEntity, BigInteger> {

	@Transactional
	Optional<PhrUserEntity> findByPhrAddress(String phrAddress);
	
	@Transactional
	Optional<List<PhrUserEntity>> findByHealthIdNumber(String healthIdNumber);
	
	Set<String> findByHealthIdNumberAndStatus(String healthIdNumber, String status);

	Optional<List<PhrUserEntity>> findByMobile(String mobile);

	Optional<List<PhrUserEntity>> findByMobileAndMobileVerified(String mobile, Boolean mobileVerified);

	Optional<List<PhrUserEntity>> findByEmail(String email);

	Optional<List<PhrUserEntity>> findByEmailAndEmailVerified(String email, Boolean emailVerified);

	Void deleteByPhrAddress(String phrAddress);

	Boolean existsByPhrAddress(String phrAddress);

	Optional<Boolean> existsByMobile(String mobile);

	Optional<Boolean> existsByMobileAndMobileVerified(String mobile, Boolean mobileVerified);

	Optional<Boolean> existsByEmail(String email);

	Optional<Boolean> existsByEmailAndEmailVerified(String email, Boolean emailVerified);

	Optional<Boolean> existsByHealthIdNumber(String healthIdNumber);
	
	Optional<Boolean> existsByHealthIdNumberAndYearOfBirth(String healthIdNumber, String yearOfBirth);
	
	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.healthIdNumber = ?1")
	Optional<List<PhrUserEntity>> getUserLiteByHealthIdNumber(String healthIdNumber);
	
	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.mobile = ?1")
	Optional<List<PhrUserEntity>> getUserLiteByMobile(String mobile);

	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.mobile = ?1 and u.mobileVerified =?2")
	Optional<List<PhrUserEntity>> getUserLiteByMobileAndMobileVerified(String mobile, Boolean mobileVerified);

	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.email = ?1")
	Optional<List<PhrUserEntity>> getUserLiteByEmail(String email);

	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.email = ?1 and u.emailVerified =?2")
	Optional<List<PhrUserEntity>> getUserLiteByEmailAndEmailVerified(String email, Boolean emailVerified);

	@Query("SELECT u.phrAddress FROM PhrUserEntity u WHERE u.phrAddress in ?1 ")
	Optional<List<String>> checkForPhrExists(Set<String> listPHRSuggestion);

	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName) FROM PhrUserEntity u WHERE u.fullName = ?1 and u.gender =?2 and u.yearOfBirth = ?3 and u.monthOfBirth = ?4 and u.dayOfBirth = ?5")
	Optional<Set<PhrUserEntity>> findPhrWithGivenMeta(String fullname, String gender, String yearOfBirth, String monthOfBirth, String dayOfBirth);
	
	@Transactional
	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName, u.profilePhoto) FROM PhrUserEntity u WHERE u.mobile = ?1 and u.mobileVerified =?2 and u.status =?3")
	Optional<List<PhrUserEntity>> getUserLiteByMobileAndMobileVerifiedAndStatus(String mobile, Boolean mobileVerified,String status);

	@Transactional
	@Query("SELECT new com.parserlabs.phr.entity.PhrUserEntity(u.phrAddress, u.status, u.mobile, u.email, u.fullName, u.profilePhoto) FROM PhrUserEntity u WHERE u.email = ?1 and u.emailVerified =?2 and u.status =?3")
	Optional<List<PhrUserEntity>> getUserLiteByEmailAndEmailVerifiedAndStatus(String email, Boolean emailVerified,String status);



}
