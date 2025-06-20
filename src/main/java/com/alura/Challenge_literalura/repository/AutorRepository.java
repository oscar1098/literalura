package com.alura.Challenge_literalura.repository;

import com.alura.Challenge_literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {

    Optional<Autor> findByName(String name);
    List<Autor> findByNameContainingIgnoreCase(String name);
    List<Autor> findByFechaDeNacimiento(int fechaNacimiento);

    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :fecha AND a.fechaDeMuerte >= :fecha")
    List<Autor> autoresVivos( int fecha );

}
