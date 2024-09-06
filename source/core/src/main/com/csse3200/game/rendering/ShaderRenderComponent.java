package com.csse3200.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class ShaderRenderComponent extends RenderComponent {
	private ShaderProgram shader;
	private float redIntensity = 0.5f;
	
	public ShaderRenderComponent() {
		FileHandle vertexShader = Gdx.files.internal("shaders/redtint.vert");
		FileHandle fragmentShader = Gdx.files.internal("shaders/redtint.frag");
		
		shader = new ShaderProgram(vertexShader, fragmentShader);
		if (!shader.isCompiled()) {
			Gdx.app.error("Shader", "Error compiling shader: " + shader.getLog());
		}
	}
	
	@Override
	protected void draw(SpriteBatch batch) {
		// Store the current shader
		ShaderProgram currentShader = batch.getShader();
		
		// Set and update our shader
		batch.setShader(shader);
		shader.setUniformf("u_redIntensity", redIntensity);
		
		// Call the actual drawing logic
		drawWithShader(batch);
		
		// Restore the previous shader
		batch.setShader(currentShader);
	}
	
	// This method should be implemented in subclasses to define the actual drawing logic
	protected abstract void drawWithShader(SpriteBatch batch);
	
	public void setRedIntensity(float intensity) {
		this.redIntensity = intensity;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		shader.dispose();
	}
}