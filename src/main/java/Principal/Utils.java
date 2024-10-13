
package Principal;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {
    
    /* Guia Almacenamiento
        1- Fichero secuencial de texto (BufferedReader/BufferedWriter)
        2- Fichero secuencial binario (DataInputStream/DataOutputStream)
        3- Fichero de objetos binario (ObjectInputStream/ObjectOutputStream)
        4- Fichero de acceso aleatorio binario (RandomAccessFile)
        5- Fichero de texto XML (DOM)
    */
    
    /***
     * Función que obtiene el tipo de almacenamiento que se esta usando en el archivo config
     * @return El tipo de fichero que se está usando en base a la guía
     */
    public static int getTipoAlmacenamiento() {
        File fichero = new File("config.dat");
        
        // Si el fichero no existe se crea por primera vez
        if(!fichero.exists()) {
            try {
                fichero.createNewFile();
                setTipoAlmacenamiento(1);
            } catch (IOException ex) {
                return -1; // Devuelve -1 para indicar error
            }
        }
        
        // Accede al archivo para recuperar el indexl
        try {
            RandomAccessFile file = new RandomAccessFile(fichero, "r");
            
            // Verificar si tiene contenido
            if(file.length() == 0) {
                System.out.println(" El archivo config no tiene datos");
                return -3;
            }
            
            int type = file.readInt();
            file.close();
            return type;
        } catch(Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }
    
    /***
     * Establece el tipo de almacenamiento que se va a usar
     * @param Tipo de almacenamiento a usar
     * @return Si se ha establecido el tipo correctamente
     */
    public static boolean setTipoAlmacenamiento(int tipo) {
        File fichero = new File("config.dat");
        
        // Se actualizan los datos en Memoria para que no se pierdan datos al cambiar el archivo
        MemoryDataHandler.updatePlrList();
        deleteAllStoragingFiles(); // Ahora se borran todos los archivos de datos para eliminar incoherencias y errores
        
        // Si el fichero no existe se crea por primera vez
        if(!fichero.exists()) {
            try {
                fichero.createNewFile();
            } catch (IOException ex) {
                return false; // Devuelve -1 para indicar error
            }
        }
        
        try {
            RandomAccessFile file = new RandomAccessFile(fichero, "rw");
            
            // Si el numero introducido no es [1,5]
            if(tipo > 5 || tipo < 1)
                return false;
            
            file.writeInt(tipo);
            file.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    
    /***
     * Elimina todos los archivos en el directorio actual donde se puedan almacenar datos
     * @return Si se ha podido ejecutar la operación correctamente
     */
    public static boolean deleteAllStoragingFiles() {
        File[] files = {new File("jugadores.txt"), new File("jugadores.dat"), new File("jugadores.xml")};
        
        // Se eliminan todos los archivos
        for(File currentFile : files) {
            if(currentFile.exists())
                currentFile.delete();
            if(currentFile.exists()) // Si llega hasta aquí significa que delete no esta funcionando
                return false;
        }
        
        return true;
    }
    
}
