package com.endava.webapp.repositories.springdata;

import com.endava.webapp.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Modifying
    @Transactional
    @Query("update Department d set d.departmentName =:depName, " +
            "d.locationName = :locName where d.departmentId = :id")
    int updateById(@Param("id") Long id, @Param("depName") String newDepartmentName,
                    @Param("locName") String newLocationName);

}
