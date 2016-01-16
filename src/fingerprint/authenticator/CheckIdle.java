/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fingerprint.authenticator;


import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class CheckIdle extends Thread {
    private Robot robot;
    private double threshHold = 0.05;
    private int activeTime;
    private int idleTime;
    private boolean idle;
    private Rectangle screenDimenstions;

    public CheckIdle(int activeTime, int idleTime) {
        this.activeTime = activeTime;
        this.idleTime = idleTime;

        // Get the screen dimensions
        // MultiMonitor support.
        int screenWidth = 0;
        int screenHeight = 0;

        GraphicsEnvironment graphicsEnv = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] graphicsDevices = graphicsEnv.getScreenDevices();

        for (GraphicsDevice screens : graphicsDevices) {
            DisplayMode mode = screens.getDisplayMode();
            screenWidth += mode.getWidth();

            if (mode.getHeight() > screenHeight) {
                screenHeight = mode.getHeight();
            }
        }

        screenDimenstions = new Rectangle(0, 0, screenWidth, screenHeight);

        // setup the robot.
        robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

        idle = false;
    }

    public void run() {
        while (true) {
            BufferedImage screenShot = robot
                    .createScreenCapture(screenDimenstions);

            try {
                Thread.sleep(idle ? idleTime : activeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            BufferedImage screenShot2 = robot
                    .createScreenCapture(screenDimenstions);

            if (compareScreens(screenShot, screenShot2) < threshHold) {
                idle = true;
            } else {
                idle = false;
            }
        }
    }

    private double compareScreens(BufferedImage screen1, BufferedImage screen2) {
        int counter = 0;
        boolean changed = false;

        // Count the amount of change.
        for (int i = 0; i < screen1.getWidth() && !changed; i++) {
            for (int j = 0; j < screen1.getHeight(); j++) {
                if (screen1.getRGB(i, j) != screen2.getRGB(i, j)) {
                    counter++;
                }
            }
        }

        return (double) counter
                / (double) (screen1.getHeight() * screen1.getWidth()) * 100;
    }

    public static void main(String[] args) {
        CheckIdle idleChecker = new CheckIdle(20000, 1000);
        idleChecker.run();
    }
}