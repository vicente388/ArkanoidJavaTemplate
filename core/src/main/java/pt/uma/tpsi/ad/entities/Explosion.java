package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pt.uma.tpsi.ad.game.Animator;
import java.util.Timer;
import java.util.TimerTask;

public class Explosion {
    private final int x, y;
    private final int width, height;
    private final Animator animator;
    private boolean remove = false;

    private static final long DURATION_MS = 1000L; // 1 segundo
    private final Timer timer;

    public Explosion(SpriteBatch batch, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.animator = new Animator(batch, "explosion.png", 5, 1);
        animator.create();

        // TimerTask simplificado com lambda
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                remove = true;
            }
        };

        timer = new Timer("ExplosionTimer", true);
        timer.schedule(task, DURATION_MS);
    }

    public void render() {
        if (!remove) animator.render(x, y, width, height);
    }

    public boolean shouldRemove() {
        return remove;
    }

}
