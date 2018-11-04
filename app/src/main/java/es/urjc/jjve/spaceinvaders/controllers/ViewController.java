package es.urjc.jjve.spaceinvaders.controllers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.SoundPool;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import es.urjc.jjve.spaceinvaders.entities.Bullet;
import es.urjc.jjve.spaceinvaders.entities.DefenceBrick;
import es.urjc.jjve.spaceinvaders.entities.Invader;
import es.urjc.jjve.spaceinvaders.entities.PlayerShip;
import es.urjc.jjve.spaceinvaders.view.SpaceInvadersView;

/**
 * Created by Christian on 03/10/2018.
 */

public class ViewController {


    private static final int MAX_INVADER_BULLETS = 300;
    // Up to 60 invaders
    List<Invader> invaders;
    int numInvaders = 0;
    int killedInvaders = 0;
    private boolean underage;
    private SpaceInvadersView view;
    private PlayerShip playerShip;
    private boolean lost;
    private int score;
    // The player's bullet
    private Bullet bullet;
    // The invaders bullets
    private List<Bullet> invadersBullets;
    private List<Bullet> playerBullets;
    private int nextBullet;
    private int maxInvaderBullets = 10;
    // The player's shelters are built from bricks
    private List<DefenceBrick> bricks;
    private int numBricks;


    private Invader specialInvader;


    //Determines the game bounds
    private int screenX;
    private int screenY;


    public ViewController(Context context, int x, int y, SpaceInvadersView view) {


        this.screenX = x;
        this.screenY = y;

        this.view = view;
        this.playerBullets = new ArrayList<>();


        // this.context = context;
        this.initGame(context);


    }


    /**
     * This method is used every game tick to update the positions of every entity and the score
     * Should be called when the entity is modified only, ToDo for next sprint, update entities on screen only when they are modified
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
            view.drawGameObject(shipBull.getRect());
        }

        for (Bullet bullet : invadersBullets) {
            if (bullet.getStatus()) {
                view.drawGameObject(bullet.getRect());
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
        playerShip.update(fps);

        if(specialInvader!= null) {
            specialInvader.update(fps);
        }

        //For each invader, we check if its an active one and then we check if it has the opportunity to shoot
        //if the invader has reached the screen limit, it reverses the direction and goes down
        for (Invader i : invaders) {
            if (i.getVisibility()) {
                i.update(fps); // Move the next invader
                if(invadersBullets.size()<maxInvaderBullets) {

                    if (i.takeAim(playerShip.getX(), playerShip.getLength())) { // Does he want to take a shot?
                        Bullet newBullet = new Bullet(screenY);
                        invadersBullets.add(newBullet);
                        newBullet.shoot(i.getX() + i.getLength() / 2, i.getY(), bullet.DOWN); // If so try and spawn a bullet// Shot fired, Prepare for the next shot
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
            if (killedInvaders == numInvaders) {
                return false;
            }

        }
        return true;


    }

    // colision bala con barrera
    public void bulletColisionBrick (DefenceBrick brick, Bullet currentBull) {
        if (brick.getVisibility()) {
            if (RectF.intersects(currentBull.getRect(), brick.getRect())) {
                // A collision has occurred
                currentBull.setInactive();
                brick.setInvisible();
            }
        }
    }

    //colision bala con invader
    public void bulletCollisionInvader(Invader inv, Bullet currentBull){
        if (inv.getVisibility()) {
            if (RectF.intersects(currentBull.getRect(), inv.getRect())) { //Has a bullet hit an invader?
                inv.setInvisible();
                currentBull.setInactive();
                if(inv == specialInvader){
                    score = score + 250;
                }else {
                    score = score + 100;
                }
                killedInvaders++;
            }
        }
    }
    public boolean updatePlayerBullet(long fps){
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
                bulletCollisionInvader(specialInvader,bullet);
                //Colisión de bala con borde de pantalla
                if (currentBull.getImpactPointY() < 0) {
                    currentBull.changeDir();
                    currentBull.setGodBullet();
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

    public void updateInvadersBullet(long fps){
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

    public void initGame(Context context) {
        // Make a new player space ship
        playerShip = new PlayerShip(context, screenX, screenY);

        this.invaders = new ArrayList<>();
        this.invadersBullets = new ArrayList<>();
        this.bricks = new ArrayList<>();

        //view.drawGameObject(playerShip.getBitmap());
        // Reset the menace level

        if (!underage) {
            // Prepare the players bullet
            bullet = new Bullet(screenY);

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
        numBricks = 0;
        for (int shelterNumber = 0; shelterNumber < 4; shelterNumber++) {
            for (int column = 0; column < 10; column++) {
                for (int row = 0; row < 5; row++) {
                    bricks.add(new DefenceBrick(row, column, shelterNumber, screenX, screenY));
                    numBricks++;
                }
            }
        }
    }

    private void checkInteresectionWInvader(Bullet bullet) {

        int i = 0;
        while (bullet.getStatus() && i < invaders.size()) {
            i++;
            if (invaders.get(i).getVisibility()) {
                if (RectF.intersects(invaders.get(i).getRect(), bullet.getRect())) {
                    bullet.setInactive();
                    invaders.get(i).setInvisible();
                    score += 100;
                }
            }
        }
    }

    private void paintShip() {
        view.drawGameObject(playerShip.getBitmap(), playerShip.getX(), playerShip.getY());
    }

    public void paintInvaders() {
        for (Invader i : invaders) {
            if (i.getVisibility()) {
                this.view.drawGameObject(i.getBitmap(), i.getX(), i.getY());
            }
        }
        if(specialInvader!=null) {
            this.view.drawGameObject(specialInvader.getBitmap(), specialInvader.getX(), specialInvader.getY());
        }
    }

    public void paintBricks() {
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

    public boolean isUnderage() {
        return underage;
    }

    public void setUnderage(boolean underage) {
        this.underage = underage;
    }

    public void notifyShoot() {
        if(playerBullets.size()<5) {
            Bullet newBull = new Bullet(screenY);
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
        if((x!=0 && y!=0)) {
            if (Math.abs(x) > Math.abs(y)) {
                if (x > 0) {
                    playerShip.setMovementState(2);
                } else {
                    playerShip.setMovementState(1);
                }

            } else {
                if (y > 0) {
                    playerShip.setMovementState(4);
                } else {
                    playerShip.setMovementState(3);
                }
            }
        }else{
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
}
