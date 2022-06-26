package generation;

import entities.Player;
import processing.core.PApplet;
import tools.Clock;
import tools.Direction;

public class RoomBuilder {
    protected RoomBuilder(){}

    Room buildRoom(Player p, Clock c, PApplet pa) {
        Room r = new Room(p, c);
        r.generateEnemySet(pa);
        return r;
    }

    Room buildStartRoom(Player p, Clock c) {
        return new Room(p, c);
    }

    void connectRooms(Room Room1, Room Room2, Direction card_direction){
        Room1.addDoor(card_direction, Room2, true);
        Room2.addDoor(card_direction.getOpposite(), Room1, true);
    }
}
