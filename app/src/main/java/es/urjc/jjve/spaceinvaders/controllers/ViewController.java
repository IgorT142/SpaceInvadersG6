package es.urjc.jjve.spaceinvaders.controllers;

import android.content.Context;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.urjc.jjve.spaceinvaders.entities.Bullet;
import es.urjc.jjve.spaceinvaders.entities.DefenceBrick;
import es.urjc.jjve.spaceinvaders.entities.Invader;
import es.urjc.jjve.spaceinvaders.entities.PlayerShip;
import es.urjc.jjve.spaceinvaders.view.SpaceInvadersView;

/**
 * Created by Christian on 03/10/2018.
 */

public class ViewController {


    // Up to 60 invaders
    private List<Invader> invaders;
    private int numInvaders = 0;
    private int killedInvaders = 0;
    private boolean underage;
    private SpaceInvadersView view;
    private PlayerShip playerShip;
    private int score;
    // The player's bullet
    private Bullet bullet;
    // The invaders bullets
    private List<Bullet> invadersBullets;
    private List<Bullet> playerBullets;
    //private int maxInvaderBullets = 10;
    // The player's shelters are built from bricks
    private List<DefenceBrick> bricks;

    private Invader specialInvader;

    //Determines the game bounds
    private int screenX;
    private int screenY;

    private int godMode = 0; //Modo invencible al teletransportarse


    public ViewController(Context context, int x, int y, SpaceInvadersView view) {

        this.screenX = x;
        this.screenY = y;

        this.view = view;
        this.playerBullets = new ArrayList<>();

        this.initGame(context);
    }


    /**
     * This method is used every game tick to update the positions of every entity and the score
     * Should be called when the entity is modified only
     */
    public void updateGame() {

        view.lockCanvas();
        view.drawBackground();
        view.drawJoystick();    //Botón de Joystick
        view.drawButton();      //Botón disparo

        paintInvaders();
        paintBricks();
        paintBullets();
        paintShip();


        view.drawGameObject("Score: " + score, 10, 50);
        view.unlockCanvas();
    }

    private void paintBullets() {

        for (Bullet shipBull : this.playerBullets) {
            view.drawGameObject (shipBull.getBitmapBullet(),shipBull.getRect().centerX(),shipBull.getRect().centerY());
        }

        for (Bullet bullet : invadersBullets) {
            if (bullet.getStatus()) {
                view.drawGameObject (bullet.getBitmapBullet(),bullet.getRect().centerX(),bullet.getRect().centerY());
            }
        }
    }

    /**
     * This method is used to update the entity position and the actions they are going to take
     * it checks if the invaders have reached the screen limit, and if they bumped into a brick barrier
     * also it checks if the invader has the opportunity to shoot
     * updates the bullet possition depending on the fps attribute
     */
    public boolean updateEntities(long fps) {

        //checks if any entity has reached a limit or another entity
        boolean bumpedEntity = false;

        //moves the spaceship
        playerShip.update(fps, spaceshipCanMove(playerShip.getMovement()));

        if(specialInvader!= null) {
            specialInvader.update(fps);
        }

        //For each invader, we check if its an active one and then we check if it has the opportunity to shoot
        //if the invader has reached the screen limit, it reverses the direction and goes down
        for (Invader i : invaders) {
            if (i.getVisibility()) {
                i.update(fps); // Move the next invader
                if(invadersBullets.size()<10) { //maxInvaderBullets

                    if (i.takeAim(playerShip.getX(), playerShip.getLength())) { // Does he want to take a shot?
                        Bullet newBullet = new Bullet(screenY,this.view.getContext());
                        invadersBullets.add(newBullet);
                        newBullet.shoot(i.getX() + i.getLength() / 2, i.getY(), Bullet.getDOWN()); // If so try and spawn a bullet// Shot fired, Prepare for the next shot
                            // Loop back to the first one if we have reached the last
                    }
                }

                // If that move caused them to bump the screen change bumped to true
                if (i.getX() > screenX - i.getLength() || i.getX() < 0) {
                    bumpedEntity = true;
                }
            }
            //Checks if an invader has touched the playership
            if (bumpedEntity) {
                // Move all the invaders down and change direction
                for (Invader inv : invaders) {
                    inv.dropDownAndReverse();
                    // Have the invaders landed
                    if (RectF.intersects(i.getRect(), playerShip.getRect())) {
                        return false;
                    }
                }
                return true;
            }
        }

        if(!underage){

            // Update the players bullet
            if(!updatePlayerBullet(fps)){
                return false;
            }
            // Has the player's bullet hit the top of the screen
            // Update all the invaders bullets if active
            updateInvadersBullet(fps);
            // Has an alien bullet hit a shelter brick
            for (Bullet bullet : invadersBullets) {
                if (bullet.getStatus()) {
                    for (DefenceBrick brick : bricks) {
                        if (brick.getVisibility()) {
                            if (RectF.intersects(bullet.getRect(), brick.getRect())) {
                                // A collision has occurred
                                bullet.setInactive();
                                brick.setInvisible();
                                for (int x = 0; x < numInvaders; x++) {
                                    invaders.get(x).chColour();
                                }
                                playerShip.chColour();
                            }
                        }
                    }
                    if(bullet.getGodBullet()){
                        if(specialInvader!=null)
                            bulletCollisionInvader(specialInvader,bullet);
                        for(Invader inv:invaders){
                            bulletCollisionInvader(inv,bullet);
                        }
                    }
                }

            }



            // Has an alien hit a shelter brick
            for (Invader invader : this.invaders){
                if(invader.getVisibility()){
                    for (DefenceBrick brick : bricks) {
                        if (brick.getVisibility()) {
                            if (RectF.intersects(invader.getRect(), brick.getRect())) {
                                // A collision has occurred
                                brick.setInvisible();
                                for (int x = 0; x < numInvaders; x++) {
                                    invaders.get(x).chColour();
                                }
                                invader.setInvisible();
                                playerShip.chColour();
                            }
                        }
                    }
                }
            }

            // Has the player hit a shelter brick
            for(DefenceBrick brick: bricks){
                if(brick.getVisibility()){
                    if(RectF.intersects(playerShip.getRect(),brick.getRect())){
                        brick.setInvisible();
                        playerShip.chColour();
                    }
                }
            }

            // Has the player hit an invader
            for(Invader invader: invaders){
                if(invader.getVisibility()){
                    if(RectF.intersects(playerShip.getRect(),invader.getRect())&& godMode <= 0){
                        return false;
                    }
                }
            }

            // Has an invader bullet hit the player ship
            for (Bullet bullet : invadersBullets) {
                if (bullet.getStatus()) {
                    if (RectF.intersects(playerShip.getRect(), bullet.getRect())) {
                        bullet.setInactive();
                        return false;
                    }
                }
            }

            // Has the player won
            return killedInvaders < numInvaders;

        }
        return true;


    }

    // colision bala con barrera
    private void bulletColisionBrick(DefenceBrick brick, Bullet currentBull) {
        if (brick.getVisibility()) {
            if (RectF.intersects(currentBull.getRect(), brick.getRect())) {
                // A collision has occurred
                currentBull.setInactive();
                brick.setInvisible();
            }
        }
    }

    //colision bala con invader
    private void bulletCollisionInvader(Invader inv, Bullet currentBull){
        if (inv.getVisibility()) {
            if (RectF.intersects(currentBull.getRect(), inv.getRect())) { //Has a bullet hit an invader?
                inv.setInvisible();
                currentBull.setInactive();
                if(inv == specialInvader){
                    score = score + 250;
                }else {
                    score = score + 100;
                }
                if(score%500==0){
                    //Teleporta la nave aleatoriamente
                    posicionRandom();
                    final Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            godMode--;
                            if (godMode== 0) {
                                timer.cancel();
                            }
                        }
                    }, 0, 100);
                }
                killedInvaders++;
            }
        }
    }
    private boolean updatePlayerBullet(long fps){
        for (int i=0;i<playerBullets.size();i++) {
            Bullet currentBull = playerBullets.get(i);
            currentBull.update(fps);
            //Colisión de bala con invader
            if(currentBull.getStatus()) {
                for (Invader inv : invaders) {
                    bulletCollisionInvader(inv, currentBull);
                }
                //Colisión de bala con muros
                for (DefenceBrick brick : bricks) {
                    bulletColisionBrick(brick, currentBull);
                }
                //bulletCollisionInvader(specialInvader,bullet);
                //Colisión de bala con borde de pantalla
                if (currentBull.getImpactPointY() < 0) {
                    currentBull.changeDir();
                    currentBull.setGodBullet();
                    //Recargar bala cuando rebote
                    Bullet nextBull = new Bullet(screenY, this.getView().getContext());
                    invadersBullets.add(playerBullets.get(0));
                    playerBullets.remove(0);
                    playerBullets.add(nextBull);
                }
                if (currentBull.getImpactPointY() > screenY){
                    currentBull.changeDir();
                }
                if(currentBull.getGodBullet()){
                    if(RectF.intersects(currentBull.getRect(),playerShip.getRect())){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void updateInvadersBullet(long fps){
        for (Bullet bullet : invadersBullets) {
            if (bullet.getStatus()) {
                bullet.update(fps);
            }
            if (bullet.getImpactPointY() > screenY) {
                bullet.changeDir();
                bullet.setGodBullet();
            }
            if (bullet.getImpactPointY() < 0) {
                bullet.changeDir();
                bullet.setGodBullet();
            }
        }
    }


    // If SpaceInvadersActivity is started then
    // start our thread.

    private void initGame(Context context) {
        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX, screenY);

        this.invaders = new ArrayList<>();
        this.invadersBullets = new ArrayList<>();
        this.bricks = new ArrayList<>();

        //view.drawGameObject(playerShip.getBitmap());
        // Reset the menace level

        if (!underage) {
            // Prepare the players bullet
            bullet = new Bullet(screenY,this.view.getContext());

            // Initialize the invadersBullets array

        }
        // Build an army of invaders
        numInvaders = 0;
        for (int column = 0; column < 6; column++) {
            for (int row = 0; row < 5; row++) {
                invaders.add(new Invader(context, row, column, screenX, screenY));
                numInvaders++;
            }
        }

        // Build the shelters
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks.add(new DefenceBrick(row, column, shelterNumber, screenX, screenY));
                }
            }
        }
    }


    private void paintShip() {
        view.drawGameObject(playerShip.getBitmap(), playerShip.getX(), playerShip.getY());
    }

    private void paintInvaders() {
        for (Invader i : invaders) {
            if (i.getVisibility()) {
                this.view.drawGameObject(i.getBitmap(), i.getX(), i.getY());
            }
        }
        if(specialInvader!=null) {
            this.view.drawGameObject(specialInvader.getBitmap(), specialInvader.getX(), specialInvader.getY());
        }
    }

    private void paintBricks() {
        for (DefenceBrick b : bricks) {
            try {
                if (b.getVisibility()) {
                    view.drawGameObject(b.getRect());
                }
            } catch (RuntimeException e) {

                System.out.println("Peta" + b.toString());
            }
        }
    }


    public SpaceInvadersView getView() {
        return view;
    }

    public void setView(SpaceInvadersView view) {
        this.view = view;
    }

    public void setUnderage(boolean underage) {
        this.underage = underage;
    }

    public void notifyShoot() {
        if(playerBullets.size()<1) {
            Bullet newBull = new Bullet(screenY,this.view.getContext());
            this.playerBullets.add(newBull);
            newBull.shoot((playerShip.getX() + playerShip.getLength()/2), playerShip.getY(), 0);
        }
    }

    public void removeBullets() {
        List<Bullet> inactive = new ArrayList<>();
        for (Bullet b : playerBullets) {
            if (!b.getStatus()) {
                inactive.add(b);
            }
        }
        for(Bullet b:invadersBullets){
            if(!b.getStatus()){
                inactive.add(b);
            }
        }
        for (Bullet b : inactive) {
            invadersBullets.remove(b);
            playerBullets.remove(b);
        }
    }

    public void shipMovement(float x, float y) {
        float epsilon = 0.01f;
        if (Math.abs(x - 0) > epsilon && (Math.abs(y - 0) > epsilon)) {
            final double angle = Math.atan2(y, x);
            final double halfPI = Math.PI / 2;
            final double quarterPI = Math.PI / 4;
            final double eighthPI = Math.PI / 8;
            if (angle >= -eighthPI && angle < eighthPI) {
                playerShip.setMovementState(2);
            } else if (angle >= eighthPI && angle < (quarterPI + eighthPI)) {
                playerShip.setMovementState(8);
                System.out.println("es el 8");
            } else if (angle >= (quarterPI + eighthPI) && angle < (halfPI + eighthPI)) {
                playerShip.setMovementState(4);
            } else if (angle >= (halfPI + eighthPI) && angle < (Math.PI - eighthPI)) {
                playerShip.setMovementState(7);
                System.out.println("es el 7");
            } else if (angle >= (Math.PI - eighthPI) && angle <= Math.PI) {
                playerShip.setMovementState(1);
            } else if (angle >= -Math.PI && angle < (-Math.PI + eighthPI)) {
                playerShip.setMovementState(1);
            } else if (angle >= (-Math.PI + eighthPI) && angle < (-halfPI - eighthPI)) {
                playerShip.setMovementState(5);
                System.out.println("es el 5");
            } else if (angle >= (-halfPI - eighthPI) && angle < (-halfPI + eighthPI)) {
                playerShip.setMovementState(3);
            } else if (angle >= (-halfPI + eighthPI) && angle < (-eighthPI)) {
                playerShip.setMovementState(6);
                System.out.println("es el 6");
            } else {
                playerShip.setMovementState(0);
            }
        } else {
            playerShip.setMovementState(0);
        }
    }

    public int getScore() {
        return score;
    }

    public void specialInvader(Context context) {
        this.specialInvader = new Invader(context,0,0,screenX,screenY);
        this.specialInvader.setInvaderSpecial(context);
    }

    private void posicionRandom(){
        float x =(float) (Math.random()*(screenX))+0;
        float y =(float) (Math.random()*(screenY))+0;
        playerShip.setX(x);
        playerShip.setY(y);
        paintShip();
        godMode=15;
    }


    //Bloquea el movimiento hacia afuera de la pantalla
    private boolean spaceshipCanMove(int direccion) {
        switch (direccion) {
            case 1: //Case LEFT
                return playerShip.getMovement() != 1 || !(playerShip.getX() - 1 < 0);
            case 2: //Case RIGHT
                return playerShip.getMovement() != 2 || !(playerShip.getX() + playerShip.getLength() > screenX);
                //En los siguiente casos la Y no se mueve cuadrada con screenY, screenY va de 0 (por arriba) hasta el maximo (por abajo) y el programa
                //toma de referencia el 0 donde el maximo y el maximo donde el 0
            case 3: //Case DOWN (o deberia de serlo, en realidad toda el caso UP)
                return playerShip.getMovement() != 3 || !(playerShip.getY() - 5 < 0);
            case 4: //Case UP (o deberia de serlo, en realidad toda el caso DOWN)
                return playerShip.getMovement() != 4 || !(playerShip.getY() + playerShip.getHeight() + 1 > screenY);
            case 5: //Case DOWN RIGHT
                return playerShip.getMovement() != 5 || !(playerShip.getX() + playerShip.getLength() > screenX) || !(playerShip.getY() - 5 < 0);
            case 6: //CASE UP RIGHT
                return (playerShip.getMovement() != 6 || !(playerShip.getY() - 5 < 0)) || (!(playerShip.getX() + playerShip.getLength() > screenX));
            case 7: //CASE DOWN LEFT
                return (playerShip.getMovement() != 7 || !(playerShip.getY() + +playerShip.getHeight() + 1 > screenY)) || (!(playerShip.getX() - 1 < 0));
            case 8: //CASE UP LEFT
                return (playerShip.getMovement() != 8 || !(playerShip.getY() + +playerShip.getHeight() + 1 > screenY)) || (!(playerShip.getX() + playerShip.getLength() > screenX));
            default:
                return true;
        }
    }
}
