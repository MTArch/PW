package com.parserlabs.phr.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PHR_AUTH_TRANSACTION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(callSuper = false)
public class PhrAuthTransactionEntity extends JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "auth_transaction_id", unique = true, updatable = false, nullable = false, columnDefinition = "This is a unique id to track the transaction")
	private UUID authTransactionId;

	@Column(name = "phr_address")
	private String phrAddress;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "health_id_number")
	private String healthIdNumber;

	@Column(name = "mobile_country_code")
	private String mobileCountryCode;

	@Column(name = "auth_method")
	private String authMethod;
	
	@Column(name = "password")
	private String password;

	@Column(name = "retry_count")
	private Integer retryCount;

	@Column(name = "retry_method")
	private String retryMethod;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "health_id_transaction_id", unique = true, updatable = false, nullable = false, columnDefinition = "This is a unique id to track the  health id transaction")
	private String healthIdTransaction;
	
	@Column(name = "reference_token")
	private String referenceToken;
	
	
	@PrePersist
	protected void onCreateAbstractBaseEntity() {
		this.authTransactionId = UUID.randomUUID();
	}
}
