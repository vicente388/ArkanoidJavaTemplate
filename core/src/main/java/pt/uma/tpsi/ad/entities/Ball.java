package pt.uma.tpsi.ad.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import pt.uma.tpsi.ad.game.Animator;

public class Ball {
    private Animator animator;
    private int posX, posY;
    private Rectangle boundingBox;
    private int directionX;
    private int directionY;
    private double angle;
    private int speedX = 6;
    private int speedY = 5;


    public Ball(SpriteBatch batch){

        animator = new Animator(batch, "ball.png", 2, 2);
        this.directionX =1;
        this.directionY =1;
        this.angle =0.5;

    }

    public void create() {
        animator.create();
        posX = (Gdx.graphics.getWidth()/2) - this.animator.getWidth()/2;
        posY = (Gdx.graphics.getHeight()/2);
        boundingBox = new Rectangle(posX, posY, animator.getWidth(), animator.getHeight());



    }

    public void render(){

        posY+=(directionY* speedY);
        posX+=(speedX * directionX);

        if (posY > Gdx.graphics.getHeight() - animator.getHeight()) {
            directionY = -1;
        }

        if (posX > Gdx.graphics.getWidth() - animator.getWidth()) {
            directionX = -1;
        } else if (posX < 0) {
            directionX = 1;
        }


        boundingBox.setPosition(posX, posY);
        animator.render(posX,posY);
    }
    public int posY() {
        return posY;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void reverseYDirection() {
        directionY *= -1;
    }

    // Empurra a bola para fora do rect fornecido para evitar deteção repetida de colisão
    public void resolveCollisionWith(Rectangle rect) {
        if (animator == null || boundingBox == null) return;
        float ballCenterY = posY + animator.getHeight() / 2f;
        float rectCenterY = rect.y + rect.height / 2f;
        if (ballCenterY < rectCenterY) {
            // bola estava abaixo do rect, posiciona-a abaixo
            posY = (int) (rect.y - animator.getHeight() - 1);
        } else {
            // bola estava acima do rect, posiciona-a acima
            posY = (int) (rect.y + rect.height + 1);
        }
        boundingBox.setPosition(posX, posY);
    }

    public void adjustDirectionOnContact(Rectangle rect) {

        reverseYDirection();
    }

}
