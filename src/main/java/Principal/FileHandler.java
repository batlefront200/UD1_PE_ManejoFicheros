package Principal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileHandler {

    /***
     * Función que obtiene los strings de los jugadores con sus datos y lo devuelve como un unico
     * @param playerList Lista de jugadores para obtener datos
     * @return Un string con todos los datos
     */
    public static String getPlayersString(List<Jugador> playerList) {
        String finalStr = "";
        Iterator it = playerList.iterator();
        while (it.hasNext()) {
            finalStr += it.next().toString() + "\n";
        }
        return finalStr;
    }
    
    /***
     * Crea un jugador a partir de un string dividiendo los datos por el divisor, en este caso :
     * @param plrString El string con los datos
     * @return El objeto jugador con los datos del string
     */
    public static Jugador playerBuildByString(String plrString) {
        Jugador plr = new Jugador();
        String[] playerAtributes = plrString.split(":");
        // 0:(int)id 1:name 2:(int)lifelevel 3:(int)coins 4:(int)experience
        
        try {
            plr.setId(Integer.parseInt(playerAtributes[0]));
            plr.setNick(playerAtributes[1]);
            plr.setLife_level(Integer.parseInt(playerAtributes[2]));
            plr.setCoins(Integer.parseInt(playerAtributes[3]));
            plr.setExperience(Integer.parseInt(playerAtributes[4]));
            
            return plr;
        } catch(NumberFormatException e) {
            return null;
        }
    }

    /***
     * Funcion que guarda los datos en ficheros dependiendo del tipo de guardado seleccionado
     * @param playerList La lista de jugadores a guardar
     * @return Verdadero si se han guardado correctamente
     * @throws IOException 
     */
    public static boolean guardarDatos(List<Jugador> playerList) throws IOException {
        int fileSavingType = Utils.getTipoAlmacenamiento();

        switch (fileSavingType) {

            // Usando fichero secuencial de texto (BufferReader)
            case 1:
                File fichero = new File("jugadores.txt");
                if (!fichero.exists())
                    try {
                    fichero.createNewFile();
                } catch (IOException e) {
                    return false;
                }

                try {
                    BufferedWriter fic = new BufferedWriter(new FileWriter("jugadores.txt"));
                    fic.write(getPlayersString(playerList));
                    fic.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                    return false;
                }

                break;

            // Fichero secuencial binario (DataInputStream/DataOutputStream)
            case 2:
                File fichero2 = new File("jugadores.dat");
                if (!fichero2.exists())
                    try {
                    fichero2.createNewFile();
                } catch (IOException e) {
                    return false;
                }

                try (DataOutputStream savedPlayers = new DataOutputStream(new FileOutputStream(fichero2))) {
                    for (Jugador jugador : playerList) {
                        savedPlayers.writeInt(jugador.getId());
                        savedPlayers.writeUTF(jugador.getNick());
                        savedPlayers.writeInt(jugador.getExperience());
                        savedPlayers.writeInt(jugador.getLife_level());
                        savedPlayers.writeInt(jugador.getCoins());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;

            // Fichero de objetos binario (ObjectInputStream/ObjectOutputStream)
            case 3:
                File fichero3 = new File("jugadores.dat");
                if (!fichero3.exists()) {
                    try {
                        fichero3.createNewFile();
                    } catch (IOException e) {
                        return false;
                    }
                }

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fichero3))) {
                    oos.writeObject(playerList);
                } catch (IOException e) {
                    System.out.println(e.toString());
                    return false;
                }

                break;

            // Fichero de acceso aleatorio binario (RandomAccessFile)
            case 4:
                File fichero4 = new File("jugadores.dat");
                if (!fichero4.exists())
                    try {
                    fichero4.createNewFile();
                } catch (IOException e) {
                    return false;
                }

                try (RandomAccessFile jugadoresGuardados = new RandomAccessFile(fichero4, "rw")) {
                // Limpiar el archivo antes de escribir nuevos jugadores
                jugadoresGuardados.setLength(0); // Limpia el contenido previo

                for (Jugador jugador : playerList) {
                    jugadoresGuardados.writeInt(jugador.getId());

                    // Escribir el nickname (15 caracteres, rellenar si es necesario)
                    String nick = jugador.getNick();
                    StringBuilder sb = new StringBuilder(nick);
                    while (sb.length() < 15) {
                        sb.append(" ");
                    }
                    jugadoresGuardados.writeChars(sb.toString()); // Guardar el nick en caracteres

                    // Escribir los otros valores del jugador
                    jugadoresGuardados.writeInt(jugador.getExperience());
                    jugadoresGuardados.writeInt(jugador.getLife_level());
                    jugadoresGuardados.writeInt(jugador.getCoins());
                }
            } catch (IOException e) {
                System.out.println("Error al guardar los jugadores: " + e.getMessage());
            }

                break;

            // 5- Fichero de texto XML (DOM)
            case 5:
                File fichero5 = new File("jugadores.xml");
                if (!fichero5.exists()) {
                    try {
                        fichero5.createNewFile();
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero5))) {
                            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jugadores></jugadores>");
                        }
                    } catch (IOException e) {
                        return false;
                    }
                }
                
                try {
                    File xmlFile = new File("jugadores.xml");
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);

                    Element root = doc.getDocumentElement();
                    NodeList jugadores = root.getElementsByTagName("jugador");

                    while (jugadores.getLength() > 0) {
                        Node jugador = jugadores.item(0);
                        root.removeChild(jugador);
                    }

                    Iterator it = playerList.iterator();
                    while(it.hasNext()) {
                        Jugador player = (Jugador) it.next();

                        Element nuevoEmpleado = doc.createElement("jugador");

                        Element id = doc.createElement("id");
                        id.setTextContent(player.getId() + "");
                        nuevoEmpleado.appendChild(id);

                        Element nombre = doc.createElement("nombre");
                        nombre.setTextContent(player.getNick());
                        nuevoEmpleado.appendChild(nombre);

                        Element lifel = doc.createElement("life_level");
                        lifel.setTextContent(player.getLife_level() + "");
                        nuevoEmpleado.appendChild(lifel);

                        Element coins = doc.createElement("coins");
                        coins.setTextContent(player.getCoins() + "");
                        nuevoEmpleado.appendChild(coins);

                        Element experience = doc.createElement("experience");
                        experience.setTextContent(player.getExperience() + "");
                        nuevoEmpleado.appendChild(experience);

                        root.appendChild(nuevoEmpleado);
                    }

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File("jugadores.xml"));
                    transformer.transform(source, result);  // Esto aplica los cambios al archivo

                } catch (Exception e) {
                    return false;
                }
                
                break;
            default:
                return false;
        }

        return true;
    }

    /***
     * Obtiene todos los datos guardados en archivos
     * @return Un ArrayList con todos los jugadores
     */
    public static ArrayList<Jugador> cargarDatos() {
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        
        switch(Utils.getTipoAlmacenamiento()) {
            
            // Fichero secuencial de texto (BufferedReader/BufferedWriter)
            case 1:
                File archivo1 = new File("jugadores.txt");
                    if(!archivo1.exists())
                        return null;
                
                try{
                    BufferedReader fichero = new BufferedReader(new FileReader(archivo1));
                    String linea;
                    while ((linea = fichero.readLine())!= null)
                        jugadores.add(playerBuildByString(linea));
                    fichero.close();
                }
                catch (Exception e){
                    return null;
                }
                
                break;
                
            // Fichero secuencial binario (DataInputStream/DataOutputStream)
            case 2:
                File fichero2 = new File("jugadores.dat");
                if (!fichero2.exists())
                    try {
                    fichero2.createNewFile();
                } catch (IOException e) {
                    return null;
                }

                int coins, experience, life_level, id;
                String nick;
                try (DataInputStream jugadoresCarga = new DataInputStream(new FileInputStream(fichero2))) {
                    while (true) {
                        id = jugadoresCarga.readInt();
                        nick = jugadoresCarga.readUTF();
                        experience = jugadoresCarga.readInt();
                        life_level = jugadoresCarga.readInt();
                        coins = jugadoresCarga.readInt();
                        jugadores.add(new Jugador(id, nick, experience, life_level, coins));
                    }
                } catch (EOFException e) {
                    // Fin del archivo alcanzado, se puede ignorar
                } catch (IOException e) {
                    System.out.println("No se encontró el archivo o hubo un error al cargar los jugadores.");
                }
                
                break;
            
            // Fichero de objetos binario (ObjectInputStream/ObjectOutputStream)
            case 3:
                File fichero3 = new File("jugadores.dat");
                if (!fichero3.exists()) {
                    try {
                        fichero3.createNewFile();
                    } catch (IOException e) {
                        return null;
                    }
                }

                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichero3))) {
                    jugadores = (ArrayList<Jugador>) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println(e.toString());
                    return null;
                }

                break;
            case 4:
                File fichero4 = new File("jugadores.dat");
                if (!fichero4.exists())
                    try {
                    fichero4.createNewFile();
                } catch (IOException e) {
                    return null;
                }
                
                char nick_name[] = new char[15], aux;
                int posicion = 0, coins2, life_level2, experience2, id2;

                // Verifica si el archivo está vacío
                if (fichero4.length() == 0) {
                    System.out.println("El archivo está vacío.");
                    return jugadores; // Retornar lista vacía si está vacío
                }

                // Si el archivo existe y no está vacío, proceder a leer
                try (RandomAccessFile file = new RandomAccessFile(fichero4, "r")) {
                    while (true) {
                        // Ubica el puntero del archivo en la posición actual
                        file.seek(posicion);

                        id2 = file.readInt();
                        
                        // Leer el nickname (15 caracteres, 30 bytes)
                        for (int i = 0; i < nick_name.length; i++) {
                            aux = file.readChar();
                            nick_name[i] = aux; 
                        }
                        String nick2 = new String(nick_name).trim();

                        // Leer los otros valores del jugador (4 enteros, 16 bytes en total)
                        experience2 = file.readInt();
                        life_level2 = file.readInt();
                        coins2 = file.readInt();

                        // Crear un nuevo objeto Jugador y añadirlo a la lista
                        Jugador jugador = new Jugador(id2, nick2, experience2, life_level2, coins2); // Aquí poner el id real no 1
                        jugadores.add(jugador);

                        // Actualizar la posición para el siguiente jugador
                        posicion += 46; // 30 bytes (nick) + 16 bytes (otros valores)

                        // Verifica si se ha llegado al final del archivo
                        if (file.getFilePointer() == file.length()) {
                            break; // Salir del bucle si se ha leído todo el archivo
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error al cargar los jugadores: " + e.getMessage());
                }
                
                break;
                
            case 5:
                File fichero5 = new File("jugadores.xml");
                if (!fichero5.exists()) {
                    try {
                        fichero5.createNewFile();
                        // Si se crea un archivo nuevo, inicializar una estructura básica vacía
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichero5))) {
                            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<jugadores></jugadores>");
                        }
                    } catch (IOException e) {
                        return null;
                    }
                }
                
                if (fichero5.length() == 0) {
                    return jugadores;
                }
                
                try {

                    File xmlFile = new File("jugadores.xml");

                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(xmlFile);

                    // Normalizar el documento XML
                    doc.getDocumentElement().normalize();

                    // Obtener lista de nodos "empleado"
                    NodeList nList = doc.getElementsByTagName("jugador");
                    Jugador plr;
                    
                    // Recorrer los empleados
                    for (int temp = 0; temp < nList.getLength(); temp++) {
                        Node nNode = nList.item(temp);

                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) nNode;

                            plr = new Jugador();
                            plr.setId(Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent()));
                            plr.setNick(eElement.getElementsByTagName("nombre").item(0).getTextContent());
                            plr.setLife_level(Integer.parseInt(eElement.getElementsByTagName("life_level").item(0).getTextContent()));
                            plr.setCoins(Integer.parseInt(eElement.getElementsByTagName("coins").item(0).getTextContent()));
                            plr.setExperience(Integer.parseInt(eElement.getElementsByTagName("experience").item(0).getTextContent()));

                            jugadores.add(plr);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                break;
            default:
                return null;
        }
        
        return jugadores;
    }

}