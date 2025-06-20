package com.alura.Challenge_literalura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String name;
    private int fechaDeNacimiento;
    private int fechaDeMuerte;
//    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Libro> libros = new ArrayList<>();

    public Autor(){}

    public Autor( DatosAutor autor ) {
        this.name = autor.name();
        this.fechaDeNacimiento = autor.fechaDeNacimiento();
        this.fechaDeMuerte = autor.fechaDeMuerte();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(int fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public double getFechaDeMuerte() {
        return fechaDeMuerte;
    }

    public void setFechaDeMuerte(int fechaDeMuerte) {
        this.fechaDeMuerte = fechaDeMuerte;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

//    public List<Libro> getLibros() {
//        return libros;
//    }
//
//    public void setLibros(List<Libro> libros) {
//        this.libros = libros;
//    }

    @Override
    public String toString() {
        return  "\nAutor: " + name +
                "\nFecha de nacimiento: " + fechaDeNacimiento +
                "\nFecha de muerte: " + fechaDeMuerte + "\n";
    }
}
