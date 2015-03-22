package com.felix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;

import junit.framework.TestCase;

import static org.mockito.Mockito.mock;

/**
 * Created by fx on 3/14/2015.
 */
public class ImageFactoryTest extends TestCase {

    protected void setUp() throws Exception {
        //Launch the program and initialise all Gdx stuff (invisible and no program is really launched)
        new HeadlessApplication(new EmptyApplication());
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

    }
    public void testGetGotenku(){
        Drawable gotenku = ImageFactory.gotenkuLite();

        assertNotNull(gotenku);

    }
}
