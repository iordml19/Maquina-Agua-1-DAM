package org.maquinavending;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase abstracta que representa a un Producto.
 * @author Iordache Mihai Laurentiu
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */
public abstract class Producto{
    private TipoProducto producto;
    private double precio;
    private double peso;

    public Producto(TipoProducto producto, double precio, double peso){
        this.producto = producto;
        this.precio = precio;
        this.peso = peso;
    }

    public TipoProducto getTipoProducto(){
        return this.producto;
    }

    /**
     * Método público no instanciable utilizado para contar los productos correspondientes a una
     * determinada clase
     * @param productos El ArrayList de Productos
     * @param clase La clase contra la que compara. Debe derivar de Producto
     * @return La cantidad de productos de esa clase en cuestión.ç
     * Revisión: Iordache Mihai Laurentiu
     */
    public static int contarProductos(ArrayList<Producto> productos, Class<?> clase){
        if (productos == null) return 0;

        int cantidadProductos = 0;

        for (Producto producto: productos){
            if (producto.getClass().equals(clase)) cantidadProductos++;
        }

        return cantidadProductos;
    }

    /**
     * Método público no instanciable que dice la cantidad de productos disponibles
     * @param productos El array de productos
     * @return Un arraylist de String indicando las clases de los productos que hay dentro del array list de productos
     * Revisión: Iordache Mihai Laurentiu
     */
    public static ArrayList<String> productosDisponibles(ArrayList<Producto> productos){
        if (productos == null) return null;

        ArrayList<String> clases = new ArrayList<>();

        String nombreClases = "";

        for (Producto producto : productos) {
            String nombreClaseProducto = producto.getClass().getSimpleName();
            nombreClases += nombreClaseProducto + " ";
        }

        nombreClases = Arrays.stream(nombreClases.split(" ")).distinct().collect(Collectors.joining(" "));

        StringTokenizer stringTokenizer = new StringTokenizer(nombreClases);

        while (stringTokenizer.hasMoreTokens()){
            clases.add(stringTokenizer.nextToken());
        }

        return clases;
    }

    public double getPrecio() {
        return this.precio;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "producto=" + producto +
                ", precio=" + precio +
                ", peso=" + peso +
                '}';
    }
}
