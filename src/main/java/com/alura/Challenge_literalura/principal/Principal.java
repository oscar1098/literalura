package com.alura.Challenge_literalura.principal;

import com.alura.Challenge_literalura.model.Autor;
import com.alura.Challenge_literalura.model.DatosAutor;
import com.alura.Challenge_literalura.model.DatosLibro;
import com.alura.Challenge_literalura.model.Libro;
import com.alura.Challenge_literalura.repository.AutorRepository;
import com.alura.Challenge_literalura.repository.LibroRepository;
import com.alura.Challenge_literalura.service.ConsumoApi;
import com.alura.Challenge_literalura.service.ConvierteDatos;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro>  libros = new ArrayList<>();
    private List<Autor> autores = new ArrayList<>();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void ejecutarPrograma(){
        int opcion = -1;
        while (opcion != 0){
            muestraElMenu();
            opcion = validarOpcionIngresada(this::muestraElMenu);

            switch (opcion){
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivos();
                    break;
                case 5:
                    lisarLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                    break;
                case 7:
                    buscarAutorPorNombre();
                    break;
                case 8:
                    autoresNacidosEnUnAno();
                    break;
                case 0:
                    System.out.println("Cerrando la aplciación");
                    break;
                default:
                    System.out.println("****************** Por favor ingrese una opcion valida ******************");
                    break;

            }

        }

    }



    private DatosLibro getDatosLibro(){
        String json;

        while (true){
            System.out.println("Ingresa el nombre del libro que deseas buscar");
            String nombreLibro = teclado.nextLine();
            String query = URLEncoder.encode(nombreLibro, StandardCharsets.UTF_8);
            json = consumoApi.obtenerDatos(URL_BASE + "?search=" + query);

            if (json.equals("Error")){
                System.out.println("Libro no encontrado");
            } else {
                break;
            }
        }

        DatosLibro datos = conversor.obtenerDatos(json,DatosLibro.class);
        return datos;
    }

    private void muestraElMenu(){
        String menu = """
                    Elija la opcion deseada:
                    
                    1- Buscar libros por titulo.
                    2- Listar libros registrados.
                    3- Listar autores registrados.
                    4- Listar autores vivos en un determinado año.
                    5- Listar libros por idioma.
                    6 -Top 10 libros mas descargados.
                    7 -Buscar auytor por nombre.
                    8 -Autores nacidos en un año.
                    0- Salir.  
                    """;
        System.out.println(menu);
        }

        private int validarOpcionIngresada(Runnable funcion){

            while (true){
                try{
                    int opcionValidar = teclado.nextInt();
                    teclado.nextLine();
                    return opcionValidar;
                } catch (InputMismatchException e){
                    System.out.println("****************** Por favor ingrese una opcion valida ******************");
                    funcion.run();
                    teclado.nextLine();
                }
            }
        }

        private void buscarLibro() {
            DatosLibro datosLibro = getDatosLibro();
            DatosAutor datosAutor = datosLibro.autor().getFirst();
            Autor autor;

            Optional<Autor> autorEnBaseDeDatos = autorRepository.findByName(datosAutor.name());

            if ( autorEnBaseDeDatos.isPresent() ){
                autor = autorEnBaseDeDatos.get();
            } else {
                autor = new Autor(datosAutor);
                autor = autorRepository.save(autor);
            }

            Libro libro = new Libro(datosLibro, autor);

            if ( !libroRepository.findByTitulo(libro.getTitulo()).isPresent() ){
                libroRepository.save(libro);
            }

            System.out.println("*************** Libro ***************");
            System.out.println(libro);
        }

        private void listarLibrosRegistrados() {

            libros = libroRepository.findAll();

            if ( !libros.isEmpty() ) {
                System.out.println("*************** Libros registrados ***************");
                libros.forEach(System.out::println);
            } else {
                System.out.println("No hay libros registrados por favor registre un libro");
            }
        }
        private void listarAutoresRegistrados() {

            autores = autorRepository.findAll();

            if ( !autores.isEmpty() ){
                System.out.println("******************* Autores registrados *******************");
                autores.forEach(System.out::println);
            } else {
                System.out.println("No hay libros registrados por favor registre un libro para ver los autores");
            }
        }
        private void listarAutoresVivos() {

            System.out.println("Por favor ingrese el año");
            int fecha = validarOpcionIngresada(()-> System.out.println("Ingrese una año valido debe ser un numero entero"));

            List<Autor> autoresVivos = autorRepository.autoresVivos(fecha);

            if ( !autoresVivos.isEmpty() ){

                System.out.println("******************* Autores vivos en " + fecha + " *******************");
                autoresVivos.forEach(System.out::println);

            } else {
                System.out.println("No hay autores registrados vivos a esa fecha");
            }
        }

        private void lisarLibrosPorIdioma() {

            System.out.println("************* Lista de idiomas *************");
            System.out.println("es - Español\n" +
                    "en - Inglés\n" +
                    "de - Aleman\n" +
                    "fr - Francés\n" +
                    "it - Italiano\n" +
                    "pt - Portugués\n" +
                    "ja - Japonés\n" +
                    "zh - Chino");

            System.out.println("Por favor ingrese un idioma. Ejemplo para Ingles ingresar 'en'");
            String idioma = teclado.nextLine();
            List<Libro> librosPorIdioma = libroRepository.librosEnIdiomaIngresado(idioma);

            if ( !librosPorIdioma.isEmpty() ){

                System.out.println("******************* Libros en el idioma: " + idioma + " *******************");
                librosPorIdioma.forEach(System.out::println);

            } else {
                System.out.println(" ---------------- No hay libros registrados en este idioma ----------------");
            }
        }

        private void top10LibrosMasDescargados() {

            List<Libro> top10Libros = libroRepository.findTop10ByOrderByNumeroDeDescargasDesc();

            top10Libros.forEach(System.out::println);
        }
        private void buscarAutorPorNombre() {

            System.out.println("Por favor ingrese el nombre del autor que desea buscar");
            String nombreAutor = teclado.nextLine();
            List<Autor> busquedaAutores = autorRepository.findByNameContainingIgnoreCase(nombreAutor);

            if ( !busquedaAutores.isEmpty() ){

                Autor autor = busquedaAutores.getFirst();
                System.out.println(autor);
            } else {
                System.out.println("------------------ No se encontraron autores ------------------");
            }

        }
        private void autoresNacidosEnUnAno() {
            System.out.println("Ingrese el año de nacimiento para buscar al autor");
            int fecha = validarOpcionIngresada(()-> System.out.println("Ingrese una año valido debe ser un numero entero"));

            List<Autor> autoresPorFechaDeNacimiento = autorRepository.findByFechaDeNacimiento(fecha);

            if ( !autoresPorFechaDeNacimiento.isEmpty() ){
                System.out.println("************************ Autores vivos en " + fecha + " ************************");
                autoresPorFechaDeNacimiento.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron autores nacidos en esta año");
            }

        }
    }
