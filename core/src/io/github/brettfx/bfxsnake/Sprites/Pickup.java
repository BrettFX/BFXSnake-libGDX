package io.github.brettfx.bfxsnake.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

/**
 * @author brett
 * @since 12/25/2017
 *
 * Pick ups are what the snake is supposed to eat to grow
 * Each time a pick up is eaten by the snake, the snake will grow in size.
 * Once the snake has eaten all the pickups a special prize will be won.
 */
public class Pickup {
    public static boolean DEBUG_MODE = false;

    private Rectangle m_food;
    private Rectangle m_bounds;
    private Random m_rand;

    private int m_minHeight;
    private int m_maxHeight;
    private int m_minWidth;
    private int m_maxWidth;

    public Pickup(SnakePart part){
        int width = part.getWidth();
        int height = part.getHeight();

        m_maxHeight = Gdx.graphics.getHeight() - width;
        m_maxWidth = Gdx.graphics.getWidth() - height;
        m_minHeight = 0;
        m_minWidth = 0;

        m_rand = new Random();

        int randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
        int randY = m_rand.nextInt(m_maxHeight) + m_minHeight;

        m_food = new Rectangle(randX, randY, width / 2, height / 2);
        m_bounds = new Rectangle(randX, randY, width / 2, height / 2);
    }

    /**
     * Collect the pickup when the snake has collided with it.
     * */
    public void collect(){
        int randX = m_rand.nextInt(m_maxWidth) + m_minWidth;
        int randY = m_rand.nextInt(m_maxHeight) + m_minHeight;

        m_food.setX(randX);
        m_food.setY(randY);
        m_bounds.setX(randX);
        m_bounds.setY(randY);
    }

    /**
     * Determine if the snake has collided with the pickup
     * */
    public boolean shouldCollect(SnakePart part){
        int locDiff = Math.abs(((int)m_bounds.getX() + (int)m_bounds.getY()) - (part.getX() + part.getY()));

        if(DEBUG_MODE){
            System.out.print("bounds x = " + (int)m_bounds.getX() + "; bounds y = " + (int)m_bounds.getY());
            System.out.print("\t\tPickup x = " + part.getX() + "; Pickup y = " + part.getY());
            System.out.print("\t\tLocation Difference = " + locDiff);
            //System.out.println("; collided = " + (locDiff <= part.getWidth() / 2));
            System.out.println();
        }

        return part.getBounds().overlaps(m_bounds);
    }

    public int getX(){
        return (int)m_bounds.getX();
    }

    public int getY(){
        return (int)m_bounds.getY();
    }

    public int getWidth(){
        return (int)m_bounds.getWidth();
    }

    public int getHeight(){
        return (int)m_bounds.getHeight();
    }
}
