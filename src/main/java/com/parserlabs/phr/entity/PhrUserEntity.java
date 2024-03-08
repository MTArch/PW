package com.parserlabs.phr.entity;

import static javax.persistence.FetchType.LAZY;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.parserlabs.phr.utils.GeneralUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "PHR_USERS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Setter
@Getter
@ToString
public class PhrUserEntity extends JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "phr_address", unique = true, updatable = false)
	private String phrAddress;

	@Column(name = "health_id_number")
	private String healthIdNumber;

	@Column(name = "password")
	private String password;

	@Column(name = "phr_provider")
	private String phrProvider;

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

	@Column(name = "email_verified")
	private boolean emailVerified;

	@Column(name = "mobile")
	private String mobile;

	@Column(name = "mobile_verified")
	private boolean mobileVerified;

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

	@Column(name = "is_profile_photo_compressed")
	private Boolean profilePhotoCompressed;
	
	@Column(name = "kyc_status")
	private String kycStatus;

	@Basic(fetch = LAZY)
	//@Lob
	@Column(name = "profile_photo")
	private String profilePhoto;

	@Column(name = "Status")
	private String status;

	@OneToOne(mappedBy = "user", cascade = { CascadeType.ALL })
	private PhrAddressEntity address;
	
	@Column(name = "reason_code")
	private String reasonCode; 
	

	@OneToOne(mappedBy = "user", cascade = { CascadeType.ALL })
	private PhrKycEntity phrKyc;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true, cascade = { CascadeType.ALL })
	private Set<PhrAuthMethodEntity> phrAuthMethodEntity;

	public void prePersist() {
		if (StringUtils.isNotBlank(phrAddress))
			phrAddress = GeneralUtils.sanetizePhrAddress(phrAddress);
		
		email = email.toLowerCase();

	}

	public PhrUserEntity(String phrAddress, String status, String mobile, String email, String fullName) {
		this.phrAddress = phrAddress;
		this.mobile = mobile;
		this.fullName = fullName;
		this.status = status;
		this.email = email;
	}
	
	public PhrUserEntity(String phrAddress, String status, String mobile, String email, String fullName, String profilePhoto) {
		this.phrAddress = phrAddress;
		this.mobile = mobile;
		this.fullName = fullName;
		this.status = status;
		this.email = email;
		this.profilePhoto=profilePhoto;
	}
	
	public PhrUserEntity(String healthIdNumber, String KycStatus) {
 		this.healthIdNumber = healthIdNumber;
 		this.kycStatus = KycStatus;

 	}


}
