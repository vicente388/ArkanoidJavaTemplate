package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class BrickGrid {

    private List<Brick> bricks;
    private SpriteBatch batch;

    public BrickGrid(SpriteBatch batch) {
        this.batch = batch;
        bricks = new ArrayList<>();
        createBricks();
    }

    private void createBricks() {
        // Requerido pelo utilizador: 4 linhas x 20 colunas
        int rows = 4;
        int cols = 20;

        // passo 1: criar instâncias com posições temporárias e inicializar dimensões
        List<Brick> temp = new ArrayList<>();
        for (int i = 0; i < rows * cols; i++) {
            int type = (int) (Math.random() * 4);
            Brick brick;
            int x = 0, y = 0;
            switch (type) {
                case 0:
                    brick = new NormalBrick(batch, x, y);
                    break;
                case 1:
                    brick = new StrongBrick(batch, x, y);
                    break;
                case 2:
                    brick = new IndestructibleBrick(batch, x, y);
                    break;
                case 3:
                    brick = new PowerUpBrick(batch, x, y);
                    break;
                default:
                    brick = new NormalBrick(batch, x, y);
                    break;
            }
            brick.create();
            temp.add(brick);
        }

        // Normalizar todas as dimensões para o maior width/height de todos os sprites
        int maxW = 0;
        int maxH = 0;
        for (Brick b : temp) {
            int w = (int) b.getBoundingBox().width;
            int h = (int) b.getBoundingBox().height;
            if (w > maxW) maxW = w;
            if (h > maxH) maxH = h;
        }
        // define o tamanho padrão e atualiza bounding boxes
        Brick.setStandardSize(maxW, maxH);
        for (Brick b : temp) {
            b.getBoundingBox().setSize(maxW, maxH);
        }

        // usar os valores normalizados
        int brickW = maxW;
        int brickH = maxH;

        // Para que as colunas fiquem direitas, usamos um spacing horizontal pequeno e constante
        int spacingX = Math.max(2, Math.round(brickW * 0.08f)); // pequeno espaço horizontal
        int spacingY = Math.round(brickH * 0.9f);  // espaço vertical razoável

        int totalWidth = cols * brickW + (cols - 1) * spacingX;
        // Se o totalWidth for maior que a largura da janela, reduzimos um pouco spacingX
        if (totalWidth > Gdx.graphics.getWidth()) {
            int overflow = totalWidth - Gdx.graphics.getWidth();
            int reducePerGap = Math.max(0, overflow / Math.max(1, cols - 1));
            spacingX = Math.max(0, spacingX - reducePerGap);
            totalWidth = cols * brickW + (cols - 1) * spacingX;
        }

        int startX = (int) ((Gdx.graphics.getWidth() - totalWidth) / 2f);
        int startY = Gdx.graphics.getHeight() - 100; // distância ao topo ajustável

        // passo 2: posicionar os bricks numa grelha ordenada
        bricks.clear();
        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Brick b = temp.get(index++);
                int x = startX + col * (brickW + spacingX);
                int y = startY - row * (brickH + spacingY);
                b.posX = x;
                b.posY = y;
                b.getBoundingBox().setPosition(x, y);
                bricks.add(b);
            }
        }
    }

    public void render() {
        for (Brick brick : bricks) {
            if (!brick.isCollided()) {
                brick.render();
            }
        }
    }

    public List<Brick> getBricks() {
        return bricks;
    }
}
