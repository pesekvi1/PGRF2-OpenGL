package renderer;

import java.util.ArrayList;
import java.util.List;

import model.Emitter;
import model.Particle;
import model.Rectangle3D;
import transforms.Point3D;

/**
 * V této tøídì se volaji metody na vypocet souradnic vlocek, vypocet kolizi, atd...
 * @author Viktor
 *
 */
public class Renderer {
    private int numberOfParticles = 1500;
    private Emitter emitter;
    private List<Particle> snowflakes;
    private List<Particle> fallenSnow;
    private List<Rectangle3D> rectangles;

    public Renderer() {
	this.snowflakes = new ArrayList<>();
	this.fallenSnow = new ArrayList<>();
	this.rectangles = new ArrayList<>();
	this.emitter = new Emitter(-50, 50, 100);
    }

    // nastaveni objektu, se kterymi muze vlocka kolidovat
    public void initializeRectangles() {
	// strecha 1
	rectangles.add(new Rectangle3D(new Point3D(-10, 15, 0), new Point3D(10, 15, 0), new Point3D(10, 10, 10),
		new Point3D(-10, 10, 10)));
	// strecha 2
	rectangles.add(new Rectangle3D(new Point3D(-10, 15, 0), new Point3D(10, 15, 0), new Point3D(10, 10, -10),
		new Point3D(-10, 10, -10)));
	// zem
	rectangles.add(new Rectangle3D(new Point3D(-25, 0, 25), new Point3D(-25, 0, -25), new Point3D(25, 0, -25),
		new Point3D(25, 0, 25)));
    }

    // naplneni listu, ktery agreguje vsechny pohybujici se vlocky
    public void initializeParticles() {
	for (int i = 0; i < numberOfParticles; i++) {
	    snowflakes.add(emitter.createParticle());
	}
    }

    // render vlocek, vypocty vsech pozic
    public void renderParticles() {
	for (int i = 0; i < snowflakes.size(); i++) {
	    Particle p = snowflakes.get(i);
	    // pokud uz castice nezije, naplnime misto v listu novou instanci castice
	    if (!p.isAlive()) {
		    snowflakes.set(i, emitter.createParticle());
		}
	    // doplneni listu aby odpovidal poctu nami zadanych castic
	    if (numberOfParticles > snowflakes.size()) {
		snowflakes.add(emitter.createParticle());
	    } else if (numberOfParticles < snowflakes.size()) {
		snowflakes.remove(snowflakes.size() - 1);
	    } else {

		for (int j = 0; j < rectangles.size(); j++) {
		    if (rectangles.get(j).collides(p.getPos())) {
			// porovnavani pozice castice s vypoctenou pozici kolize
			// kdyz jeste kolize nenastala, cyklus pokracuje dal
			if (p.getPos().getY() > rectangles.get(j).intersects(p.getPos())) {
			    continue;
			} else {
			    fallenSnow.add(new Particle(new Point3D(p.getPos().getX(),
				    rectangles.get(j).intersects(p.getPos()), p.getPos().getZ()), p.getGravity(),
				    p.getLife()));
			    snowflakes.set(i, emitter.createParticle());
			}
		    }
		}
	    }
	    // pohyb castice
	    p.move();
	}
    }

    public void moveParticle(int i) {
	snowflakes.get(i).move();
    }

    public List<Particle> getSnowflakes() {
	return snowflakes;
    }

    public List<Particle> getFallenSnow() {
	return fallenSnow;
    }

    public int getNumberOfParticles() {
	return numberOfParticles;
    }

    public void setNumberOfParticles(int numberOfParticles) {
	this.numberOfParticles = numberOfParticles;
    }

    public Emitter getEmitter() {
	return emitter;
    }
}
