package pt.uma.tpsi.ad.entities;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pt.uma.tpsi.ad.game.Animator;

public abstract class Brick {
    protected Animator animator;
    protected int posX, posY;
    protected Rectangle boundingBox;
    protected boolean collided = false;

    // standard size shared between all bricks (set when first brick is created)
    private static int standardWidth = 0;
    private static int standardHeight = 0;

    // Scale factor to render bricks smaller than the source image (pode ser ajustado)
    // Ajustado para 0.45 para reduzir o tamanho e permitir 20 colunas alinhadas
    public static float SCALE = 1f;

    // constructor antigo (compatibilidade) assume 1x1
    public Brick(SpriteBatch batch, String spritePath, int x, int y){
        this(batch, spritePath, 1, 1, x, y);
    }

    // novo constructor que permite especificar cols/rows do sprite sheet
    public Brick(SpriteBatch batch, String spritePath, int cols, int rows, int x, int y){
        this.animator = new Animator(batch, spritePath, cols, rows);
        this.posX = x;
        this.posY = y;
        // bounding box is initialized in create() after animator.create() sets sizes
    }
    public void create() {
        animator.create();
        // define standard size the primeira vez que um brick é criado
        if (standardWidth == 0 || standardHeight == 0) {
            // aplica escala para reduzir tamanho visual dos bricks
            standardWidth = Math.max(1, Math.round(animator.getWidth() * SCALE));
            standardHeight = Math.max(1, Math.round(animator.getHeight() * SCALE));
        }
        // use the standard size for all bricks' bounding boxes
        this.boundingBox = new Rectangle(posX, posY, standardWidth, standardHeight);
        boundingBox.setPosition(posX, posY);
    }

    public void render() {
        // desenhar animação em loop desde o início do jogo
        animator.render(posX, posY, standardWidth, standardHeight);
    }

    // helper para subclasses desenharem um frame fixo (mantém tamanho standard)
    protected void renderFrame(int frameIndex) {
        animator.renderFrame(posX, posY, standardWidth, standardHeight, frameIndex);
    }

    public boolean isCollided() {
        return collided;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public abstract void onCollision();

    // --- helpers para normalizar tamanho entre diferentes spritesheets ---
    public static void setStandardSize(int w, int h) {
        standardWidth = Math.max(1, w);
        standardHeight = Math.max(1, h);
    }

    public static int getStandardWidth() { return standardWidth; }
    public static int getStandardHeight() { return standardHeight; }
}
