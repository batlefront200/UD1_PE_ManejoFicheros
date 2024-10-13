
package Principal;

import java.io.Serializable;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, experience, life_level, coins;
    private String nick;
    
    public Jugador() {
        // Cambiar esto luego para poner asignación de id automática
        this.id = 0;
        this.experience = 0;
        this.life_level = 0;
        this.coins = 100;
        this.nick = "Unknown";
    }
    
    public Jugador(int id, String nick, int experience, int life_level, int coins) {
        this.id = id;
        this.nick = nick;
        this.experience = experience;
        this.life_level = life_level;
        this.coins = coins;
    }
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the experience
     */
    public int getExperience() {
        return experience;
    }

    /**
     * @param experience the experience to set
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /**
     * @return the life_level
     */
    public int getLife_level() {
        return life_level;
    }

    /**
     * @param life_level the life_level to set
     */
    public void setLife_level(int life_level) {
        this.life_level = life_level;
    }

    /**
     * @return the coins
     */
    public int getCoins() {
        return coins;
    }

    /**
     * @param coins the coins to set
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * @return the nick
     */
    public String getNick() {
        return nick;
    }

    /**
     * @param nick the nick to set
     */
    public void setNick(String nick) {
        this.nick = nick;
    }
    
    /***
     * Obtiene todos los datos del jugador en forma de String separado por dos puntos :
     * @return El string con los datos del jugador
     */
    @Override
    public String toString() {
        return this.getId()+":"+this.getNick()+":"+this.getLife_level()+":"+this.getCoins()+":"+this.getExperience();
    }
    
}
