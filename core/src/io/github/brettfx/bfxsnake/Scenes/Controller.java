package io.github.brettfx.bfxsnake.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.States.GameStateManager;
import io.github.brettfx.bfxsnake.States.PlayState;

/**
 * @author brett
 * @since 12/26/2017
 */

public class Controller {
    private static final String UP = "directional_pad/up_arrow.png";
    private static final String DOWN = "directional_pad/down_arrow.png";
    private static final String LEFT = "directional_pad/left_arrow.png";
    private static final String RIGHT = "directional_pad/right_arrow.png";
    private static final String PAUSE_BUTTON = "pause_button/pause_play_img_white.png";

    private static final float PADDING_TOP = 20f; //5
    private static final float PADDING_LEFT = 20f; //5
    private static final float PADDING_BOTTOM = 20f; //5
    private static final float PADDING_RIGHT = 20f; //5

    private static final float ARROW_WIDTH = 150f; //40
    private static final float ARROW_HEIGHT = 150f; //40

    private Viewport m_viewport;
    private Stage m_touchStage;

    private Image m_leftImg;
    private Image m_rightImg;
    private Image m_downImg;
    private Image m_upImg;

    private boolean m_usingController;

    private boolean m_upPressed,
            m_downPressed,
            m_leftPressed,
            m_rightPressed,
            m_pausedPressed;

    private  BitmapFont m_bitmapFont;

    private GameStateManager m_gsm;

    public Controller(GameStateManager gsm){
        m_gsm = gsm;

        OrthographicCamera cam = new OrthographicCamera();
        m_viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);

        m_touchStage = new Stage(m_viewport);

        m_usingController = m_gsm.isControllerOn();

        //Create up_arrow image
        m_upImg = new Image(new Texture(UP));
        m_upImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to up_arrow image to act as a button
        m_upImg.addListener(new InputListener()
        {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_upPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_upPressed = false;
            }
        });

        //Create down_arrow image
        m_downImg = new Image(new Texture(DOWN));
        m_downImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to down_arrow image to act as a button
        m_downImg.addListener(new InputListener(){


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_downPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_downPressed = false;
            }
        });

        //Create left_arrow image
        m_leftImg = new Image(new Texture(LEFT));
        m_leftImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to left_arrow image to act as a button
        m_leftImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_leftPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_leftPressed = false;
            }
        });

        //Create right_arrow image
        m_rightImg = new Image(new Texture(RIGHT));
        m_rightImg.setSize(ARROW_WIDTH, ARROW_HEIGHT);

        //Add input listener to right_arrow image to act as a button
        m_rightImg.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
            {
                //Need to return true in order for touchUp event to fire
                return (m_rightPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button)
            {
                m_rightPressed = false;
            }
        });

        m_bitmapFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        m_bitmapFont.getData().setScale(BFXSnake.DEF_FONT_SIZE, BFXSnake.DEF_FONT_SIZE);

        m_gsm.setTextButtonFont(m_bitmapFont);

        Image pauseButton = new Image(new Texture(PAUSE_BUTTON));
        pauseButton.setColor(pauseButton.getColor().r,
                pauseButton.getColor().g,
                pauseButton.getColor().b,
                BFXSnake.OPACITY);

        pauseButton.setSize(ARROW_WIDTH, ARROW_HEIGHT);
        pauseButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Need to return true in order for touchUp event to fire
                return (m_pausedPressed = true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                m_pausedPressed = false;
            }
        });

        Table pauseTable = new Table();
        pauseTable.setFillParent(true);
        pauseTable.bottom().right();
        pauseTable.add(pauseButton).size(pauseButton.getWidth(), pauseButton.getHeight());

        m_touchStage.addActor(pauseTable);

        //Create a table that will hold the controller arrows (3x3 matrix) if the controller is enabled
        if(m_usingController){
            Table controlsTable = new Table();
            controlsTable.left().bottom();

            //Populate the 3x3 table with arrow images to simulate directional pad controller
            controlsTable.add();
            controlsTable.add(m_upImg).size(m_upImg.getWidth(), m_upImg.getHeight());
            controlsTable.add();

            controlsTable.row().pad(PADDING_TOP, PADDING_LEFT, PADDING_BOTTOM, PADDING_RIGHT);

            controlsTable.add(m_leftImg).size(m_leftImg.getWidth(), m_leftImg.getHeight());
            controlsTable.add();
            controlsTable.add(m_rightImg).size(m_rightImg.getWidth(), m_rightImg.getHeight());

            controlsTable.row().padBottom(PADDING_BOTTOM);

            controlsTable.add();
            controlsTable.add(m_downImg).size(m_downImg.getWidth(), m_downImg.getHeight());
            controlsTable.add();

            controlsTable.pack();

            //Add the table to the m_touchStage
            m_touchStage.addActor(controlsTable);
        }

        if(PlayState.DEBUG_MODE){
            m_touchStage.setDebugAll(true);
        }
    }

    public GameStateManager getGSM(){
        return m_gsm;
    }

    public Stage getStage(){
        return m_touchStage;
    }

    public boolean isUsingController(){
        return m_usingController;
    }

    public void toggleUse(){
        m_usingController = !m_usingController;
    }

    public void draw(){
        m_touchStage.draw();
    }

    public boolean isDownPressed(){
        return m_downPressed;
    }

    public boolean isLeftPressed(){
        return m_leftPressed;
    }

    public boolean isUpPressed(){
        return m_upPressed;
    }

    public boolean isRightPressed(){
        return m_rightPressed;
    }

    public boolean isPausedPressed(){
        return m_pausedPressed;
    }

    @SuppressWarnings("unused")
    public void resize(int width, int height){
        m_viewport.update(width, height);
    }

    public float getWidth(){
        float widths = m_rightImg.getWidth() + m_leftImg.getHeight() + m_upImg.getWidth() + m_downImg.getWidth();
        return widths - (PADDING_RIGHT);
    }

    public void dispose(){
        m_touchStage.dispose();
        m_bitmapFont.dispose();
    }
}
