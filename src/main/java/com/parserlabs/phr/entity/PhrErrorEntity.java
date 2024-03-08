package com.parserlabs.phr.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phr_error")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PhrErrorEntity {
	
	@Id
	@Column(name = "id", columnDefinition = "NUMERIC(19,0)")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "health_id_number")
	private String healthIdNumber;
	
	@Column(name = "req_payload")
	private String reqPayload;
	
	@Column(name = "res_payload")
	private String respPayload;
	
	@CreatedDate
	@Column(name = "created_date", nullable = false, updatable = false)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Column(name = "update_date", nullable = true, updatable = true)
	private LocalDateTime updateDate;
}
