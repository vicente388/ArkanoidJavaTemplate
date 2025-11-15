package pt.uma.tpsi.ad.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import pt.uma.tpsi.ad.entities.Ball;
import pt.uma.tpsi.ad.entities.BrickGrid;
import pt.uma.tpsi.ad.entities.Player;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private BackgroundManagement backgroundManagement;
    private BitmapFont font;
    private Player player;
    private Ball ball;
    private BrickGrid brickGrid;

    private boolean gameOver = false; // controla o fim do jogo
    private boolean victory = false; // controla vitória


    @Override
    public void create() {
        Gdx.graphics.setWindowedMode(1280, 720);
        batch = new SpriteBatch();

        backgroundManagement = new BackgroundManagement(batch);
        player = new Player(batch);
        player.create();

        ball = new Ball(batch);
        ball.create();
        brickGrid = new BrickGrid(batch, player);

        // 🔹 Usa a fonte padrão do LibGDX — sem precisar de ficheiros externos
        font = new BitmapFont();
    }

    @Override
    public void render() {
        batch.begin();
        backgroundManagement.render();
        player.render();
        ball.render();
        // Atualiza bricks/explosões e processa colisões dentro do BrickGrid
        brickGrid.update(ball);
        // depois desenha a grelha atualizada
        brickGrid.render();

        if (ball.getBoundingBox().overlaps(player.getBoundingBox())) {
            // comportamento simples: a bola reflete verticalmente sem lógica de zonas
            ball.adjustDirectionOnContact(player.getBoundingBox());
            // empurra a bola para fora do paddle para evitar múltiplas deteções seguidas
            ball.resolveCollisionWith(player.getBoundingBox());
        }

        // Se o jogo ainda não acabou, verifica condições de fim
        if (!gameOver) {
            if (ball.posY() < 0) { // se a bola sair por baixo do ecrã
                gameOver = true;
                victory = false;
            } else if (brickGrid.isCleared()) { // vitória
                gameOver = true;
                victory = true;
            }
        }

        // Se o jogo acabou, mostra texto apropriado (vitória ou derrota)
        if (gameOver) {
            font.getData().setScale(3); // aumenta o tamanho do texto
            if (victory) {
                font.draw(batch, "YOU WIN!",
                    Gdx.graphics.getWidth() / 2f - 150,
                    Gdx.graphics.getHeight() / 2f);
            } else {
                font.draw(batch, "GAME OVER",
                    Gdx.graphics.getWidth() / 2f - 150,
                    Gdx.graphics.getHeight() / 2f);
            }
        }

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        if (player != null) player.dispose();
    }
}
