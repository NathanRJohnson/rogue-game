package tools;

public class Clock {
    private static Clock clock;
    private double time;
    private double incrememnt;

    private Clock() {
        time = 0;
        incrememnt = 0.01;
    }

    public static Clock getInstance() {
      if (clock == null) {
        clock = new Clock();
      }

      return clock;
    }

    public void run() {
        time += incrememnt;
    }

    public double getTime() {
        return time;
    }
}
