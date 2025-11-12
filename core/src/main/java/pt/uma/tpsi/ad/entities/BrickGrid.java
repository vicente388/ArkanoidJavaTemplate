package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BrickGrid {

    private final List<Brick> bricks = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();
    private final SpriteBatch batch;
    private final Player player;

    private static final int ROWS = 4, COLS = 20;

    public BrickGrid(SpriteBatch batch, Player player) {
        this.batch = batch;
        this.player = player;
        createBricks();
    }

    private void createBricks() {
        Random rng = new Random();
        int brickW = 32, brickH = 16, spacingX = 20, spacingY = 13;
        int totalWidth = COLS * (brickW + spacingX) - spacingX;
        int startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        int startY = Gdx.graphics.getHeight() - 100;


        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Brick b;
                float p = rng.nextFloat();
                if (p < 0.5f) b = new NormalBrick(batch, 0, 0);
                else if (p < 0.8f) b = new StrongBrick(batch, 0, 0);

                else if (p < 0.9f) b = new IndestructibleBrick(batch, 0, 0);
                else b = new PowerUpBrick(batch, 0, 0);

                int x = startX + col * (brickW + spacingX);
                int y = startY - row * (brickH + spacingY);
                b.posX = x; b.posY = y;
                b.getBoundingBox().setPosition(x, y);
                bricks.add(b);
            }
        }
    }

    public void update(float delta, Ball ball) {
        for (int i = bricks.size() - 1; i >= 0; i--) {
            Brick brick = bricks.get(i);
            if (brick.getBoundingBox().overlaps(ball.getBoundingBox())) {
                brick.onCollision();
                ball.reverseYDirection();
                ball.resolveCollisionWith(brick.getBoundingBox());

                if (brick.isCollided()) {
                    explosions.add(new Explosion(batch, brick.posX, brick.posY,
                        (int) brick.getBoundingBox().width, (int) brick.getBoundingBox().height));
                    if (player != null) player.addScore(brick.getPoints());
                    bricks.remove(i);
                }
            }
        }

        explosions.forEach(e -> e.update(delta));
        explosions.removeIf(Explosion::shouldRemove);
    }

    public void render() {
        bricks.stream().filter(b -> !b.isCollided()).forEach(Brick::render);
        explosions.forEach(Explosion::render);
    }

    public boolean isCleared() {
        return bricks.stream().noneMatch(b -> !(b instanceof IndestructibleBrick) && !b.isCollided());
    }
}
