package com.springboot.apirest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.springboot.apirest.entity.Mascota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Integer> {
    Page<Mascota> findByUsuarioEmail(String email, Pageable pageable);
}
