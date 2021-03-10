package com.timmith.deadtropolis;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.timmith.deadtropolis.sprites.Citizen;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.net.URISyntaxException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Deadtropolis extends ApplicationAdapter {
	SpriteBatch batch;
	private Socket socket;
	String id;
	Citizen player;
	Texture playerSkin;
	HashMap<String, Citizen> friendlyPlayers;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		playerSkin = new Texture("hero.png");
 		friendlyPlayers = new HashMap<String, Citizen>();

		connectSocket();
		configSocketEvents();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	public void handleInput(float dt) {
		if (player != null){
			if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
				player.setPosition(player.getX() + (-200 * dt), player.getY());
			} else if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
				player.setPosition(player.getX() + (200 * dt), player.getY());
			}
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		handleInput(Gdx.graphics.getDeltaTime());

		batch.begin();
		if (player != null){
			player.draw(batch);
		}

		for (HashMap.Entry<String, Citizen> entry : friendlyPlayers.entrySet()){
			entry.getValue().draw(batch);
		}

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

	public void configSocketEvents(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				player = new Citizen(playerSkin);
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: " + id);
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "New Player Connected: " + id);

					friendlyPlayers.put(id, new Citizen(playerSkin));

				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error getting New Player ID");
				}
			}
		});
	}

	@Override
	public void dispose () {
		batch.dispose();
		playerSkin.dispose();
	}


}
