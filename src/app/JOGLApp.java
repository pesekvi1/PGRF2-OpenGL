package app;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Z teto tridy se cela aplikace spousti
 * 
 * @author Viktor
 *
 */
public class JOGLApp {
    private static final int FPS = 60; // animator's target frames per second

    private GLCanvas canvas = null;
    private Canvas renderer = new Canvas();
    private Frame testFrame;
    private KeyAdapter keyAdapter;
    private JPanel panelRight = new JPanel();
    private JButton btnHelp = new JButton("Ovládání & Info o aplikaci");
    private JLabel lblParticles = new JLabel("");
    private JLabel lblWindX = new JLabel("");
    private JLabel lblWindZ = new JLabel("");
    private Timer t = new Timer(150, e -> update());

    public void start() {
	try {
	    t.start();
	    btnHelp.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    // zobrazeni panelu s navodem, jak ovladat aplikaci a informacemi o autorovi
		    JOptionPane.showMessageDialog(testFrame,
			    "OVLÁDÁNÍ APLIKACE \n " + "Pohyb pomocí W A S D \n"
				    + "Rozhled pomocí myší pøi stisknutém levém tlaèítku \n"
				    + "Pøidání vloèky NUMPAD2, odebrání NUMPAD0 \n"
				    + "Pro zesílení vetruX stisknìte NUMPAD8 \n" + "Pro zeslabení použjte NUMPAD5 \n"
				    + "Pro zesílení vetruZ použijte NUMPAD9 \n" + "Pro zeslabení použijte NUMPAD6 \n\n"
				    + "Viktor Pešek (c) FIM UHK PGRF2 \n" + "Poslední úprava dne: 30. 04. 2018",
			    "Ovládání", JOptionPane.INFORMATION_MESSAGE);
		}
	    });
	    testFrame = new Frame("TestFrame");
	    testFrame.setSize(512, 384);
	    setApp(testFrame, 1);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private void update() {
	lblParticles.setText(renderer.getParticlesCount());
	lblWindX.setText(renderer.getWindX());
	lblWindZ.setText(renderer.getWindZ());
    }

    private void setApp(Frame testFrame, int type) {
	Dimension dim;
	if (canvas != null) {
	    testFrame.remove(canvas);
	    dim = canvas.getSize();
	} else {
	    dim = new Dimension(800, 600);
	}

	// setup OpenGL Version 2
	GLProfile profile = GLProfile.get(GLProfile.GL2);
	GLCapabilities capabilities = new GLCapabilities(profile);
	capabilities.setRedBits(8);
	capabilities.setBlueBits(8);
	capabilities.setGreenBits(8);
	capabilities.setAlphaBits(8);
	capabilities.setDepthBits(24);

	canvas = new GLCanvas(capabilities);
	canvas.setSize(dim);
	testFrame.setLayout(new BorderLayout());
	testFrame.add(BorderLayout.CENTER, canvas);
	testFrame.add(panelRight, BorderLayout.EAST);

	canvas.addGLEventListener((GLEventListener) renderer);
	canvas.addKeyListener((KeyListener) renderer);
	canvas.addKeyListener(keyAdapter);
	canvas.addMouseListener((MouseListener) renderer);
	canvas.addMouseMotionListener((MouseMotionListener) renderer);
	canvas.requestFocus();
	panelRight.setLayout(new GridLayout(30, 1));
	panelRight.add(lblParticles);
	panelRight.add(lblWindX);
	panelRight.add(lblWindZ);
	panelRight.add(btnHelp);

	final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	testFrame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		new Thread() {
		    @Override
		    public void run() {
			if (animator.isStarted())
			    animator.stop();
			System.exit(0);
		    }
		}.start();
	    }
	});
	testFrame.setTitle(renderer.getClass().getName());
	testFrame.pack();
	testFrame.setVisible(true);
	animator.start(); // start the animation loop
    }

    public static void main(String[] args) {
	SwingUtilities.invokeLater(() -> new JOGLApp().start());
    }
}