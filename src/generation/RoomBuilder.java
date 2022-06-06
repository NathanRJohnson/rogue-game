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

        Door d1 = new Door(card_direction, Room2);
        Room1.doors.put(card_direction, d1);
        // print("  Added door to " + card_direction);
        Door d2 = new Door(card_direction.getOpposite(), Room1);
        Room2.doors.put(card_direction.getOpposite(), d2);
        // print("  Added door to " + card_direction.getOpposite());
    }
}
