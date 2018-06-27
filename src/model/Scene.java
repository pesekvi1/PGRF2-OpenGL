package model;

import java.io.IOException;
import java.io.InputStream;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * Tøída pro naèítání scény, obsahuje textury a mapování textur dále metody pro
 * vykreslování objektù
 * 
 * @author Viktor
 *
 */
public class Scene {
    private Texture textureGrass;
    private Texture textureSnow;
    private Texture textureWallWindow;
    private Texture textureWallDoor;
    private Texture textureWall;
    private Texture textureRoof;

    // vsechny textury jsou ulozeny v adresari res
    public void initializeScene(GL2 gl) {
	InputStream is = getClass().getResourceAsStream("/tex-grass.jpg");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureGrass = TextureIO.newTexture(is, true, "jpg");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	is = getClass().getResourceAsStream("/tex-snow.png");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureSnow = TextureIO.newTexture(is, true, "png");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	is = getClass().getResourceAsStream("/texWallWindow.jpg");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureWallWindow = TextureIO.newTexture(is, true, "jpg");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	is = getClass().getResourceAsStream("/texWallDoor.jpg");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureWallDoor = TextureIO.newTexture(is, true, "jpg");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	is = getClass().getResourceAsStream("/texWall.jpg");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureWall = TextureIO.newTexture(is, true, "jpg");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	is = getClass().getResourceAsStream("/texRoof.jpg");
	if (is == null)
	    System.out.println("File not found");
	else
	    try {
		textureRoof = TextureIO.newTexture(is, true, "jpg");
	    } catch (IOException e) {
		System.err.println("Chyba cteni souboru s texturou");
	    }

	// display-list na vykresleni castice
	gl.glNewList(1, GL2.GL_COMPILE);

	gl.glEnable(GL2.GL_TEXTURE_2D);
	textureSnow.enable(gl);
	textureSnow.bind(gl);

	gl.glBegin(GL2.GL_QUADS);
	gl.glColor3f(1, 1, 1);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3f(1, 1, 1);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3f(-1, 1, 1);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3f(-1, -1, 1);
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3f(1, -1, 1);

	gl.glEnd();
	textureSnow.disable(gl);
	gl.glEndList();

	// display na vykresleni domecku
	gl.glNewList(2, GL2.GL_COMPILE);
	// vykresleni domecku
	textureWallDoor.enable(gl);
	textureWallDoor.bind(gl);
	gl.glBegin(GL2.GL_QUADS);
	// stena predni
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 10, 10);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 10, 10);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(10, 0, 10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(-10, 0, 10);

	gl.glEnd();
	textureWallDoor.disable(gl);

	textureWallWindow.enable(gl);
	textureWallWindow.bind(gl);
	gl.glBegin(GL2.GL_QUADS);
	// stena leva
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 10, 10);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(-10, 10, -10);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(-10, 0, -10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(-10, 0, 10);
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	// stena prava
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(10, 10, -10);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 10, 10);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(10, 0, 10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(10, 0, -10);
	gl.glEnd();
	textureWallWindow.disable(gl);

	textureWall.enable(gl);
	textureWall.bind(gl);
	gl.glBegin(GL2.GL_QUADS);
	// stena zadni
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 10, -10);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 10, -10);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(10, 0, -10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(-10, 0, -10);
	gl.glEnd();
	textureWall.disable(gl);

	textureRoof.enable(gl);
	textureRoof.bind(gl);
	gl.glBegin(GL2.GL_QUADS);
	// prvni pulka strechy
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 15, 0);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 15, 0);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(10, 10, 10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(-10, 10, 10);
	gl.glEnd();

	gl.glBegin(GL2.GL_QUADS);
	// druha pulka strechy
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 15, 0);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 15, 0);
	gl.glTexCoord2f(1.0f, 0.0f);
	gl.glVertex3d(10, 10, -10);
	gl.glTexCoord2f(0.0f, 0.0f);
	gl.glVertex3d(-10, 10, -10);
	gl.glEnd();
	textureRoof.disable(gl);

	textureWall.enable(gl);
	textureWall.bind(gl);
	gl.glBegin(GL.GL_TRIANGLES);
	// pravy stit
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(10, 10, 10);
	gl.glTexCoord2f(0.5f, 0.5f);
	gl.glVertex3d(10, 15, 0);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(10, 10, -10);
	gl.glEnd();

	gl.glBegin(GL.GL_TRIANGLES);
	// levy stit
	gl.glTexCoord2f(0.0f, 1.0f);
	gl.glVertex3d(-10, 10, 10);
	gl.glTexCoord2f(0.5f, 0.5f);
	gl.glVertex3d(-10, 15, 0);
	gl.glTexCoord2f(1.0f, 1.0f);
	gl.glVertex3d(-10, 10, -10);
	gl.glEnd();
	textureWall.disable(gl);

	gl.glEndList();

	// plocha, reprezentujici teren
	gl.glNewList(3, GL2.GL_COMPILE);

	gl.glEnable(GL2.GL_TEXTURE_2D);
	gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_REPLACE);
	textureGrass.enable(gl);
	textureGrass.bind(gl);

	// vykresleni plochy, terenu
	gl.glBegin(GL2.GL_QUADS);
	for (int z = -25; z < 25; z += 1) {
	    for (int x = -25; x < 25; x += 1) {
		gl.glTexCoord2f(0.0f, 0.0f);
		gl.glVertex3i(x, 0, z);
		gl.glTexCoord2f(1.0f, 0.0f);
		gl.glVertex3i(x + 1, 0, z);
		gl.glTexCoord2f(1.0f, 1.0f);
		gl.glVertex3i(x + 1, 0, z + 1);
		gl.glTexCoord2f(0.0f, 1.0f);
		gl.glVertex3i(x, 0, z + 1);
	    }
	}
	gl.glEnd();
	textureGrass.disable(gl);
	gl.glEndList();
    }

    public void drawParticle(GL2 gl) {
	gl.glCallList(1);
    }

    public void drawHouse(GL2 gl) {
	gl.glCallList(2);
    }

    public void drawSurface(GL2 gl) {
	gl.glCallList(3);
    }

}
