package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.github.brettfx.bfxsnake.BFXSnake;
import io.github.brettfx.bfxsnake.Components.Score;
import io.github.brettfx.bfxsnake.Scenes.Controller;
import io.github.brettfx.bfxsnake.Sprites.Pickup;
import io.github.brettfx.bfxsnake.Sprites.Snake;
import io.github.brettfx.bfxsnake.Sprites.SnakePart;

/**
 * @author brett
 * @since 12/24/2017
 */
public class PlayState extends State {

    public static boolean DEBUG_MODE = false;

    private Snake m_snake;

    private Controller m_controller;

    private boolean m_gameOver;

    private Stage m_gameOverStage;
    private Stage m_scoreStage;

    private GameStateManager m_gsm;

    private Label m_playAgainLabel;
    private Label m_backLabel;

    //TODO display score while playing and update high score when needed
    private Label m_scoreLabel;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        m_gsm = gsm;

        m_gsm.saveDifficulty();
        m_gsm.flush();

        m_controller = new Controller(m_gsm.isControllerOn());

        m_snake = new Snake(m_controller);

        //Determine difficulty value
        int difficultyVal = 0;

        //Possible difficulty vlaues: 0, 1, 2, 3, 4
        //Diffculty value will be subtracted from snake's initial movement speed
        switch(m_gsm.getDifficulty()){
            case 0: //EASY
                difficultyVal = 0;
                break;

            case 1: //MEDIUM
                difficultyVal = 10;
                break;

            case 2: //HARD
                difficultyVal = 16;
                break;

            case 3: //PRO
                difficultyVal = 20;
                break;

            case 4: //INSANE
                difficultyVal = 24;
                break;

            default:
                break;
        }

        m_snake.setDifficultyVal(difficultyVal);

        Viewport gameOverViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_gameOverStage = new Stage(gameOverViewport);

        Viewport scoreViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), m_cam);
        m_scoreStage = new Stage(scoreViewport);

        Gdx.input.setInputProcessor(m_gameOverStage);

        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal(BFXSnake.MENU_FONT));
        bitmapFont.getData().setScale(BFXSnake.FONT_SIZE, BFXSnake.FONT_SIZE);

        //Setting up the font of the text to be displayed on the gameover screen
        Label.LabelStyle font = new Label.LabelStyle(bitmapFont, bitmapFont.getColor());

        Table gameOverTable = new Table();

        gameOverTable.top().padTop(Gdx.graphics.getHeight() / 4);

        //Table will take up entire stage
        gameOverTable.setFillParent(true);

        //Create game over label
        Label gameOverLabel = new Label("GAME OVER", font);
        gameOverTable.add(gameOverLabel).expandX();

        //Create a play again label
        m_playAgainLabel = new Label("PLAY AGAIN", font);
        m_playAgainLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.set(new PlayState(m_gsm));
            }
        });

        gameOverTable.row();

        //Add a bit of padding to top of play again label
        gameOverTable.add(m_playAgainLabel).expandX().padTop(Gdx.graphics.getHeight() / 8);

        m_backLabel = new Label("BACK TO MENU", font);
        m_backLabel.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                //Need to return true in order for touchUp event to fire
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                m_gsm.set(new MenuState(m_gsm));
            }
        });

        gameOverTable.row();

        //Add padding that is equivalent to the height of the head of the snake
        gameOverTable.add(m_backLabel).expandX().padTop(m_snake.getSnake().get(0).getHeight());

        m_gameOverStage.addActor(gameOverTable);

        m_gameOver = false;

        Table scoreTable = new Table();
        scoreTable.bottom().right();
        scoreTable.setFillParent(true);

        m_scoreLabel = new Label(String.valueOf(m_gsm.getScore().getCurrentScore()), font);

        scoreTable.add(m_scoreLabel);

        m_scoreStage.addActor(scoreTable);

        if(DEBUG_MODE){
            m_gameOverStage.setDebugAll(true);
            m_scoreStage.setDebugAll(true);
        }
    }

    @Override
    public void handleInput() {
        if(DEBUG_MODE){
            if(Gdx.input.justTouched()){
                System.out.println("Just touched at: x = " + Gdx.input.getX() + ", y = " + Gdx.input.getY());
                System.out.print("Current snake head location: x = " + m_snake.getSnake().get(0).getX());
                System.out.println(", y = " + m_snake.getSnake().get(0).getY());
                System.out.println();
            }
        }

        if(!m_snake.isPaused()){
            if(m_controller.isUsingController()){
                if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN ) || Gdx.input.isKeyJustPressed(Input.Keys.S)
                        || (m_controller.isDownPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.DOWN);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)
                        || (m_controller.isUpPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.UP);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)
                        || (m_controller.isLeftPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.LEFT);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)
                        || (m_controller.isRightPressed() && Gdx.input.justTouched())){

                    m_snake.setDirection(Snake.Directions.RIGHT);
                    
                }else if((m_controller.isPausedPressed() && Gdx.input.justTouched()) || Gdx.input.isKeyJustPressed(Input.Keys.P)){
                    m_snake.pause();
                }
            }else{ //Handle case when controller is disabled
                if(Gdx.input.justTouched()){
                    if(m_gameOver){
                        return;
                    }

                    if(m_controller.isPausedPressed()){
                        m_snake.pause();
                        m_controller.setExitVisibility(true);
                        return;
                    }

                    SnakePart head = m_snake.getSnake().get(0);

                    int touchLocX = Gdx.input.getX();
                    int touchLocY = Gdx.graphics.getHeight() - Gdx.input.getY(); //Invert y-axis
                    int snakeHeadX = (int)head.getX();
                    int snakeHeadY = (int)head.getY();

                    //By default, assume same location
                    Snake.Directions currentDirection = head.getDirection();
                    Snake.Directions d1 = currentDirection;
                    Snake.Directions d2 = currentDirection;
                    Snake.Directions deducedDirection;

                    //Calculate as best as possible, the two most logical directions that the user meant the snake to go in.
                    //NB: should not do anything if the user taps directly on the snake head
                    if(touchLocX < snakeHeadX){
                        d1 = Snake.Directions.LEFT;
                    }else if(touchLocX > snakeHeadX){
                        d1 = Snake.Directions.RIGHT;
                    }

                    if(touchLocY < snakeHeadY){
                        d2 = Snake.Directions.DOWN;
                    }else if(touchLocY > snakeHeadY){
                        d2 = Snake.Directions.UP;
                    }

                    //Eliminate one of those directions by determining which one is an opposite direction or the same as the
                    //current direction
                    //NB: by mathematical deduction, one of the two directions will always be either an opposite direction
                    //or the same as the current direction
                    if(d1 != currentDirection && !m_snake.isOpposite(d1)){
                        deducedDirection = d1;
                    }else{
                        deducedDirection = d2;
                    }

                    //Make the snake go in the deduced direction
                    m_snake.setDirection(deducedDirection);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN ) || Gdx.input.isKeyJustPressed(Input.Keys.S)){
                    m_snake.setDirection(Snake.Directions.DOWN);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)){
                    m_snake.setDirection(Snake.Directions.UP);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.A)){
                    m_snake.setDirection(Snake.Directions.LEFT);

                }else if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.D)){
                    m_snake.setDirection(Snake.Directions.RIGHT);
                }else if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
                    m_snake.pause();
                    m_controller.setExitVisibility(true);
                }
            }

            //Debugging tools
            if(DEBUG_MODE){
                if(Gdx.input.isKeyJustPressed(Input.Keys.G)) { //Allow debugger to grow snake at will
                    m_snake.grow();
                }else if(Gdx.input.isKeyJustPressed(Input.Keys.C)){ //Toggle controller
                    m_controller.toggleUse();
                }
            }

        }else if((m_controller.isPausedPressed() && Gdx.input.justTouched()) || Gdx.input.isKeyJustPressed(Input.Keys.P)){
            m_snake.resume();
            m_controller.setExitVisibility(false);
        }else if(m_controller.isExitPressed() && Gdx.input.justTouched()){
            m_gsm.set(new MenuState(m_gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        m_snake.update(dt);
        m_gameOver = m_snake.isColliding();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(m_cam.combined);
        sb.begin();

        ShapeRenderer sr = m_snake.getShapeRenderer();
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Filled);

        //Render controller's overlay
        if(m_controller.isUsingController()){
            sr.setColor(Color.GRAY);
            sr.rect(0, 0, m_controller.getWidth(), Gdx.graphics.getHeight());
        }

        //Render pickup
        sr.setColor(m_gsm.getPickupColor());
        Pickup pickup = m_snake.getPickup();
        sr.rect(pickup.getX(), pickup.getY(), pickup.getWidth(), pickup.getHeight());

        //Render snake
        sr.setColor(m_gsm.getSnakeColor());

        for(SnakePart part : m_snake.getSnake()){
            sr.rect(part.getX(), part.getY(), part.getWidth(), part.getHeight());
        }

        sr.end();
        sb.end();

        //Draw the controller
        m_controller.draw();

        //Draw the score
        m_scoreStage.draw();

        //Determine if game over and show game over if it is game over
        if(m_gameOver){
            int width = (int)(m_backLabel.getWidth() + (m_playAgainLabel.getWidth() / 2));
            int height = (int)m_backLabel.getHeight();
            float x = m_backLabel.getX() - (m_playAgainLabel.getWidth() / 4);

            sb.begin();

            //Render button overlay for play again button
            Texture playAgainTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                    BFXSnake.BUTTON_COLOR));
            sb.draw(playAgainTexture, x, m_playAgainLabel.getY());

            //Render button overlay for back button
            Texture backTexture = new Texture(m_gsm.getPixmapRoundedRectangle(width, height, height / 2,
                    BFXSnake.BUTTON_COLOR));
            sb.draw(backTexture, x, m_backLabel.getY());

            sb.end();

            m_gameOverStage.draw();
        }
    }

    @Override
    public void dispose() {
        m_snake.dispose();
        m_controller.dispose();
        m_gameOverStage.dispose();
        m_scoreStage.dispose();
    }
}
