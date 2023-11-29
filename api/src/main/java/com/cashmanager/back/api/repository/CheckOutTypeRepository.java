package com.cashmanager.back.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cashmanager.back.api.model.CheckOutType;

@Repository
public interface CheckOutTypeRepository extends JpaRepository<CheckOutType, Long> {
	@Query(value = "select * from check_out_type where check_out_type.id=:id", nativeQuery=true)
	Optional<CheckOutType> findById(@Param("id") String checkOutTypeId);

}
