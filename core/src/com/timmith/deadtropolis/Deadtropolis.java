package com.timmith.deadtropolis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.concurrent.ExecutionException;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Deadtropolis extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	private Socket socket;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		connectSocket();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	public void connectSocket(){
		try {
			socket = IO.socket("http://localhost:4001");
			socket.connect();
		}catch(URISyntaxException e){
			System.out.println(e);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}