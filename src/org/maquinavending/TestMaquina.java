package org.maquinavending;

import org.maquinavending.exceptions.ImporteExactoException;
import org.maquinavending.exceptions.ImporteInsuficienteException;
import org.maquinavending.exceptions.MaquinaVaciaException;
import org.maquinavending.exceptions.MonedaNoValidaException;
import org.maquinavending.utilidad.Command;
import org.maquinavending.utilidad.Switcher;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase principal
 * @author Oscar García Gómez
 * @author Iordache Mihai Laurentiu
 * @version 1.0
 * Revisión: Iordache Mihai Laurentiu
 */

public final class TestMaquina {
    public static final int COMPRAR_PRODUCTO = 1;
    public static final int CAMBIO_DISPONIBLE = 2;
    public static final int SALIR = 3;
    public static final int INFO = 4;
    private Scanner sc = new Scanner(System.in);
    private Maquina maquina;
    private int opcion;
    private int productoAComprar;
    private Switcher switcher = new Switcher();

    public static void main(String[] args) {
        ArrayList<BotellaAgua> botellaAguas = new ArrayList<>();
        TestMaquina testMaquina = new TestMaquina();

        for (int i = 0; i < 10; i++){
            botellaAguas.add(new BotellaAgua(TipoProducto.Botella_Agua, 1.00, 1.5));
        }

        testMaquina.maquina = new Maquina(new ArrayList<>(botellaAguas));
        testMaquina.opciones();

        System.out.println();
        testMaquina.opcion = testMaquina.sc.nextInt();

        testMaquina.menu(testMaquina.opcion);
    }


    /**
     * Las opciones disponibles para el usuario
     * @author Iordache Mihai Laurentiu
     */
    public void opciones(){
        System.out.print("\nBienvenido al menú de nuestra máquina de vending. Elija una opción\n\n1. Comprar un producto\n" +
                "2. Cambio disponible" +
                " de la máquina\n3. Salir\n4. Información");
    }

    /**
     * Menú de la aplicación
     * @param opcion La opción del usuario
     * @author Oscar García Gómez
     * Revisión: Iordache Mihai Laurentiu
     */
    public void menu(int opcion){

        switch (opcion){
            case COMPRAR_PRODUCTO:{

                try {
                    this.maquina.productosDisponibles();
                    this.productoAComprar = this.sc.nextInt();
                } catch (ClassNotFoundException e) {
                    System.out.println("La clase no ha sido encontrada");
                    break;
                } catch (MaquinaVaciaException e) {
                    System.out.println(e.getMessage());
                    break;
                }

                iniciarMaquina();

                break;
            }
            case CAMBIO_DISPONIBLE: {
                System.out.println("\nCambio disponible: " + Math.round(maquina.cambioDisponible() * 10.0) / 10.0 + "€");
                this.opciones();

                System.out.println();

                this.opcion = this.sc.nextInt();
                menu(this.opcion);
                break;
            }
            case SALIR : {
                System.out.println("\nGracias por visitarnos! Hasta la próxima!");
                break;
            }
            case INFO: {
                System.out.println("\nHola! Bienvenid@ a nuestra máquina de vending. Aceptamos monedas de: 0.10€, 0.20€, 0.50€, 1€ y 2€");
                System.out.println("Si introduces una moneda incorrecta, se cancelerá tu compra! Que disfrutes de tu compra :)");
                break;
            }
            default: {
                System.out.println("\nOpción Inexistente. Saliendo...");
                break;
            }
        }
    }

    /**
     * Método utilizado para iniciar la máquina
     * @author Iordache Mihai Laurentiu
     * Revisión: Iordache Mihai Laurentiu
     */
    public void iniciarMaquina() {

            if (this.maquina.getProductosDisponibles().containsKey(this.productoAComprar)) {
                switcher.addCaseCommand(productoAComprar, new Command() {
                    private TestMaquina testMaquina = new TestMaquina();

                    /**
                     * Método sobreescrito que se encarga de recoger las monedas del usuario y
                     * de llamar al método para realizar la compra
                     */
                    @Override
                    public void execute() {
                        maquina.productoSeleccionado(maquina.getProductosDisponibles().get(productoAComprar));
                        System.out.println("\nHa elegido comprar: " + (maquina.getProductoAComprar().getClass().getSimpleName().contains("Botella") ?
                                maquina.nombreDelProducto(maquina.getProductoAComprar().getClass().getSimpleName(), "de", true) :
                                maquina.nombreDelProducto(maquina.getProductoAComprar().getClass().getSimpleName(), "", false)));

                        // Comienzo de la compra
                        System.out.println("Precio del producto: " + maquina.getProductoAComprar().getPrecio() + "€");
                        System.out.println("\nIntroduzca el importe. Introduzca '0' cuando finalice de introducir las monedas:");


                        ArrayList<Moneda> monedas = new ArrayList<>();

                        double moneda = sc.nextDouble();
                        if (moneda != 0) monedas.add(new Moneda(moneda));

                        while (!(moneda == 0)) {
                            System.out.println("Introduzca la moneda. Introduzca '0' para finalizar");
                            moneda = sc.nextDouble();
                            if (moneda != 0) monedas.add(new Moneda(moneda));
                        }

                        try {
                            maquina.comprar(maquina.getProductoAComprar(), monedas);
                            if (maquina.isCompraFinalizada()) {
                                System.out.println();
                                System.out.println("Desea Seguir Comprando? ");

                                String opcion = testMaquina.sc.next();
                                if (maquina.deseaSeguirComprando(opcion)) {
                                    testMaquina.opciones();
                                    System.out.println();
                                    testMaquina.opcion = testMaquina.sc.nextInt();
                                    menu(testMaquina.opcion);
                                } else {
                                    System.out.println("\nSaliendo...\n\nGracias! Hasta la próxima!");
                                }

                            }
                        } catch (MonedaNoValidaException | ImporteInsuficienteException | ImporteExactoException e) {
                            System.out.println(e.getMessage());
                        }
                    }

                });

                switcher.on(productoAComprar);

            } else {
                while (!this.maquina.getProductosDisponibles().containsKey(productoAComprar)) {
                    System.out.println("\nEl producto que ha indicado no se encuentra en la lista de productos disponibles. Vuelva a elegir");
                    this.menu(this.opcion);
                }
            }
    }
}
