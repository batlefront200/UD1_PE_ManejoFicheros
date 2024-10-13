
package Principal;

import java.util.ArrayList;
import java.util.Iterator;

/***
 * Clase utilizada para manejar los datos guardados en memoria
 * @author batle
 */
public class MemoryDataHandler {
    private static boolean initializated = false;
    public static ArrayList<Jugador> plrList;
    
    /***
     * Actualiza la lista de jugadores guardados en ficheros
     * @return Verdadero si se ha ejecutado correctamente
     */
    public static boolean updatePlrList() {
        ArrayList<Jugador> lista = FileHandler.cargarDatos();
        if(lista != null) {
            plrList = lista;
            return true;
        }
        return false;
    }
    
    /***
     * Obtiene todos los datos guardados en ficheros y los agrega a memoria para poder manejarlos
     * @return 
     */
    public static boolean initData() {
        if(initializated)
            return false;
        
        initializated = true;
        plrList = FileHandler.cargarDatos();
        
        // Si está vacía la inicializamos como un objeto vacío
        if(plrList == null) {
            plrList = new ArrayList<>();
        }
        return true;
    }
    
    /***
     * Añade un jugador a la lista de jugadores en memoria
     * @param plr El jugador a eliminar
     * @return Verdadero si se ha podido efectuar
     */
    public static boolean addPlayer(Jugador plr) {
        try {plrList.add(plr); return true;} catch(Exception e) {return false;}
    }
    
    /***
     * Busca un jugador con un ID específico dentro de la lista en memoria
     * @param id El id del jugador a buscar
     * @return El jugador con ese id, null en caso de que no se encuentre
     */
    public static Jugador obtainPlayerById(int id) {
        Jugador plrAux;
        Iterator it = plrList.iterator();
        while(it.hasNext()) {
            plrAux = (Jugador)it.next();
            if(plrAux.getId() == id)
                return plrAux;
        }
        return null;
    }
    
    /***
     * Elimina un jugador de la lista en memoria
     * @param plr El jugador a eliminar
     * @return Verdadero si se ha podido efectuar
     */
    public static boolean deletePlayer(Jugador plr) {
        if(plrList.contains(plr)) {
            plrList.remove(plr);
            return true;
        } else {
            return false;
        }
    }
    
    /***
     * Verifica si un id esta disponible o no para usarlo, con el fin de evitar ids repetidos
     * @param id El id a verificar
     * @return Verdadero si se puede usar ese id sin que haya repeticiones
     */
    // Verifica que un ID esté dispoinble, para evitar repeticiones/incoherencias
    public static boolean isIdAvailable(int id) {
        Iterator it = plrList.iterator();
        Jugador aux;
        while(it.hasNext()) {
            aux = (Jugador)it.next();
            if(aux.getId() == id)
                return false;
        }
        return true;
    }
    
    /***
     * Obtiene la lista de jugadores en memoria
     * @return La lista de jugadores en memoria
     */
    public static ArrayList<Jugador> getPlrList() {
       return plrList;
    }
    
}
