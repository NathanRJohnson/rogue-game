package generation;

import entities.Player;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;
import tools.Constants;
import tools.Direction;

public class Map {
    int max_rooms, current_rooms;
    int grid_width;

    private Room startRoom;
    private RoomBuilder rb;
    private Player player;
    private Clock clock;

    public Map(Player p, Clock c) {
        rb = new RoomBuilder();
        max_rooms = 10;
        current_rooms = 0;
        grid_width = 5;
        player = p;
        clock = c;
        startRoom = rb.buildStartRoom(player, clock);
    }


    public void buildMapGraph(PApplet pa) {
        Room prevRoom = startRoom, newRoom;
        PVector walker = new PVector((int) pa.random(0, grid_width), (int) pa.random(0, grid_width));
        Direction card_direction = Direction.NORTH;
        int debug = 0;
        while (current_rooms <= max_rooms && debug < 4) {

            do {
                card_direction = Direction.getRandomDirection();
            } while (step(walker, card_direction) == Constants.FAIL);

            // print("trying to add new room to the " + card_direction + ":  ");
            if (!prevRoom.doorExistsAt(card_direction)) {
                // println("SUCCESS!");
                current_rooms++;
                newRoom = rb.buildRoom(player, clock);
                newRoom.set_d_num(current_rooms);
                rb.connectRooms(prevRoom, newRoom, card_direction);

                prevRoom = newRoom;
            } else {
                //  println("FAIL!");
                prevRoom = prevRoom.getRoomAt(card_direction);
            }
        }
    }

    public Room getStartRoom(){
        return startRoom;
    }

    //0 on success, -1 on fail
    public int step(PVector walker, Direction card_direction) {
        switch(card_direction) {
            case NORTH:
                if (walker.x == 0) return Constants.FAIL;
                walker.x--;
                return Constants.VALID;
            case SOUTH:
                if (walker.x == grid_width) return Constants.FAIL;
                walker.x++;
                return Constants.VALID;
            case EAST:
                if (walker.y == grid_width) return Constants.FAIL;
                walker.y++;
                return Constants.VALID;
            case WEST:
                if (walker.y == 0) return Constants.FAIL;
                walker.y--;
                return Constants.VALID;
        }
        return Constants.VALID;
    }
}
