package com.repeatlet.views;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class RepeatletWallPaperService extends WallpaperService {

	private Queue<String> queue = new LinkedList<String>();

	@Override
	public Engine onCreateEngine() {
		return new RepeatletEngine();
	}

	class RepeatletEngine extends Engine {
		
		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			
			setTouchEventsEnabled(true);
			
			queue.clear();
			String filePath = Environment.getExternalStorageDirectory() + "/words.txt";
			File file = new File(filePath);
			FileInputStream fis;
			try {
				fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				String word;
				while ((word = br.readLine()) != null) {
					queue.add(word);
				}
				br.close();
				isr.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onSurfaceCreated(SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			doDraw(0, 0);
		}
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN) {
				doDraw((int)event.getX(), (int)event.getY());
			}
			super.onTouchEvent(event);
		}

		public void doDraw(int x, int y) {
			Canvas canvas = getSurfaceHolder().lockCanvas();
			
			Paint paint = new Paint();
			canvas.drawColor(Color.BLACK);
			paint.setTextSize(24);
			paint.setColor(Color.WHITE);
			
			String word = queue.remove();
			canvas.drawText(word, x, y, paint);
			queue.add(word);
			
			getSurfaceHolder().unlockCanvasAndPost(canvas);
		}
	}
}
