package generation;

import entities.Enemy;
import entities.Player;
import projectiles.Projectile;
import processing.core.PApplet;
import tools.Clock;
import tools.Constants;
import tools.Direction;
import entities.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Room {
    private ArrayList<Enemy> enemies;
    private Player p;
    private Clock clock;
    protected HashMap<Direction, Door> doors = new HashMap<Direction, Door>();
    private int d_room_num;

    private float north_boundary, south_boundary, east_boundary, west_boundary;

    public Room(Player _p, Clock _c) {
        enemies = new ArrayList<Enemy>();
        p = _p;
        clock = _c;

        north_boundary = 15;
        south_boundary = Constants.HEIGHT - 15;
        east_boundary = 15;
        west_boundary = Constants.WIDTH - 15;

    }

    public void initRoom(PApplet pa) {

    }

    public void generateEnemySet(PApplet pa){
        double r = Math.random();
        if (r < 0.33) {
            generate_one(pa);
        } else if (r < 0.66) {
            generate_two(pa);
        } else {
            generate_three(pa);
        }
    }

    public void addDoor(Direction d, Room nextRoom){
        doors.put(d, new Door(d, nextRoom));
    }

    public void addDoor(Direction d, Room nextRoom, boolean isLocked){
        doors.put(d, new Door(d, nextRoom, isLocked));
    }


    Room getRoomAt(Direction card_direction){
        return doors.get(card_direction).getNextRoom();
    }

    Boolean doorExistsAt(Direction card_direction){
        return doors.get(card_direction) != null;
    }

    void set_d_num(int n){
        d_room_num = n;
    }

    public void applyBoundaries(Entity entity) {
        //quick spike to see what direction velo is moving in
        if (entity.getPos().y <= north_boundary && entity.GetVel().y < 0) {
            entity.setYVel(0);
        }
        if (entity.getPos().y >= south_boundary && entity.GetVel().y > 0) {
            entity.setYVel(0);
        }
        if (entity.getPos().x <= east_boundary && entity.GetVel().x < 0) {
            entity.setXVel(0);
        }
        if (entity.getPos().x >= west_boundary && entity.GetVel().x > 0) {
            entity.setXVel(0);
        }

    }

    public void unlockDoors(){
        for (Door d : doors.values()){
            d.setLocked(false);
        }
    }

    //display and update
    public void run(PApplet pa) {
        pa.background(150);
        pa.text(d_room_num, 50, 50);
        Iterator<Enemy> it = enemies.iterator();

        while (it.hasNext()) {
            Enemy e = (Enemy) it.next();
            e.run(p, enemies, clock, pa);
            applyBoundaries(e);
            if (e.isDead()) {
                it.remove();
            }
        }

        if (enemies.isEmpty()){
            unlockDoors();
        }

//        Player's projectiles
        for (Projectile p : p.getLauncher().getProjectiles()) {
            for (Enemy e : enemies) {
                if (e.collides(p)) {
                    e.hit(p);
                }
            }
        }

        for (Door d: doors.values()){
            d.display(pa);
        }
    }

    private void generate_one(PApplet pa){
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
    }
    private void generate_two(PApplet pa){
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
    }
    private void generate_three(PApplet pa){
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
        enemies.add(new Enemy(pa));
    }

    public int getRoomNum(){
        return d_room_num;
    }

    public ArrayList<Enemy> getEnemies(){
        return enemies;
    }

    public HashMap<Direction, Door> getDoorsMap(){
        return doors;
    }
}
