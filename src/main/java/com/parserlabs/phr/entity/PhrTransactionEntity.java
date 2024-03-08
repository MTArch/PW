package com.parserlabs.phr.entity;

import static javax.persistence.FetchType.LAZY;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PHR_TRANSACTION")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PhrTransactionEntity extends JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "transaction_id", unique = true, updatable = false, nullable = false, columnDefinition = "This is a unique id to track the transaction")
	protected UUID transactionId;

	@Column(name = "registration_method")
	private String registrationMethod;

	@Column(name = "phr_address")
	private String phrAddress;

	@Column(name = "health_id_number")
	private String healthIdNumber;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "middle_name")
	private String middleName;

	@Column(name = "gender")
	private String gender;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "mobile_country_code")
	private String mobileCountryCode;

	@Column(name = "day_of_birth")
	private String dayOfBirth;

	@Column(name = "month_of_birth")
	private String monthOfBirth;

	@Column(name = "year_of_birth")
	private String yearOfBirth;

	@Column(name = "date_of_birth")
	private String dateOfBirth;

	@Column(name = "kyc_status")
	private String kycStatus;

	@Basic(fetch = LAZY)
	//@Lob
	@Column(name = "profile_photo")
	private String profilePhoto;

	@Column(name = "password")
	private String password;

	@Column(name = "retry_count")
	private Integer retryCount;

	@Column(name = "retry_method")
	private String retryMethod;

	@Column(name = "country_name")
	private String countryName;

	@Column(name = "country_code")
	private String countryCode;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "district_name")
	private String districtName;

	@Column(name = "district_code")
	private String districtCode;

	@Column(name = "sub_district_name")
	private String subDistrictName;

	@Column(name = "sub_district_code")
	private String subDistrictCode;

	@Column(name = "town_name")
	private String townName;

	@Column(name = "town_code")
	private String townCode;

	@Column(name = "village_name")
	private String villageName;

	@Column(name = "village_code")
	private String villageCode;

	@Column(name = "ward_name")
	private String wardName;

	@Column(name = "ward_code")
	private String wardCode;
	
	@Column(name = "full_name")
	private String fullName;

	@Column(name = "address_line")
	private String addressLine;

	@Column(name = "address_type")
	private String addressType;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "mobile_verified")
	private Boolean mobileVerified;

	@Column(name = "email_verified")
	private Boolean emailVerified;
	
	@Column(name = "otp")
	private String otp;
	
	@Column(name = "status")
	private String status;

	@Column(name = "kyc_type")
	private String kycType;

	@Column(name = "reference_id", updatable = false, columnDefinition = "This is the health-id system's txnId")
	private String referenceId;
	
	@Column(name = "hid_token")
	private String hidToken;
	
	@Column(name = "auth_transaction_id")
	private UUID authTransactionId;

	@PrePersist
	protected void onCreateAbstractBaseEntity() {
		this.transactionId = UUID.randomUUID();
	}

}