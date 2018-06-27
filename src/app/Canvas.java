package app;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import model.Emitter;
import model.Particle;
import model.Scene;
import renderer.Renderer;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * trida pro zobrazeni sceny v OpenGL: inicializace, prekresleni, udalosti,
 * viewport
 * @author PGRF FIM UHK
 * @version 2015
 */

public class Canvas implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {
    private static double SCALE = 0.1; // koeficinet pro zmenseni castice
    private Renderer renderer = new Renderer();
    private Emitter emitter;
    private Scene scene = new Scene();
    private List<Particle> snowflakes = new ArrayList<>(); // vlocky poletujici
    private List<Particle> fallenSnow = new ArrayList<>(); // vlocky jiz spadle na zem
    private GLU glu;
    float m[] = new float[16];

    int width, height, dx, dy, x, y;
    int ox, oy;

    float zenit;
    float azimut;
    double ex, ey, ez, cx, cy, cz, px, py, pz, cenx, ceny, cenz, ux, uy, uz;
    double a_rad, z_rad;
    long oldmils = System.currentTimeMillis();

    /**
     * metoda inicializace, volana pri vytvoreni okna
     */
    @Override
    public void init(GLAutoDrawable glDrawable) {
	System.out.println("VELIKOST LISTU: " + snowflakes.size());
	GL2 gl = glDrawable.getGL().getGL2();
	scene.initializeScene(gl);
	renderer.initializeRectangles();
	emitter = renderer.getEmitter();
	renderer.initializeParticles();
	glu = new GLU();
	px = 0;
	py = 15;
	pz = 60;
	uy = 1;
	uz = 0;
    }

    /**
     * metoda zobrazeni, volana pri prekresleni kazdeho snimku
     */
    @Override
    public void display(GLAutoDrawable glDrawable) {
	GL2 gl = glDrawable.getGL().getGL2();
	// nulovani mista pro kresleni
	gl.glClearColor(0f, 0f, 0f, 1f);
	gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
	gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
	gl.glEnable(GL2.GL_DEPTH_TEST);

	gl.glMatrixMode(GL2.GL_PROJECTION);
	gl.glLoadIdentity();
	glu.gluPerspective(45, (double) width / height, 0.1, 500);

	gl.glMatrixMode(GL2.GL_MODELVIEW);
	gl.glLoadIdentity();

	cx = px + ex;
	cy = py + ey;
	cz = pz + ez;

	gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, m, 0);
	gl.glLoadIdentity();

	// nastaveni kamery
	gl.glRotatef(-zenit, 1.0f, 0, 0);
	gl.glRotatef(azimut, 0, 1.0f, 0);
	gl.glMultMatrixf(m, 0);
	gl.glTranslated(-px, -py, -pz);

	// surface
	scene.drawSurface(gl);

	// domecek
	scene.drawHouse(gl);

	gl.glDisable(GL2.GL_TEXTURE_2D);

	// nastaveni blendovani a zakaz zapisovani do Z-Bufferu
	gl.glEnable(GL2.GL_BLEND);
	gl.glDepthMask(false);
	gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

	// vypocitani souradnic vlocek a nasledne vykresleni
	renderer.renderParticles();
	this.snowflakes = renderer.getSnowflakes();
	for (int i = 0; i < snowflakes.size(); i++) {
	    gl.glPushMatrix();
	    rotateQuadToFaceCamera(gl, snowflakes.get(i));
	    gl.glPopMatrix();
	}

	// vykresleni jiz spadlych vlocek
	this.fallenSnow = renderer.getFallenSnow();
	for (int i = 0; i < fallenSnow.size(); i++) {
	    gl.glPushMatrix();
	    rotateQuadToFaceCamera(gl, fallenSnow.get(i));
	    gl.glPopMatrix();
	}

	gl.glDisable(GL2.GL_TEXTURE_2D);
	gl.glDepthMask(true);
	gl.glDisable(GL2.GL_BLEND);

    }

    /**
     * metoda volana pri zmene velikosti okna a pri prvnim vykresleni
     */
    @Override
    public void reshape(GLAutoDrawable glDrawable, int x, int y, int width, int height) {
	this.height = height;
	this.width = width;
	GL2 gl = glDrawable.getGL().getGL2();
	gl.glViewport(0, 0, width, height);
    }

    // metoda na vykreslovani vlocek a nataceni smerem k pozorovateli
    private void rotateQuadToFaceCamera(GL2 gl, Particle p) {
	gl.glTranslated(p.getPos().getX(), p.getPos().getY(), p.getPos().getZ());
	gl.glRotated(-azimut, 0, 1, 0);
	gl.glRotated(zenit, 1, 0, 0);
	gl.glScaled(SCALE, SCALE, SCALE);
	scene.drawParticle(gl);
    }

    // mouse listener
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) {
	    ox = e.getX();
	    oy = e.getY();
	}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    // mouse motion listener
    @Override
    public void mouseDragged(MouseEvent e) {
	if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
	    dx = e.getX() - ox;
	    dy = e.getY() - oy;
	    ox = e.getX();
	    oy = e.getY();

	    zenit -= dy;
	    if (zenit > 90)
		zenit = 90;
	    if (zenit <= -90)
		zenit = -90;
	    azimut += dx;
	    azimut = azimut % 360;
	    a_rad = Math.toRadians(azimut);
	    z_rad = Math.toRadians(zenit);
	    ex = Math.sin(a_rad) * Math.cos(z_rad);
	    ey = Math.sin(z_rad);
	    ez = -Math.cos(a_rad) * Math.cos(z_rad);
	    ux = Math.sin(a_rad) * Math.cos(z_rad + Math.PI / 2);
	    uy = Math.sin(z_rad + Math.PI / 2);
	    uz = -Math.cos(a_rad) * Math.cos(z_rad + Math.PI / 2);

	}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    // key listener
    @Override
    public void keyPressed(KeyEvent e) {
	if (e.getKeyCode() == KeyEvent.VK_W) {
	    px += ex;
	    py += ey;
	    pz += ez;
	}
	if (e.getKeyCode() == KeyEvent.VK_S) {
	    px -= ex;
	    py -= ey;
	    pz -= ez;
	}
	if (e.getKeyCode() == KeyEvent.VK_A) {
	    pz -= Math.cos(a_rad - Math.PI / 2);
	    px += Math.sin(a_rad - Math.PI / 2);
	}
	if (e.getKeyCode() == KeyEvent.VK_D) {
	    pz += Math.cos(a_rad - Math.PI / 2);
	    px -= Math.sin(a_rad - Math.PI / 2);
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD2) {
	    renderer.setNumberOfParticles(renderer.getNumberOfParticles() + 1);
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD0) {
	    renderer.setNumberOfParticles(renderer.getNumberOfParticles() - 1);
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD8) {
	    emitter.setWindX(new BigDecimal(emitter.getWindX().doubleValue() + 0.01));
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD5) {
	    emitter.setWindX(new BigDecimal(emitter.getWindX().doubleValue() - 0.01));
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD9) {
	    emitter.setWindZ(new BigDecimal(emitter.getWindZ().doubleValue() + 0.01));
	}
	if (e.getKeyCode() == KeyEvent.VK_NUMPAD6) {
	    emitter.setWindZ(new BigDecimal(emitter.getWindZ().doubleValue() - 0.01));
	}
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void dispose(GLAutoDrawable arg0) {
    }

    public String getParticlesCount() {
	return " Poèet vloèek: " + renderer.getNumberOfParticles() + " ";
    }

    public String getWindX() {
	return emitter != null ? " Vítr X =  " + emitter.getWindX().setScale(2, RoundingMode.CEILING) + " " : "";
    }

    public String getWindZ() {
	return emitter != null ? " Vítr Z =  " + emitter.getWindZ().setScale(2, RoundingMode.CEILING) + " " : "";
    }

}