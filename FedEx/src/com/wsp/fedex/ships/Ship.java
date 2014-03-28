package com.wsp.fedex.ships;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.wsp.fedex.fedex;
import com.wsp.fedex.objects.Beam;

public abstract class Ship {
	
	abstract void shoot();
	
	abstract void move();
	
	abstract void dispose(SpriteBatch batch, fedex gameIn);
	
	abstract void draw(SpriteBatch batch);
	
	abstract Rectangle getRectangle();
	
	abstract float getX();
	
	abstract float getY();
	
	abstract boolean checkShipCollision(Rectangle enemy);
	
	abstract boolean checkBeamCollision(Array<Beam> beams);
}
