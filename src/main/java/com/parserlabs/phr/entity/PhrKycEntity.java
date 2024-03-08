package com.parserlabs.phr.entity;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PHR_USER_KYC")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class PhrKycEntity extends JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	private Long id;

	@Column(name = "phr_address")
	private String phrAddress;

	@Column(name = "health_id_number")
	private String healthIdNumber;
	
	@Column(name = "full_name")
	private String fullName;

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

	@Column(name = "kyc_document_type")
	private String kycDocumentType;

	@Column(name = "kyc_document_number")
	private String kycDocumentNumber;

	@Basic(fetch = LAZY)
	@Lob
	@Column(name = "profile_photo")
	private String profilePhoto;

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

	@Column(name = "address_line")
	private String addressLine;

	@Column(name = "pincode")
	private String pincode;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private PhrUserEntity user;
}
