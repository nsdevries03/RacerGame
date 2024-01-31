import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import javax.imageio.ImageIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class Asteroids {
    public Asteroids() {
        setup();
    }

    public static void setup() {
        appFrame = new JFrame("Asteroids");
        XOFFSET = 0;
        YOFFSET = 40;
        WINWIDTH = 500;
        WINHEIGHT = 500;
        pi = 3.1415926535;
        twoPi = 2 * 3.1415926535;
        endgame = false;
        p1width = 24;
        p1height = 24;
        p1originalX = (double)XOFFSET + ((double)WINWIDTH / 2.0) - (p1width / 2.0);
        p1originalY = (double)YOFFSET + ((double)WINHEIGHT / 2.0) - (p1height / 2.0);
        p2width = 24;
        p2height = 24;
        p2originalX = (double)XOFFSET + ((double)WINWIDTH / 2.0) - (p1width / 2.0);
        p2originalY = (double)YOFFSET + ((double)WINHEIGHT / 2.0) - (p1height / 2.0) + 30;
        playerBullets = new Vector<ImageObject>();
        playerBulletsTimes = new Vector<Long>();
        bulletWidth = 5;
        playerbulletlifetime = 1600L;
        playerbulletgap = 1;
        flamecount = 1;
        flamewidth = 12.0;

        try {
            background = ImageIO.read(new File("images/background.jpg"));
            player = ImageIO.read(new File("images/player1.png"));
            player2 = ImageIO.read(new File("images/player2.png"));
//            flame1 = ImageIO.read(new File("images/flame1.png"));
//            flame2 = ImageIO.read(new File("images/flame1.png"));
//            flame3 = ImageIO.read(new File("images/flame1.png"));
//            flame4 = ImageIO.read(new File("images/flame1.png"));
//            flame5 = ImageIO.read(new File("images/flame1.png"));
//            flame6 = ImageIO.read(new File("images/flame1.png"));
//            playerBullet = ImageIO.read(new File("images/playerbullet.png"));
        } catch (IOException ioe) {
            System.out.println("Error loading images");
        }
    }

    private static class Animate implements Runnable {
        public void run() {
            while (!endgame) {
                backgroundDraw();
//                playerBulletsDraw();
                playerDraw();
                player2Draw();
//                flameDraw();

                try {
                    Thread.sleep(32);
                } catch (InterruptedException e) {}
            }
        }
    }

    private static void insertPlayerBullet() {
        ImageObject bullet = new ImageObject(0, 0, bulletWidth, bulletWidth, p1.getAngle());
        lockrotateObjAroundObjtop(bullet, p1, p1width / 2.0);
        playerBullets.addElement(bullet);
        playerBulletsTimes.addElement(System.currentTimeMillis());
    }

    private static class PlayerMover implements Runnable {
        public PlayerMover() {
            velocitystep = 0.01;
            rotatestep = 0.01;
        }

        public void run() {
            while (!endgame) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}

                if (upPressed) p1velocity += velocitystep;
                if (downPressed) p1velocity -= velocitystep;
                if (leftPressed) {
                    if (p1velocity < 0) p1.rotate(-rotatestep);
                    else p1.rotate(rotatestep);
                }
                if (rightPressed) {
                    if (p1velocity < 0) p1.rotate(rotatestep);
                    else p1.rotate(-rotatestep);
                }
                if (firePressed) {
                    try {
                        if (playerBullets.isEmpty()) insertPlayerBullet();
                        else if (System.currentTimeMillis() - playerBulletsTimes.elementAt(playerBulletsTimes.size() - 1)
                                > playerbulletlifetime / 4.0) insertPlayerBullet();
                    } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {}
                }

                p1.move(-p1velocity * Math.cos(p1.getAngle() - pi / 2.0), p1velocity * Math.sin(p1.getAngle() - pi / 2.0));
                p1.screenWrap(XOFFSET, XOFFSET + WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
            }
        }

        private double velocitystep;
        private double rotatestep;
    }

    private static class Player2Mover implements Runnable {
        public Player2Mover() {
            velocitystep = 0.01;
            rotatestep = 0.01;
        }

        public void run() {
            while (!endgame) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {}

                if (wPressed) p2velocity += velocitystep;
                if (sPressed) p2velocity -= velocitystep;
                if (aPressed) {
                    if (p2velocity < 0) p2.rotate(-rotatestep);
                    else p2.rotate(rotatestep);
                }
                if (dPressed) {
                    if (p2velocity < 0) p2.rotate(rotatestep);
                    else p2.rotate(-rotatestep);
                }
                if (fire2Pressed) {
                    try {
                        if (playerBullets.isEmpty()) insertPlayerBullet();
                        else if (System.currentTimeMillis() - playerBulletsTimes.elementAt(playerBulletsTimes.size() - 1)
                                > playerbulletlifetime / 4.0) insertPlayerBullet();
                    } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {}
                }

                p2.move(-p2velocity * Math.cos(p2.getAngle() - pi / 2.0), p2velocity * Math.sin(p2.getAngle() - pi / 2.0));
                p2.screenWrap(XOFFSET, XOFFSET + WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
            }
        }

        private final double velocitystep;
        private final double rotatestep;
    }

//        private static class FlameMover implements Runnable {
//            public FlameMover() {
//                gap = 7.0;
//            }
//
//            public void run() {
//                while (!endgame) {
//                    lockrotateObjAroundObjbottom(flames, p1, gap);
//                }
//            }
//            private double gap;
//        }

//        private static class PlayerBulletsMover implements Runnable {
//            public PlayerBulletsMover() {
//                velocity = 1.0;
//            }
//
//            public void run() {
//                while (!endgame) {
//                    try {
//                        Thread.sleep(4);
//                    } catch (InterruptedException e) {
//                        System.out.println("Error sleeping");
//                    }
//
//                    try {
//                        for (int i = 0; i < playerBullets.size(); i++) {
//                            playerBullets.elementAt(i).move(-velocity * Math.cos(playerBullets.elementAt(i).getAngle() - pi / 2.0),
//                                    velocity * Math.sin(playerBullets.elementAt(i).getAngle() - pi / 2.0));
//                            playerBullets.elementAt(i).screenWrap(XOFFSET, XOFFSET + WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
//
//                            if (System.currentTimeMillis() - playerBulletsTimes.elementAt(i) > playerbulletlifetime) {
//                                playerBullets.remove(i);
//                                playerBulletsTimes.remove(i);
//                            }
//                        }
//
//
//                    } catch (java.lang.ArrayIndexOutOfBoundsException aie) {
//                        System.out.println("Error with playerBullets");
//                    }
//                }
//            }
//            private double velocity;
//        }

//        private static class CollisionChecker implements Runnable {
//            public void run() {
//                Random randomNumber = new Random(LocalTime.now().getNano());
//                while (!endgame) {
//                    try {
//                        for (int i = 0; i < asteroids.size(); i++) {
//                            for (int j = 0; j < playerBullets.size(); j++) {
//                                if (collisionOccurs(asteroids.elementAt(i), playerBullets.elementAt(j))) {
//                                    double posX = asteroids.elementAt(i).getX();
//                                    double posY = asteroids.elementAt(i).getY();
//
//                                    explosions.addElement(new ImageObject(posX, posY, 27, 24, 0.0));
//                                    explosionsTimes.addElement(System.currentTimeMillis());
//
//                                    if (asteroidsTypes.elementAt(i) == 1) {
//                                        asteroids.addElement(new ImageObject(posX, posY, ast2width, ast2width, (double)(randomNumber.nextInt(360))));
//                                        asteroidsTypes.addElement(2);
//                                        asteroids.remove(i);
//                                        asteroidsTypes.remove(i);
//                                        playerBullets.remove(j);
//                                        playerBulletsTimes.remove(j);
//                                    }
//
//                                    if (asteroidsTypes.elementAt(i) == 2) {
//                                        asteroids.addElement(new ImageObject(posX, posY, ast3width, ast3width, (double)(randomNumber.nextInt(360))));
//                                        asteroidsTypes.addElement(3);
//                                        asteroids.remove(i);
//                                        asteroidsTypes.remove(i);
//                                        playerBullets.remove(j);
//                                        playerBulletsTimes.remove(j);
//                                    }
//
//                                    if (asteroidsTypes.elementAt(i) == 3) {
//                                        asteroids.remove(i);
//                                        asteroidsTypes.remove(i);
//                                        playerBullets.remove(j);
//                                        playerBulletsTimes.remove(j);
//                                    }
//                                }
//                            }
//                        }
//
//                        for (int i = 0; i < asteroids.size(); i++) {
//                            if (collisionOccurs(asteroids.elementAt(i), p1)) {
//                                endgame = true;
//                                System.out.println("Game Over. You Lose!");
//                            }
//                        }
//                        try {
//                            for (int i = 0; i < playerBullets.size(); i++) {
//                                if (collisionOccurs(enemy, playerBullets.elementAt(i))) {
//                                    double posX = enemy.getX();
//                                    double posY = enemy.getY();
//
//                                    explosions.addElement(new ImageObject(posX, posY, 27, 24, 0.0));
//                                    explosionsTimes.addElement(System.currentTimeMillis());
//
//                                    playerBullets.remove(i);
//                                    playerBulletsTimes.remove(i);
//                                    enemyAlive = false;
//                                    enemy = null;
//                                    enemyBullets.clear();
//                                    enemyBulletsTimes.clear();
//                                }
//                            }
//
//                            if (collisionOccurs(enemy, p1)) {
//                                endgame = true;
//                                System.out.println("Game Over. You Lose!");
//                            }
//
//                            for (int i = 0; i < enemyBullets.size(); i++) {
//                                if (collisionOccurs(enemyBullets.elementAt(i), p1)) {
//                                    endgame = true;
//                                    System.out.println("Game Over. You Lose!");
//                                }
//                            }
//                        } catch (java.lang.NullPointerException jlnpe) {}
//
//                    } catch (java.lang.ArrayIndexOutOfBoundsException jlaioobe) {}
//                }
//            }
//        }

        private static class WinChecker implements Runnable {
            public void run() {
                while (!endgame) {
                    if (false) {
                        endgame = true;
                        System.out.println("Game Over. You Win!");
                    }
                }
            }
        }

        private static void lockrotateObjAroundObjbottom(ImageObject objOuter, ImageObject objInner, double dist) {
            objOuter.moveto(objInner.getX() + (dist + objInner.getWidth() / 2.0) * Math.cos(-objInner.getAngle() + pi / 2.0) + objOuter.getWidth() / 2.0,
                    objInner.getY() + (dist + objInner.getHeight() / 2.0) * Math.sin(-objInner.getAngle() + pi / 2.0) + objOuter.getHeight() / 2.0);
            objOuter.setAngle(objInner.getAngle());
        }

        private static void lockrotateObjAroundObjtop(ImageObject objOuter, ImageObject objInner, double dist) {
            objOuter.moveto(objInner.getX() + objOuter.getWidth() + (objInner.getWidth() / 2.0 + (dist + objInner.getWidth() / 2.0)
                    * Math.cos(objInner.getAngle() + pi / 2.0)) / 2.0, objInner.getY() - objOuter.getHeight() + (dist + objInner.getHeight() / 2.0)
                    * Math.sin(objInner.getAngle() / 2.0));

            objOuter.setAngle(objInner.getAngle());
        }

        private static AffineTransformOp rotateImageObject(ImageObject obj) {
            AffineTransform at = AffineTransform.getRotateInstance(-obj.getAngle(), obj.getWidth() / 2.0, obj.getHeight() / 2.0);
            AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            return atop;
        }

        private static AffineTransformOp spinImageObject(ImageObject obj) {
            AffineTransform at = AffineTransform.getRotateInstance(-obj.getInternalAngle(), obj.getWidth() / 2.0, obj.getHeight() / 2.0);
            AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            return atop;
        }

        private static void backgroundDraw() {
            Graphics g = appFrame.getGraphics();
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(background, XOFFSET, YOFFSET, null);
        }

//        private static void playerBulletsDraw() {
//            Graphics g = appFrame.getGraphics();
//            Graphics2D g2D = (Graphics2D) g;
//            try {
//                for (int i = 0; i < playerBullets.size(); i++) {
//                    g2D.drawImage(rotateImageObject(playerBullets.elementAt(i)).filter(playerBullet, null), (int)(playerBullets.elementAt(i).getX() + 0.5),
//                            (int)(playerBullets.elementAt(i).getY() + 0.5), null);
//                }
//            } catch (java.lang.ArrayIndexOutOfBoundsException aioobe) {
//                playerBullets.clear();
//                playerBulletsTimes.clear();
//            }
//        }

        private static void playerDraw() {
            Graphics g = appFrame.getGraphics();
            Graphics2D g2D = (Graphics2D) g;
            g2D.drawImage(rotateImageObject(p1).filter(player, null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
        }

    private static void player2Draw() {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;
        g2D.drawImage(rotateImageObject(p2).filter(player2, null), (int)(p2.getX() + 0.5), (int)(p2.getY() + 0.5), null);
    }

//        private static void flameDraw() {
//            if (upPressed) {
//                Graphics g = appFrame.getGraphics();
//                Graphics2D g2D = (Graphics2D) g;
//                if (flamecount == 1) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame1, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                } else if (flamecount == 2) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame2, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                } else if (flamecount == 3) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame3, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                }
//            }
//            if (downPressed) {
//                Graphics g = appFrame.getGraphics();
//                Graphics2D g2D = (Graphics2D) g;
//                if (flamecount == 1) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame4, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                } else if (flamecount == 2) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame5, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                } else if (flamecount == 3) {
//                    g2D.drawImage(rotateImageObject(flames).filter(flame6, null), (int)(flames.getX() + 0.5), (int)(flames.getY() + 0.5), null);
//                    flamecount = 1 + ((flamecount + 1) % 3);
//                }
//            }
//        }

        private static class KeyPressed extends AbstractAction {
            public KeyPressed() {
                action = "";
            }

            public KeyPressed(String input) {
                action = input;
            }

            public void actionPerformed(ActionEvent e) {
                if (action.equals("UP")) upPressed = true;
                if (action.equals("DOWN")) downPressed = true;
                if (action.equals("LEFT")) leftPressed = true;
                if (action.equals("RIGHT")) rightPressed = true;
//                if (action.equals("F")) firePressed = true;
                if (action.equals("W")) wPressed = true;
                if (action.equals("S")) sPressed = true;
                if (action.equals("A")) aPressed = true;
                if (action.equals("D")) dPressed = true;
            }
            private String action;
        }

        private static class KeyReleased extends AbstractAction {
            public KeyReleased() {
                action = "";
            }

            public KeyReleased(String input) {
                action = input;
            }

            public void actionPerformed(ActionEvent e) {
                if (action.equals("UP")) upPressed = false;
                if (action.equals("DOWN")) downPressed = false;
                if (action.equals("LEFT")) leftPressed = false;
                if (action.equals("RIGHT")) rightPressed = false;
//                if (action.equals("F")) firePressed = false;
                if (action.equals("W")) wPressed = false;
                if (action.equals("S")) sPressed = false;
                if (action.equals("A")) aPressed = false;
                if (action.equals("D")) dPressed = false;
            }
            private String action;
        }

        private static class QuitGame implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                endgame = true;
            }
        }

        private static class StartGame implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                endgame = true;

                upPressed = false;
                downPressed = false;
                leftPressed = false;
                rightPressed = false;
                firePressed = false;
                p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0.0);
                p1velocity = 0.0;

                wPressed = false;
                sPressed = false;
                aPressed = false;
                dPressed = false;
                fire2Pressed = false;
                p2 = new ImageObject(p2originalX, p2originalY, p2width, p2height, 0.0);
                p2velocity = 0.0;

                flames = new ImageObject(p1originalX + p1width / 2.0, p1originalY + p1height, flamewidth, flamewidth, 0.0);
                flamecount = 1;
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {}
                playerBullets = new Vector<ImageObject>();
                playerBulletsTimes = new Vector<Long>();
                endgame = false;
                Thread t1 = new Thread(new Animate());
                Thread t2 = new Thread(new PlayerMover());
                Thread t3 = new Thread(new Player2Mover());
                Thread t9 = new Thread(new WinChecker());
                t1.start();
                t2.start();
                t3.start();
                t9.start();
            }
        }

        private static Boolean isInside(double p1x, double p1y, double p2x1, double p2y1, double p2x2, double p2y2) {
            boolean ret = false;
            if (p1x > p2x1 && p1x < p2x2) {
                if (p1y > p2y1 && p1y < p2y2) ret = true;
                if (p1y > p2y2 && p1y < p2y1) ret = true;
            }
            if (p1x > p2x2 && p1x < p2x1) {
                if (p1y > p2y1 && p1y < p2y2) ret = true;
                if (p1y > p2y2 && p1y < p2y1) ret = true;
            }
            return ret;
        }

        private static Boolean collisionOccursCoordinates (double p1x1, double p1y1, double p1x2, double p1y2, double p2x1, double p2y1, double p2x2, double p2y2) {
            Boolean ret = false;
            if (isInside(p1x1, p1y1, p2x1, p2y1, p2x2, p2y2)) ret = true;
            if (isInside(p1x1, p1y2, p2x1, p2y1, p2x2, p2y2)) ret = true;
            if (isInside(p1x2, p1y1, p2x1, p2y1, p2x2, p2y2)) ret = true;
            if (isInside(p1x2, p1y2, p1x1, p2y1, p2x2, p2y2)) ret = true;
            if (isInside(p2x1, p2y1, p1x1, p1y1, p1x2, p1y2)) ret = true;
            if (isInside(p2x1, p2y2, p1x1, p1y1, p1x2, p1y2)) ret = true;
            if (isInside(p2x2, p2y1, p1x1, p1y1, p1x2, p1y2)) ret = true;
            if (isInside(p2x2, p2y2, p1x1, p1y1, p1x2, p1y2)) ret = true;
            return ret;
        }

        private static Boolean collisionOccurs(ImageObject obj1, ImageObject obj2) {
            Boolean ret = false;
            if (collisionOccursCoordinates(obj1.getX(), obj1.getY(), obj1.getX() + obj1.getWidth(), obj1.getY() + obj1.getHeight(),
                    obj2.getX(), obj2.getY(), obj2.getX() + obj2.getWidth(), obj2.getY() + obj2.getHeight()))
                ret = true;
            return ret;
        }

        private static class ImageObject {
            public ImageObject() {
            }

            public ImageObject(double xinput, double yinput, double xwidthinput, double yheightinput, double angleinput) {
                x = xinput;
                y = yinput;
                xwidth = xwidthinput;
                yheight = yheightinput;
                angle = angleinput;
                internalangle = 0.0;
                coords = new Vector<Double>();
            }

            public double getX() {
                return x;
            }

            public double getY() {
                return y;
            }

            public double getWidth() {
                return xwidth;
            }

            public double getHeight() {
                return yheight;
            }

            public double getAngle() {
                return angle;
            }

            public double getInternalAngle() {
                return internalangle;
            }

            public void setAngle(double angleinput) {
                angle = angleinput;
            }

            public void setInternalAngle(double internalangleoutput) {
                internalangle = internalangleoutput;
            }

            public Vector<Double> getCoords() {
                return coords;
            }

            public void setCoords(Vector<Double> coordsinput) {
                coords = coordsinput;
                generateTriangles();
            }

            public void generateTriangles() {
                triangles = new Vector<Double>();
                comX = getComX();
                comY = getComY();
                for (int i = 0; i < coords.size(); i += 2) {
                    triangles.addElement(coords.elementAt(i));
                    triangles.addElement(coords.elementAt(i + 1));
                    triangles.addElement(coords.elementAt((i + 2) % coords.size()));
                    triangles.addElement(coords.elementAt((i + 3) % coords.size()));
                    triangles.addElement(comX);
                    triangles.addElement(comY);
                }
            }

            public void printTriangles() {
                for (int i = 0; i < coords.size(); i += 6) {
                    System.out.print("p0x: " + triangles.elementAt(i) + " , p0y: " + triangles.elementAt(i + 1));
                    System.out.print(" p1x: " + triangles.elementAt(i + 2) + " , p1y: " + triangles.elementAt(i + 3));
                    System.out.print(" p2x: " + triangles.elementAt(i + 4) + " , p2y: " + triangles.elementAt(i + 5));
                }
            }

            public double getComX() {
                double ret = 0;
                if (coords.size() > 0) {
                    for (int i = 0; i < coords.size(); i += 2) {
                        ret += coords.elementAt(i);
                    }
                    ret = ret / (coords.size() / 2.0);
                }
                return ret;
            }

            public double getComY() {
                double ret = 0;
                if (coords.size() > 0) {
                    for (int i = 1; i < coords.size(); i += 2) {
                        ret += coords.elementAt(i);
                    }
                    ret = ret / (coords.size() / 2.0);
                }
                return ret;
            }

            public void move(double xinput, double yinput) {
                x += xinput;
                y += yinput;
            }

            public void moveto(double xinput, double yinput) {
                x = xinput;
                y = yinput;
            }

            public void screenWrap(double leftEdge, double rightEdge, double topEdge, double bottomEdge) {
                if (x > rightEdge) moveto(leftEdge, getY());
                if (x < leftEdge) moveto(rightEdge, getY());
                if (y > bottomEdge) moveto(getX(), topEdge);
                if (y < topEdge) moveto(getX(), bottomEdge);
            }

            public void rotate(double angleinput) {
                angle += angleinput;
                while (angle > twoPi) {
                    angle -= twoPi;
                }

                while (angle < 0) {
                    angle += twoPi;
                }
            }

            public void spin(double internalangleinput) {
                internalangle += internalangleinput;
                while (internalangle > twoPi) {
                    internalangle -= twoPi;
                }

                while (internalangle < 0) {
                    internalangle += twoPi;
                }
            }

            private double x;
            private double y;
            private double xwidth;
            private double yheight;
            private double angle;
            private double internalangle;
            private Vector<Double> coords;
            private Vector<Double> triangles;
            private double comX;
            private double comY;
        }

        private static void bindKey(JPanel myPanel, String input) {
            myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " + input), input + " pressed");
            myPanel.getActionMap().put(input + " pressed", new KeyPressed(input));
            myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + input), "released " + input);
            myPanel.getActionMap().put("released " + input, new KeyReleased(input));

        }
        public static void main(String[] args) {
            setup();
            appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            appFrame.setSize(501, 585);

            JPanel myPanel = new JPanel();

            JButton newGameButton = new JButton("New Game");
            newGameButton.addActionListener(new StartGame());
            myPanel.add(newGameButton);

            JButton quitButton = new JButton("Quit Game");
            quitButton.addActionListener(new QuitGame());
            myPanel.add(quitButton);

            bindKey(myPanel, "UP");
            bindKey(myPanel, "DOWN");
            bindKey(myPanel, "LEFT");
            bindKey(myPanel, "RIGHT");
            bindKey(myPanel, "F");

            bindKey(myPanel, "W");
            bindKey(myPanel, "S");
            bindKey(myPanel, "A");
            bindKey(myPanel, "D");
            bindKey(myPanel, "F");

            appFrame.getContentPane().add(myPanel, "South");
            appFrame.setVisible(true);
    }


    private static Boolean endgame;
    private static BufferedImage background;
    private static BufferedImage player;
    private static BufferedImage player2;

    private static Boolean upPressed;
    private static Boolean downPressed;
    private static Boolean leftPressed;
    private static Boolean rightPressed;
    private static Boolean firePressed;
    private static Boolean wPressed;
    private static Boolean sPressed;
    private static Boolean aPressed;
    private static Boolean dPressed;
    private static Boolean fire2Pressed;

    private static ImageObject p1;
    private static double p1width;
    private static double p1height;
    private static double p1originalX;
    private static double p1originalY;
    private static double p1velocity;

    private static ImageObject p2;
    private static double p2width;
    private static double p2height;
    private static double p2originalX;
    private static double p2originalY;
    private static double p2velocity;

    private static Vector<ImageObject> playerBullets;
    private static Vector<Long> playerBulletsTimes;
    private static double bulletWidth;
    private static BufferedImage playerBullet;
    private static long playerbulletlifetime;
    private static double playerbulletgap;

    private static ImageObject flames;
    private static BufferedImage flame1;
    private static BufferedImage flame2;
    private static BufferedImage flame3;
    private static BufferedImage flame4;
    private static BufferedImage flame5;
    private static BufferedImage flame6;
    private static int flamecount;
    private static double flamewidth;

    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;

    private static double pi;
    private static double twoPi;

    private static JFrame appFrame;

    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
}
