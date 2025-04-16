package server.model;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private final int durationInSeconds; // Durée totale de la partie en secondes
    private int remainingTime; // Temps restant
    private Timer timer;
    private Runnable onTimeUp; // Action à exécuter lorsque le temps est écoulé

    public GameTimer(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
        this.remainingTime = durationInSeconds;
    }

    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (remainingTime > 0) {
                    remainingTime--;

                } else {
                    timer.cancel();
                    if (onTimeUp != null) {
                        onTimeUp.run();
                    }
                }
            }
        }, 0, 1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}