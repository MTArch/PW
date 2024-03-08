package com.parserlabs.phr.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PHR_USERS_AUTH_METHODS")

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode(callSuper=false)
public class PhrAuthMethodEntity extends JPAAuditorEntity<String> {

	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "auth_method")
	private String authMethod;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
	private PhrUserEntity user;

	@Column(name = "status")
	private String status;

}