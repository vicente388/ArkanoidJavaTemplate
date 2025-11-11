package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Random;

public class PowerUpBrick extends Brick {

    private static final Random RNG = new Random();

    public PowerUpBrick(SpriteBatch batch, int x, int y) {
        // Usar green.png como spritesheet (assumido 4 colunas x 1 linha)
        super(batch, "green.png", 2, 1, x, y);
    }

    @Override
    public void onCollision() {
        // Ao ser destruído, marca como colidido e gera um power-up aleatório
        collided = true;
        int p = RNG.nextInt(3); // exemplo: 0 = expand paddle, 1 = slow ball, 2 = extra life
        switch (p) {
            case 0:
                System.out.println("PowerUpBrick: PowerUp 'Expand Paddle' dropado");
                break;
            case 1:
                System.out.println("PowerUpBrick: PowerUp 'Slow Ball' dropado");
                break;
            case 2:
                System.out.println("PowerUpBrick: PowerUp 'Extra Life' dropado");
                break;
            default:
                break;
        }
    }
}

