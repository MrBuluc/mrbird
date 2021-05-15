package com.hakkicanbuluc.mrbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class MrBirdGame extends ApplicationAdapter {
	SpriteBatch batch;

	Texture background;
	Texture bird;
	Texture[] birdFrames;
	Texture bullet, bullet1, bullet2;

	int i = 0;
	int frame = 0;
	int gameState = 0;
	int numberOfEnemies = 4;
	int score = 0;
	int scoredEnemy = 0;

	float birdX, birdY;
	float velocity = 0;
	float gravity = 0.1f;
	float[] enemyX = new float[numberOfEnemies];
	float[] enemyyOffset = new float[numberOfEnemies];
	float[] enemyyOffset1 = new float[numberOfEnemies];
	float[] enemyyOffset2 = new float[numberOfEnemies];
	float distance = 0;
	float enemyVelocity = 8;

	Random random;

	Circle birdCircle;
	Circle[] enemyCircles;
	Circle[] enemyCircles1;
	Circle[] enemyCircles2;

	BitmapFont font, font1;

	//ShapeRenderer shapeRenderer;


	@Override
	public void create () {
		batch = new SpriteBatch();

		background = new Texture("background.png");

		bird = new Texture("bird0.png");
		birdFrames = new Texture[8];
		birdFrames[0] = new Texture("bird0.png");
		birdFrames[1] = new Texture("bird1.png");
		birdFrames[2] = new Texture("bird2.png");
		birdFrames[3] = new Texture("bird3.png");
		birdFrames[4] = new Texture("bird4.png");
		birdFrames[5] = new Texture("bird5.png");
		birdFrames[6] = new Texture("bird6.png");
		birdFrames[7] = new Texture("bird7.png");

		bullet = new Texture("bullet0.png");
		bullet1 = new Texture("bullet1.png");
		bullet2 = new Texture("bullet2.png");


		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		birdX = width / 3.0f - bird.getHeight() / 4.0f;
		birdY = height / 3.0f;

		//shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles1 = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];


		distance = width / 2.f;
		random = new Random() ;
		for (int i = 0; i < numberOfEnemies; i++) {
			enemyyOffset[i] = (random.nextFloat() - 0.5f) * (height - 200);
			enemyyOffset1[i] = (random.nextFloat() - 0.5f) * (height - 200);
			enemyyOffset2[i] = (random.nextFloat() - 0.5f) * (height - 200);

			enemyX[i] = width - bullet.getWidth() / 2f + i * distance;

			enemyCircles[i] = new Circle();
			enemyCircles1[i] = new Circle();
			enemyCircles2[i] = new Circle();


		}

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(4);
		font1 = new BitmapFont();
		font1.setColor(Color.WHITE);
		font1.getData().setScale(6);

	}

	@Override
	public void render () {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		batch.begin();
		batch.draw(background, 0, 0, width, height);

		if (gameState == 1) {
			if (enemyX[scoredEnemy] < birdX) {
				score++;
				if (scoredEnemy < numberOfEnemies - 1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}
			if (Gdx.input.justTouched()) {
				velocity = -6;
			}

			for (int i = 0; i < numberOfEnemies; i++) {
				if (enemyX[i] < 0) {
					enemyX[i] += numberOfEnemies * distance;

					enemyyOffset[i] = (random.nextFloat() - 0.5f) * (height - 200);
					enemyyOffset1[i] = (random.nextFloat() - 0.5f) * (height - 200);
					enemyyOffset2[i] = (random.nextFloat() - 0.5f) * (height - 200);

				} else {
					enemyX[i] -= enemyVelocity;
				}

				batch.draw(bullet, enemyX[i], height / 2f + enemyyOffset[i], width / 15f, height / 10f);
				batch.draw(bullet1, enemyX[i], height / 2f + enemyyOffset1[i], width / 15f, height / 10f);
				batch.draw(bullet2, enemyX[i], height / 2f + enemyyOffset2[i], width / 15f, height / 10f);

				enemyCircles[i] = new Circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset[i] + height / 20f,
						width / 30f);
				enemyCircles1[i] = new Circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset1[i] + height / 20f,
						width / 30f);
				enemyCircles2[i] = new Circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset2[i] + height / 20f,
						width / 30f);
			}

			frame = ++frame % 60;
			if (frame % 8 == 0) {
				bird = birdFrames[i];
				i = ++i % 8;
			}

			if (birdY > 0) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				gameState = 2;
			}

			if (birdY < height) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				birdY = height / 3f;
				gameState = 2;
			}



		} else if (gameState == 0) {
			font1.draw(batch, "Welcome to Mr. Bird! Tap to Play", 100, height / 2f);
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2) {
			font1.draw(batch, "Game Over! Tap to Play Again!", 100, height / 2f);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				birdY = height / 3f;
				velocity = 0;
				scoredEnemy = 0;
				score = 0;

				for (int i = 0; i < numberOfEnemies; i++) {
					enemyyOffset[i] = (random.nextFloat() - 0.5f) * (height - 200);
					enemyyOffset1[i] = (random.nextFloat() - 0.5f) * (height - 200);
					enemyyOffset2[i] = (random.nextFloat() - 0.5f) * (height - 200);

					enemyX[i] = width - bullet.getWidth() / 2f + i * distance;

					enemyCircles[i] = new Circle();
					enemyCircles1[i] = new Circle();
					enemyCircles2[i] = new Circle();


				}
			}
		}

		batch.draw(bird, birdX, birdY, width / 15f,
				height / 10f);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		birdCircle.set(birdX + width / 30f, birdY + height / 20f, width / 30f);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfEnemies; i++) {
			//shapeRenderer.circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset[i] + height / 20f,
			//		width / 30f);
			//shapeRenderer.circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset1[i] + height / 20f,
			//		width / 30f);
			//shapeRenderer.circle(enemyX[i] + width / 30f, height / 2f + enemyyOffset2[i] + height / 20f,
			//		width / 30f);
			if (Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles1[i]) ||
					Intersector.overlaps(birdCircle, enemyCircles2[i])) {
				gameState = 2;
			}
		}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {

	}
}
