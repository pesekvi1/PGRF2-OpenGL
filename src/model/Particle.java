package model;


import transforms.Point3D;
import transforms.Vec3D;

/**
 * Trida reprezentujici jednotlive castice/vlocky
 * tato trida se stara o vypoctu pohybu castice ve scene
 * @author Viktor
 *
 */
public class Particle {
    private Point3D pos;
    private Vec3D gravity;
    private int life;
    private int hasLived;
    private boolean alive = true;

    public Particle(Point3D pos, Vec3D gravity, int life) {
	super();
	this.pos = pos;
	this.gravity = gravity;
	this.life = life;
	this.hasLived = 0;
	this.alive = true;
    }

    public void move() {
	if (alive && hasLived < life) {
	    this.pos = addGravity(gravity);
	    hasLived++;
	} else {
	    alive = false;
	}
    }
    
    public Point3D addGravity(Vec3D v) {
	return new Point3D(pos.getX() + v.getX(), pos.getY() + v.getY(), pos.getZ() + v.getZ());
    }

    public Vec3D getGravity() {
	return gravity;
    }

    public Point3D getPos() {
	return pos;
    }

    public void setPos(Point3D pos) {
	this.pos = pos;
    }

    public int getLife() {
	return life;
    }

    public boolean isAlive() {
	return alive;
    }

    public int getHasLived() {
	return hasLived;
    }

}
