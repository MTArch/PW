package com.parserlabs.phr.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.parserlabs.phr.addednew.CustomSpanned;
import com.parserlabs.phr.entity.PhrErrorEntity;


@Repository
@CustomSpanned
public interface PhrErrorRepository  extends JpaRepository<PhrErrorEntity, BigInteger> {

}
