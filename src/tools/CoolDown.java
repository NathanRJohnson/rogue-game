package tools;

public class CoolDown {
  private double start;
  private double duration;
  private State state;

  public CoolDown(double duration) {
    this.duration = duration;
    this.state = State.NOT_STARTED;
  }

  public void start() {
    start = Clock.getInstance().getTime();
    state = State.STARTED;
  }

  public boolean hasElapsed() {
    if (state == State.STARTED && Clock.getInstance().getTime() >= start + duration) {
      state = State.FINISHED;
      return true;
    }
    return false;
  }

  public void reset() {
    start = 0;
    state = State.NOT_STARTED;
  }

  public boolean isCoolingDown() {
    return hasBeenStarted() && !hasElapsed();
  }

  public boolean hasBeenStarted() {
    return state != State.NOT_STARTED;
  }

  private enum State {
    NOT_STARTED,
    STARTED,
    FINISHED
  } 
}
