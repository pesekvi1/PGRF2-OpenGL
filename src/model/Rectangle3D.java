package model;

import transforms.Point3D;

/**
 * Model jednotlivych casti teles u kterych ma cenu zjistovat kolize s vlockami
 * tzn. strecha domu, teren atd..
 * zde se rozhoduje o kolizich
 * @author Viktor
 *
 */
public class Rectangle3D {
    private Point3D p1, p2, p3, p4;
    private double maxX, minX, maxY, minY, maxZ, minZ;

    public Rectangle3D(Point3D p1, Point3D p2, Point3D p3, Point3D p4) {
	this.p1 = p1;
	this.p2 = p2;
	this.p3 = p3;
	this.p4 = p4;
	maxX = findMax(p1.getX(), p2.getX(), p3.getX(), p4.getX());
	minX = findMin(p1.getX(), p2.getX(), p3.getX(), p4.getX());
	maxY = findMax(p1.getY(), p2.getY(), p3.getY(), p4.getY());
	minY = findMin(p1.getY(), p2.getY(), p3.getY(), p4.getY());
	maxZ = findMax(p1.getZ(), p2.getZ(), p3.getZ(), p4.getZ());
	minZ = findMin(p1.getZ(), p2.getZ(), p3.getZ(), p4.getZ());
    }
    // metoda, ktera zjistuje, zda vlocka bude kolidovat s danym objektem
    public boolean collides(Point3D point) {
	return (point.getX() >= minX && point.getX() <= maxX) && (point.getY() <= maxY)
		&& (point.getZ() >= minZ && point.getZ() <= maxZ);
    }

    // vypocet mista, ve kterem se vlocka srazi s objektem
    public double intersects(Point3D point) {
	if (minZ == 0) {
	    double t = (maxZ - point.getZ()) / (maxZ - minZ);
	    double y3 = (minY * (1.0 - t)) + (maxY * t);
	    return y3;
	} else {
	    double t = (point.getZ() - minZ) / (maxZ - minZ);
	    double y3 = (minY * (1.0 - t)) + (maxY * t);
	    return y3;
	}
    }
    // nalezeni hranice objektu
    public double findMax(double a, double b, double c, double d) {
	return 	Math.max(Math.max(a, b), Math.max(c, d));
    }
    // nalezeni hranice objektu
    public double findMin(double a, double b, double c, double d) {	
	return Math.min(Math.min(a, b), Math.min(c, d));
    }
}
