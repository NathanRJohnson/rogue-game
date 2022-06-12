package generation;

import entities.Player;
import tools.Clock;
import tools.Direction;

public class RoomBuilder {
    protected RoomBuilder(){}

    Room buildRoom(Player p, Clock c) {
        return new Room(p, c);
    }

    Room buildStartRoom(Player p, Clock c) {
        return new Room(p, c);
    }

    void connectRooms(Room Room1, Room Room2, Direction card_direction){
        Room1.addDoor(card_direction, Room2);
        Room2.addDoor(card_direction.getOpposite(), Room1);
    }
}
