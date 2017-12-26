package io.github.brettfx.bfxsnake.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

/**
 * @author Brett Allen
 * @since 12/24/2017
 */

public class GameStateManager {

    //A stack of states for more efficient memory utilization
    private Stack<State> m_states;

    public GameStateManager(){
        m_states = new Stack<State>();
    }

    public void push(State state){
        m_states.push(state);
    }

    public void pop(){
        m_states.pop();
    }

    public void set(State state){
        m_states.pop();
        m_states.push(state);
    }

    public void update(float dt){
        m_states.peek().update(dt);
    }

    public void render(SpriteBatch sb){
        m_states.peek().render(sb);
    }
}