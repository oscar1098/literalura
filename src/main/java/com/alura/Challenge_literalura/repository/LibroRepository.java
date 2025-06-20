package com.alura.Challenge_literalura.repository;

import com.alura.Challenge_literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);

    List<Libro> findTop10ByOrderByNumeroDeDescargasDesc();

    @Query("SELECT l FROM Libro l WHERE l.idioma ILIKE %:idiomaIngresado%")
    List<Libro> librosEnIdiomaIngresado(String idiomaIngresado );
}
