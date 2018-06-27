package model;

import java.math.BigDecimal;
import java.util.Random;

import transforms.Point3D;
import transforms.Vec3D;

/**
 * Trida reprezentuje mrak, ze ktereho padaji vlocky
 * stara se o vytvareni novych vlocek a nastavuje jim gravitaci
 * @author Viktor
 *
 */
public class Emitter {
    private BigDecimal windX = new BigDecimal(0.0);
    private BigDecimal windZ = new BigDecimal(0.0);
    private double start;
    private double end;
    private double height;

    public Emitter(double start, double end, double height) {
	super();
	this.start = start;
	this.end = end;
	this.height = height;
    }

    public Particle createParticle() {
	return new Particle(new Point3D(generateRandom(this.start, this.end), this.height, generateRandom(this.start, this.end)), // pozice castice
		new Vec3D(windX.doubleValue(), -Math.random(), windZ.doubleValue()), // gravitace pro castici
			(int)generateRandom(850, 1500) // life
			);
    }
    
    public double generateRandom(double start, double end) {
	double random = new Random().nextDouble();
	double result = start + (random * (end - start));
	return result;
    }

    public BigDecimal getWindZ() {
	return windZ;
    }

    public void setWindZ(BigDecimal windZ) {
	this.windZ = windZ;
    }

    public BigDecimal getWindX() {
	return windX;
    }

    public void setWindX(BigDecimal wind) {
	this.windX = wind;
    }
}
