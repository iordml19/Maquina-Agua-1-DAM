package org.maquinavending;

import org.maquinavending.exceptions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ListIterator;

/**
 * Clase que representa a la máquina de vending.
 * @author Iordache Mihai Laurentiu
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */

public final class Maquina implements Comprable {
    private ArrayList<Producto> productos;
    private ArrayList<Moneda> monedasCambio = new ArrayList<>(25);
    private HashMap<Integer, String> productosDisponibles = new HashMap<>();
    private Producto productoAComprar;
    private double cambioAdevolver;
    private boolean compraFinalizada = false;

    /*
     * Bloque de inicialización. Creación de las monedas de cambio
     */
    {
        ArrayList<Moneda> monedas = new ArrayList<>();

        for (int i = 0; i < 10; i++){
            monedas.add(new Moneda(0.10));
        }

        for (int i = 0; i < 5; i++){
            monedas.add(new Moneda(0.20));
        }


        for (int i = 0; i < 4; i++){
            monedas.add(new Moneda(0.50));
        }

        for (int i = 0; i < 6; i++){
            monedas.add(new Moneda(1.00));
        }

        this.monedasCambio.addAll(monedas);

    }

    /**
     * @param productos Los productos a introducir en la máquina
     */
    public Maquina(ArrayList<Producto> productos){
        this.productos = new ArrayList<>();
        this.productos.addAll(productos);
        //this.productos.add(new BebidaEnergetica(TipoProducto.Bebida_Energetica, 1.50, 1.50));
        //this.productos.add(new Snack(TipoProducto.Snack, 1.5, 0.5));
        //this.productos.add(new BebidaEstimulante(TipoProducto.Bebida_Estimulante, 2, 1.00));
    }

    /**
     * Método sobreescrito encargado de la compra del producto
     * @param producto El producto a comprar.
     * @param monedas Las monedas del comprador.
     * @throws MonedaNoValidaException Lanzada en caso de que la moneda sea una de las no aceptadas por la máquina
     * @throws ImporteInsuficienteException Lanzada en caso de que el importe del usuario no sea suficiente
     * @throws ImporteExactoException Lanzada en caso de que la máquina no disponga de cambio
     * Revisión: Iordache Mihai Laurentiu
     */
    @Override
    public void comprar(Producto producto, ArrayList<Moneda> monedas) throws MonedaNoValidaException, ImporteInsuficienteException, ImporteExactoException{
        boolean sonMonedasValidas = this.sonMonedasValidas(monedas);

        if(sonMonedasValidas){
            boolean puedeComprar = true;

            // Efectuar compra
            double cantidadIntroducida = this.contarMonedasIntroducidas(monedas);

            if (cantidadIntroducida < producto.getPrecio()) puedeComprar = false;

            if (puedeComprar){

                if (cantidadIntroducida >= producto.getPrecio()){
                    this.cambioAdevolver = cantidadIntroducida - producto.getPrecio();
                    this.cambioAdevolver = Math.round(this.cambioAdevolver * 10.0) / 10.0;

                    // && !(cantidadIntroducida == producto.getPrecio() && this.cantidadDineroCambio() < cantidadIntroducida)
                    if (this.cambioAdevolver <= this.cantidadDineroCambio() ){

                        try{
                            double cantidad = this.devolverCambio();
                            System.out.println("\nCompra realizada con éxito. Producto adquirido: " + this.nombreDelProducto(producto.getClass().getSimpleName(), "de", true));
                            System.out.println("\nCambio: " + String.format("%.2f", cantidad) + "€");

                            this.productos.remove(producto);
                            this.compraFinalizada = true;
                        }catch(NoHayMonedasCambioException e){
                            this.compraFinalizada = false;
                            System.out.println(e.getMessage() + " " + cantidadIntroducida + " €");
                        }

                    }else{
                        throw new ImporteExactoException("\nLo sentimos pero la máquina no dispone de cambio suficiente. Como consecuencia," +
                                " se ha cancelado la compra y se le ha integrado el importe introducido\nReintegro: " + cantidadIntroducida + "€");
                    }

                }

            }else{
                throw new ImporteInsuficienteException("\nDebe introducir la cantidad solicitada para adquirir el producto." +
                        "\n\nSe le solicita introducir: " + producto.getPrecio() + "€. Cantidad introducida: " + cantidadIntroducida + "€");
            }

        }else{
            throw new MonedaNoValidaException("\nHa introducido monedas no validas. Su dinero ha sido devuelto. Cantidad: " +
                    this.contarMonedasIntroducidas(monedas) + "€.\nMonedas Aceptadas: 0.10€, 0.20€, 0.50€. 1€ y 2€");
        }
    }

    /**
     * Método público instanciable que dice los productos disponibles para la venta
     * @throws ClassNotFoundException Lanzada si la clase no ha sido encontrada
     * @throws MaquinaVaciaException Lanzada en caso de que la máquina no disponga de productos para vender
     * Revisión: Iordache Mihai Laurentiu
     */
    public void productosDisponibles() throws ClassNotFoundException, MaquinaVaciaException {
        boolean noHayProductos = this.existenProductos();

        /*
         * Comprueba si no hay productos. Si los hay los muestra: El tipo y la cantidad (opcional)
         */

        if (!noHayProductos){
            System.out.println("\nProductos disponibles:\n");

            int indiceProducto = 0;

            for(String clases : Producto.productosDisponibles(this.productos)){
                if (Producto.contarProductos(this.productos, Class.forName("org.maquinavending." + clases)) > 0){
                    indiceProducto++;

                    /*
                        Para hacer comprobaciones con respecto a una clase ejecutar lo siguiente:
                        (clases.equals("aqui va el nombre de la clase"))

                        La cantidad no es relevante para el cliente, si no hay, no se muestra
                         ". Cantidad: " + Producto.contarProductos(this.productos, Class.forName("org.maquinavending." + clases))
                     */

                    System.out.println(indiceProducto + ".- " +  (clases.contains("Botella") ? this.nombreDelProducto(clases, "de", true) :
                            this.nombreDelProducto(clases, "", false))
                    + " de " + this.getPesoProducto(clases) + " " + this.getUnidadProducto(clases) + ". Precio: " + this.getPrecioProducto(clases) + "€");

                    this.productosDisponibles.put(indiceProducto, clases);
                }
            }
        }else{
            throw new MaquinaVaciaException("\nLo sentimos! En este momento no disponemos de ningún producto a la venta!");
        }
    }

    /**
     * Método privado instanciable que dice el nombre del Producto. NOTA: Este método no funciona correctamente
     * con clases compuestas por más de dos palabras.
     * @param claseDelProducto El nombre de la clase del producto
     * @param preposicion La preposición que tendrá la cadena. Utilizada para concatenar palabras
     * @param incluirEspacios true o false indicando si se incluyen o no espacios entre la preposición y la siguiente palabra
     * @return El resultado con el nombre del producto o el nombre del producto original ya que este no tenia dos palabras
     * Revisión: Iordache Mihai Laurentiu
     */

    protected String nombreDelProducto(String claseDelProducto, String preposicion, boolean incluirEspacios){

        for (int i = 1; i < claseDelProducto.length(); i++){

            if (Character.isUpperCase(claseDelProducto.charAt(i))){
                int indiceSiguientePalabra = claseDelProducto.indexOf(claseDelProducto.charAt(i));

                if(incluirEspacios){
                    return claseDelProducto.substring(0, indiceSiguientePalabra) +
                          " " + preposicion + " " + (claseDelProducto.substring(indiceSiguientePalabra));
                }

                return claseDelProducto.substring(0, indiceSiguientePalabra) + preposicion + " " + (claseDelProducto.substring(indiceSiguientePalabra));
            }
        }

        return claseDelProducto;
    }

    /**
     * Método privado que devuelve el peso del producto en cuestión
     * @param clase La clase del producto
     * @return El peso del producto
     */
    private double getPesoProducto(String clase){
        for (Producto producto : this.productos) {
            if (producto.getClass().getSimpleName().equals(clase)) return producto.getPeso();
        }

        return 0;
    }

    /**
     * Método privado que devuelve la unidad del producto
     * @param clase La clase del producto
     * @return La unidad del producto dependiendo de si Es Botella o Bebida u otra cosa.
     */
    private String getUnidadProducto(String clase){
        for (Producto producto : this.productos) {
            if (producto.getClass().getSimpleName().equals(clase) &&
                    (producto.getClass().getSimpleName().contains("Botella") || producto.getClass().getSimpleName().contains("Bebida"))) return "L";
        }

        return "KG";
    }

    /**
     * Méotodo privado que devuelve el precio del producto
     * @param clase La clase del producto
     * @return El precio del producto
     */
    private double getPrecioProducto(String clase){
        for (Producto producto : this.productos) {
            if (producto.getClass().getSimpleName().equals(clase)) return producto.getPrecio();
        }

        return 0;
    }

    public HashMap<Integer, String> getProductosDisponibles() {
        return this.productosDisponibles;
    }

    /**
     * Método protegido que selecciona el producto a comprar
     * @param productoAComprar La clase del producto a comprar
     */
    protected void productoSeleccionado(String productoAComprar){
        for (Producto producto : this.productos){
            if (producto.getClass().getSimpleName().equals(productoAComprar)){
                this.productoAComprar = producto;
                break;
            }
        }
    }

    protected Producto getProductoAComprar(){
        return this.productoAComprar;
    }

    /**
     * Método privado instanciable que verifica si las monedas del comprador son vçalidas
     * @param monedas Las monedas del comprador
     * @return true o false indicando si son validas o no
     */
    private boolean sonMonedasValidas(ArrayList<Moneda> monedas){

        for (Moneda moneda : monedas) {
            if (moneda.getValor() != 0.10 && moneda.getValor() != 0.20
                && moneda.getValor() != 0.50  && moneda.getValor() != 1.00
                && moneda.getValor() != 2.00) return false;
        }

        return true;
    }

    /**
     * Método que cuenta la cantidad de dinero introducida por el comprador
     * @param monedas Las monedas del usuario
     * @return La cantidad de dinero introducida por el comprador
     */
    private double contarMonedasIntroducidas(ArrayList<Moneda> monedas){
        double cantidad = 0;

        for (Moneda moneda : monedas) {
            cantidad += moneda.getValor();
        }

        return Math.round(cantidad * 100.0) / 100.0;
    }

    /**
     * Método público instanciable que indica si el usuario desea seguir comprando
     * @param opcion La opción del usuario. Si, para seguir comprando, no u otra cosa para cancelar
     * @return true o false si desea seguir comprando
     */
    public boolean deseaSeguirComprando(String opcion){
        boolean quiereSeguirComprando = true;

        if (!"si".equals(opcion.toLowerCase()))  quiereSeguirComprando = false;

        return quiereSeguirComprando;
    }

    /**
     * Método privado que devuelve la cantidad de dinero que hay para cambio
     * @return La cantidad de dinero para cambio
     */
    private double cantidadDineroCambio(){
        double cantidad = 0;

        for (Moneda moneda : this.monedasCambio) {
            cantidad += moneda.getValor();
        }

        return cantidad;
    }

    /**
     * Método público que muestra el cambio disponible
     * @return El cambio disponible de la máquina
     */
    public double cambioDisponible(){
        return this.cantidadDineroCambio();
    }

    /**
     * Método estático que suma los valores del subconjunto de las monedas de cambio
     * @param array El array con los subconjuntos
     * @return La suma de los valores del subconjunto
     */
    private static double sumarValorMonedasDisponibles(ArrayList<Double> array) {
        double contador = 0;

        for (Double val : array) {
            contador += val;
        }

        return contador;
    }

    /**
     * Método privado que se encarga de devolver el cambio al comprador
     * @return El cambio del comprador
     * Revisión: Iordache Mihai Laurentiu
     */
    private double devolverCambio() throws NoHayMonedasCambioException{
        double cantidadADevolverAcumulada = 0;

        ArrayList<Double> valoresMonedas = new ArrayList<>();

        for (Moneda value : this.monedasCambio) {
            valoresMonedas.add(value.getValor());
        }

        ArrayList<Double> monedasCambiar = monedasCambioUsuario(valoresMonedas, this.cambioAdevolver);

        if (this.cambioAdevolver >= 1){
            Collections.reverse(monedasCambiar);
        }

        // CASOS ESPECIALES
        if (monedasCambiar.isEmpty() && (this.cambioAdevolver == 0.30 || this.cambioAdevolver == 0.90)){
            Moneda monedaCambio = null;

            ListIterator<Moneda> iteradorMonedasCambio = this.monedasCambio.listIterator();

            while (iteradorMonedasCambio.hasNext()){
                Moneda m = iteradorMonedasCambio.next();
                if (this.cambioAdevolver == 0.30){
                    if (m.getValor() == 0.20 || m.getValor() == 0.10){
                        monedaCambio = m;
                        if (this.cambioAdevolver != Math.round(cantidadADevolverAcumulada * 10.0) / 10.0){
                            cantidadADevolverAcumulada += monedaCambio.getValor();
                            iteradorMonedasCambio.remove();
                        }
                    }
                }else{
                    if (m.getValor() == 0.20 || m.getValor() == 0.10 || m.getValor() == 0.50){
                        monedaCambio = m;
                        if (this.cambioAdevolver != Math.round(cantidadADevolverAcumulada * 10.0) / 10.0){
                            cantidadADevolverAcumulada += monedaCambio.getValor();
                            iteradorMonedasCambio.remove();
                        }
                    }
                }
            }
        }

        // CASOS NORMALES
        for (Double d : monedasCambiar) {

            if (Math.round(cantidadADevolverAcumulada * 10.0) / 10.0 != this.cambioAdevolver){
                cantidadADevolverAcumulada += d;

                Moneda m = null;

                for (Moneda moneda : this.monedasCambio) {
                    if (moneda.getValor() == d) m = moneda;
                }

               if (m != null ) this.monedasCambio.remove(m);
            }

        }

        if (cantidadADevolverAcumulada != this.cambioAdevolver && Math.round(cantidadADevolverAcumulada * 10.0) / 10.0 == 0.00){
            throw new NoHayMonedasCambioException("\nLo sentimos pero su compra ha sido cancelada debido a que la máquina no dispone de las " +
                    " monedas necesarias para ofrecerle cambio a pesar de existir cambio. Se le ha reintegrado el importe:");
        }

        return Math.round(cantidadADevolverAcumulada * 10.0) / 10.0;
    }

    /**
     * Método estático que busca los subconjuntos del cambio del usuario
     * @param arr El array en el que buscar subconjuntos
     * @param suma La suma a buscar, en este caso, el cambio del usuario
     * @return Un arraylist con los subconjuntos
     */
    private static ArrayList<Double> monedasCambioUsuario(ArrayList<Double> arr, double suma) {
        ArrayList<Double> ar = new ArrayList<>();

        for (int i = 0; i < arr.size(); i++) {
            ArrayList<Double> temp = new ArrayList<>();

            for (int j = i; j < arr.size(); j++) {
                temp.add(arr.get(j));
                if (sumarValorMonedasDisponibles(temp) == suma) ar.addAll(temp);
            }
        }

        return ar;
    }

    /**
     * Muestra los productos disponibles
     * @return Un array list con los productos disponibles
     */
    public ArrayList<Producto> getProductos() {
        return this.productos;
    }

    /**
     * Dice si existen o no productos
     * @return true si no existen, false si existen
     */
    private boolean existenProductos(){
        return this.productos.isEmpty();
    }

    /**
     * Dice si la compra está finalizada
     * @return true o false en función del valor de la propiedad
     */
    public boolean isCompraFinalizada() {
        return compraFinalizada;
    }
}
