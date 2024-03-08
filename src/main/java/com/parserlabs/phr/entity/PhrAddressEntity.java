package com.parserlabs.phr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PHR_ADDRESS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class PhrAddressEntity extends  JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

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

	@Column(name = "address_type")
	private String addressType;

	@Column(name = "pincode")
	private String pincode;

	@Column(name = "status")
	private String status;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private PhrUserEntity user;
}
