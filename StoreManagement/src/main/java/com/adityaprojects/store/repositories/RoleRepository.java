package com.adityaprojects.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.adityaprojects.store.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
