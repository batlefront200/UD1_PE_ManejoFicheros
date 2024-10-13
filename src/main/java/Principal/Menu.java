
package Principal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Menu {
    
    /***
     * Función hecha para prevenir errores a la hora de obtener un int a través del input
     * @return el int si se ha obtenido correctamente, si no volverá  al menú
     */
    public static int safeIntGet() {
        Scanner ss = new Scanner(System.in);
        try {
            int obtainedInt = ss.nextInt();
            if(obtainedInt > -1)
                return obtainedInt;
        } catch(Exception e) {showError("The number introduced is not valid"); mainMenu();}
        mainMenu();
        return 0;
    }
    
    /***
     * Esta función limpia la consola haciendo bucles de 50 saltos de linea
     */
    public static void clear() {
        // Limpia la consola mostrando varias lineas en blanco
        for (int i = 0; i < 100; i++) { System.out.println("\n"); }
    }
    
    /***
     * Función para notificar errores por consola
     * @param errorText El texto que se mostrará en consola
     * @return Devuelve verdadero si se ha podido ejecutar correctamente
     */
    public static boolean showError(String errorText) {
        System.out.println("\n [ERROR]: "+errorText+"\n");
        try {Thread.sleep(3000);} catch(InterruptedException e) {return false;}
        return true;
    }
   
    /***
     * Función que inicializa la aplicación y el menú
     */
    public static void initApp() {
        clear();
        if(!MemoryDataHandler.initData())
            System.exit(0);
        System.out.println(" [!] Data Loaded Successfully!");
        try {Thread.sleep(2000);} catch(InterruptedException e) {System.exit(0);}
        mainMenu();
    }
    
    /***
     * Función que se ejecuta antes de salir de la aplicación para guardar todos los cambios
     */
    public static void shutDownApp() {
        ArrayList<Jugador> plrList = MemoryDataHandler.getPlrList();
        try {
            FileHandler.guardarDatos(plrList);
            System.out.println(" [!] Datos guardados con éxito");
            System.exit(0);
        } catch (IOException ex) {
            System.out.println(" [!] Ha ocurrido un error mientras se guardaban los datos");
            mainMenu();
        }
    }
    
    /***
     * Función del menú principal, controla qué quiere hacer el usuario a través de un menú y llama a las funciones correspondientes
     */
    public static void mainMenu() {
        clear();
        System.out.println(" [!] Menú principal [!]");
        System.out.println(" [1] Alta jugador");
        System.out.println(" [2] Baja de jugadores");
        System.out.println(" [3] Modificación de jugadores");
        System.out.println(" [4] Listado por código (ID)");
        System.out.println(" [5] Listado General");
        System.out.println(" [6] Configuración");
        System.out.println(" [7] Salir");
        
        System.out.print("\n [?]: ");
        try {
            String response = new Scanner(System.in).nextLine();
            int responseInt;
            responseInt = Integer.parseInt(response.trim().charAt(0)+"");
            switch(responseInt) {
                case 1:
                    altaJugador();
                    break;
                case 2:
                    bajaJugador();
                    break;
                case 3:
                    modificarJugador();
                    break;
                case 4:
                    listadoPorId();
                    break;
                case 5:
                    mostrarJugadores();
                    break;
                case 6:
                    configMenu();
                    break;
                case 7:
                    shutDownApp();
                    System.exit(0);
                    break;
                default:
                    showError("El numero introducido no es válido");
                    mainMenu();
                    break;
                    
            }
            
        } catch(NumberFormatException | StringIndexOutOfBoundsException e) { 
            showError("Verifica tu input");
            mainMenu();
        }
        
    }
    
    /***
     * Menú que ayuda al usuario a crear un jugador y añadirlo
     */
    public static void altaJugador() {
        clear();
        Jugador plr = new Jugador();
        Scanner ss = new Scanner(System.in);
        
        try {
            System.out.print("\n [ID]: ");
            plr.setId(safeIntGet());
            if(!MemoryDataHandler.isIdAvailable(plr.getId())) {
                showError("The introduced ID is already in use");
                try {Thread.sleep(2500);} catch(InterruptedException e) {  }
                mainMenu();
            }
            
            // ss.nextLine(); // Limpiamos el Scanner despues de obtener un input
            System.out.print("\n [Nombre]: ");
            plr.setNick(ss.nextLine());

            System.out.print("\n [Nivel de Vida]: ");
            plr.setLife_level(safeIntGet());

            System.out.print("\n [Monedas]: ");
            plr.setCoins(safeIntGet());

            System.out.print("\n [Experiencia]: ");
            plr.setExperience(safeIntGet());
            
        } catch (Exception e) { showError("Alguno de los valores que ha introducido son incorrectos\n  Volviendo al menu..."); mainMenu(); }
        
        // Añadir jugador
        if(MemoryDataHandler.addPlayer(plr)) {
            System.out.println("\n [+] Jugador añadido correctamente");
        } else {
            System.out.println("\n [-] No se ha podido añadir al jugador");
        }
        
        try {Thread.sleep(3000);} catch(InterruptedException e) {  }
        mainMenu();
        
    }
    
    /***
     * Menú que ayuda al usuario a eliminar un jugador en base a su ID.
     */
    public static void bajaJugador() {
        Scanner ss = new Scanner(System.in);
        System.out.print("\n [?] Introduce el ID del jugador a borrar: ");
        
        try { 
            int id = safeIntGet();
            Jugador plr = MemoryDataHandler.obtainPlayerById(id);
            if(plr != null) {
                // El jugador existe
                System.out.println(" [!] Esta seguro que quiere eliminar el siguiente jugado?");
                System.out.println(" -> "+plr.toString());
                System.out.print(" [S/n]: "); // S está en mayuscula porque es la opcion predeterminada
                // ss.nextLine(); // Limpiamos el Scanner porque acabamos de obtener un int
                char response;
                try { response = ss.nextLine().toLowerCase().charAt(0); } catch(Exception e) { response='s'; }
                if(response == 'n') {
                    showError("El usuario ha cancelado la operación de borrado");
                } else {
                    if(!MemoryDataHandler.deletePlayer(plr)) {
                        showError("No se pudo eliminar al jugador");
                    }
                    System.out.println(" [!] Jugador eliminado correctamente!");
                    try {Thread.sleep(3000);} catch(InterruptedException e) {  }
                    mainMenu();
                }
                
            } else {
                showError("No se ha encontrado el jugador con esa id");
            }
            
        } catch(NumberFormatException e) {
            showError("No se pudo obtener el numero del input");
        }
        
    }
    
    /***
     * Menú que ayuda al usuario a modificar los jugadores, si no se introduce ningún dato en algún campo, no se modificará el mismo
     */
    public static void modificarJugador() {
        Scanner ss = new Scanner(System.in);
        System.out.print("\n [?] Introduce el ID del jugador a modificar: ");
        
        try { 
            int id = safeIntGet();
            ss.nextLine(); // Se limpia el scanner despues de obtener un entero
            Jugador plr = MemoryDataHandler.obtainPlayerById(id);
            
            if(plr != null) {
                // Borramos temporalmente el jugador de memoria para modificarlo y luego añadirlo
                MemoryDataHandler.deletePlayer(plr);
                
                // El jugador existe
                System.out.println(" [?] Introduce los nuevos datos de "+plr.getNick()+" con Id "+plr.getId());
                System.out.println(" [!] Si no desea modificar algun dato introduzca un - y no se modificará");
                
                System.out.print("\n\n [ID]: ");
                int newid = safeIntGet();
                if(newid > 0 & MemoryDataHandler.isIdAvailable(newid)) { plr.setId(newid); }
                
                System.out.print("\n\n [Nombre]: ");
                String newNick = ss.nextLine();
                if(newNick.trim().length() != 0) { plr.setNick(newNick); }
                
                System.out.print("\n\n [Life Level]: ");
                int newLife = safeIntGet();
                if(newLife > 0) { plr.setLife_level(newLife); }
                
                System.out.print("\n\n [Coins]: ");
                int newCoins = safeIntGet();
                if(newCoins > 0) { plr.setCoins(newCoins); }
                
                System.out.print("\n\n [Experience]: ");
                int newExperience = safeIntGet();
                if(newExperience > 0) { plr.setExperience(newExperience); }
                
                MemoryDataHandler.addPlayer(plr);
                
            } else {
                showError("No se ha encontrado el jugador con esa id");
                mainMenu();
            }
            
        } catch(NumberFormatException e) {
            showError("No se pudo obtener el numero del input");
            mainMenu();
        }
        
    }
    
    /***
     * Muestra los datos de un Jugador dado un ID específico
     */
    public static void listadoPorId() {
        Scanner ss = new Scanner(System.in);
        System.out.print("\n [?] Introduce el ID del jugador a visualizar: ");
        
        try { 
            int id = ss.nextInt();
            ss.nextLine(); // Vaciar el scanner
            Jugador plr = MemoryDataHandler.obtainPlayerById(id);
            
            if(plr != null) {
                System.out.printf("\n %-5s %-15s %-12s %-10s %-12s\n", "[ID]", "[Nombre]", "[Nivel_Vida]", "[Monedas]", "[Experiencia]");
                System.out.printf("  %-5d %-15s %-12d %-10d %-12d\n", plr.getId(),  plr.getNick(),  plr.getLife_level(),  plr.getCoins(), plr.getExperience());

                ss.nextLine();
                mainMenu();
            } else {
                showError("No se ha encontrado el jugador con esa id");
                mainMenu();
            }
            
        } catch(NumberFormatException e) {
            showError("No se pudo obtener el numero del input");
            mainMenu();
        }
    }
    
    /***
     * Muestra un listado con todos los jugadores guardados en memoria
     */
    public static void mostrarJugadores() {
        ArrayList<Jugador> plrList = MemoryDataHandler.getPlrList();
        if(plrList.isEmpty()) {
            showError("Theres no data to show up");
            mainMenu();
        }
        
        System.out.printf("\n\n %-5s %-15s %-12s %-10s %-12s\n", "[ID]", "[Nombre]", "[Nivel_Vida]", "[Monedas]", "[Experiencia]");
        Iterator it = plrList.iterator();
        Jugador plrAux;
        while (it.hasNext()) {
            plrAux = (Jugador) it.next();
            System.out.printf("  %-5d %-15s %-12d %-10d %-12d\n", plrAux.getId(),  plrAux.getNick(),  plrAux.getLife_level(),  plrAux.getCoins(), plrAux.getExperience());
        }

        
        Scanner ss = new Scanner(System.in);
        System.out.println("\n [?] Pulsa enter para volver al menú");
        ss.nextLine();
        mainMenu();
        
        
    }
    
    /***
     * Menu de configuración que permite al usuario seleccionar el tipo de almacenamiento
     */
    public static void configMenu() {
        clear();
        System.out.println(" [!] Tipo de almacenamiento actual: "+Utils.getTipoAlmacenamiento()+" [!]");
        System.out.println(" [1] Fichero secuencial de texto"); // *
        System.out.println(" [2] Fichero secuencial binario"); // *
        System.out.println(" [3] Fichero de objetos binario"); // *
        System.out.println(" [4] Fichero de acceso aleatorio binario");
        System.out.println(" [5] Fichero de texto XML"); // *
        System.out.println(" [6] << Volver al menú ");
        
        System.out.print("\n [?]: ");
        try {
            int response = new Scanner(System.in).nextInt();
            if(response > 0 && response < 6) {
                Utils.setTipoAlmacenamiento(response);
                System.out.println(" [!] Tipo de almacenamiento cambiado a "+Utils.getTipoAlmacenamiento());
                try {Thread.sleep(3000);} catch(InterruptedException e) {System.exit(0);}
                mainMenu();
            } else if(response == 6) {
                mainMenu();
            } else {
                showError("El numero introducido no es correcto");
                configMenu();
            }
            
        } catch(NumberFormatException e) { 
            showError("Verifica tu input");
            mainMenu();
        }
    }
    
}
