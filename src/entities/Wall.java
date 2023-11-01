package entities;

import processing.core.PApplet;
import processing.core.PVector;

import tools.Constants;

enum WALL_TYPE {
  HORIZONTAL,
  VERTICAL,
  ANGLED
}

public class Wall {
  PVector start, end, wall;
  float angleD, angleE;
  double minArea;
  WALL_TYPE wallType;

  PVector endToStartSegment, startToEndSegment, endToPlayerSegment, startToPlayerSegment;

  public Wall(PVector _start, PVector _end){
    start = _start;
    end = _end;
    wall = PVector.sub(end, start);
    wallType = determineWallType();
    minArea = 0.5 * PApplet.abs(PVector.sub(start, end).mag()) * Constants.PLAYER_RADIUS;
  }
    
  /**
   * Determines if the wall is a vertical, horizontal, or angled wall. Not currently used.
   * 
   * @return the type of wall.
   */
  private WALL_TYPE determineWallType(){
    if (start.x == end.x){
      return WALL_TYPE.VERTICAL;
    } else if (start.y == end.y){
      return WALL_TYPE.HORIZONTAL;
    } else {
      return WALL_TYPE.ANGLED;
    }
  }

  public void run(Player player){
    if (isHit(player)){
      player.setVel(computeNewEntityVelocity(player));
    }
  }

  private boolean isHit(Player player) {
    return inWallFOV(player) && isTriangleMinArea(player);
  }
  
  // todo: look into optimizing with multiple wall implentations and a factory class to build the wall
  /**
   * Checks if the player is in the FOV of the wall.
   * 
   * @param player: The player.
   * @return true if the player is in the field of view of the blocking side of the wall.
   */
  private boolean inWallFOV(Player player) {
    float endStartPlayerAngle = endStartPlayerAngle(player);
    float startEndPlayerAngle = startEndPlayerAngle(player);   

    if (endStartPlayerAngle < PApplet.HALF_PI + 0.3 && startEndPlayerAngle < PApplet.HALF_PI + 0.3){
      return true;
    }
    return false;
  }

  /**
   * Get the angle between the End-Start and Start-Player line segments.
   * 
   * @param player: The player.
   * @return The angle between the EndStart and StartPlayer line segments.
   */
  private float endStartPlayerAngle(Player player){
    endToStartSegment = wall;
    startToPlayerSegment = PVector.sub(start, player.getPos());
    return PApplet.PI - PVector.angleBetween(endToStartSegment, startToPlayerSegment);
  }

  /**
   * Get the angle between the Start-End and End-Player line segments.
   * 
   * @param player: The player.
   * @return The angle between the Start-End and End-Player line segments.
   */
  private float startEndPlayerAngle(Player player){
    startToEndSegment = PVector.mult(wall, -1);
    endToPlayerSegment = PVector.sub(end, player.getPos());
    return PApplet.PI - PVector.angleBetween(startToEndSegment, endToPlayerSegment);
  }

  /**
   * Checks if the triangle made with the start, end, and player locations is at it's minimum non-blocking area
   * 
   * @param player: The player.
   * @return true if the area of the triangle is equal to the non-blocking minimum.
   */
  private boolean isTriangleMinArea(Player player){
    double currentArea = 0.5 * PApplet.abs(start.x * (end.y - player.getPos().y) + end.x * (player.getPos().y - start.y) + player.getPos().x * (start.y - end.y));
    return currentArea <= minArea;
  }

  private PVector computeNewEntityVelocity(Player player){
    float angle = angleOfVelocityRelativeToWall(player);
    if (angle > 0 && angle < PApplet.PI) {
      return player.GetVel();
    }
    PVector direction = wall.copy().normalize();
    float new_magnitude = player.GetVel().dot(direction);
    return PVector.mult(direction, new_magnitude);
  }

  private float angleOfVelocityRelativeToWall(Player player){
    float angle = PApplet.atan2(player.GetVel().y, player.GetVel().x) - PApplet.atan2(wall.y, wall.x);
    if (angle < 0) { 
      angle+=PApplet.TWO_PI; 
    }
    return angle;
  }
  
  public void display(PApplet pa){
    pa.strokeWeight(2);
    // pa.line(start.x, start.y + 1, end.x, end.y + 1);
    pa.line(start.x, start.y, end.x, end.y);
    pa.strokeWeight(1);
  }

  public PVector getStart() {
    return start.copy();
  }

  public PVector getEnd() {
    return end.copy();
  }
}
