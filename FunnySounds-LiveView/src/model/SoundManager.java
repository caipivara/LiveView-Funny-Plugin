package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.experience.nielda.music.SoundPlayer;

public class SoundManager implements Runnable {
	// ****************************************************************
	// Attributes
	// ****************************************************************

	// Our handler.
	private Handler mHandler = null;

	private final static String SOUNDS_FILE = "sounds.xml";
	private ArrayList<Sound> sounds;
	private int actualSound;
	private Context context;

	// ****************************************************************
	// Constructor
	// ****************************************************************

	public SoundManager(Context context) {

		this.context = context;
		this.mHandler = new Handler();
		actualSound = 0;
		loadSounds();
	}

	// ****************************************************************
	// Accessor Methods
	// ****************************************************************

	public String getActualSound() {
		return sounds.get(actualSound).getName();
	}

	public String getNextSound() {

		if (++actualSound >= sounds.size()) {
			actualSound = 0;
		}

		return sounds.get(actualSound).getName();

	}

	public String getPastSound() {

		if (--actualSound < 0) {
			actualSound = sounds.size() - 1;
		}

		return sounds.get(actualSound).getName();

	}

	// ****************************************************************
	// XML Setup load
	// ****************************************************************

	public void loadSounds() {
		try {

			boolean done = false;

			final XmlPullParserFactory factory = XmlPullParserFactory
					.newInstance();
			factory.setNamespaceAware(true);
			final XmlPullParser parser = factory.newPullParser();

			InputSource source = new InputSource(context.getAssets().open(
					SOUNDS_FILE));
			parser.setInput(source.getByteStream(), source.getEncoding());

			int eventType = parser.getEventType();
			String name = "";

			while (eventType != XmlPullParser.END_DOCUMENT && !done) {

				switch (eventType) {

				case XmlPullParser.START_TAG:

					name = parser.getName();

					if (name.equals("sounds")) {
						sounds = new ArrayList<Sound>();
					} else if (name.equals("sound")) {
						sounds.add(new Sound(parser.getAttributeValue(0),
								parser.getAttributeValue(1)));
					}

					break;
				}
				eventType = parser.next();
			}
		} catch (final FileNotFoundException e) {
			Log.e("FileNotFoundException", e.getMessage());
		} catch (final IOException e) {
			Log.e("IOException en Favorito", e.getMessage());
		} catch (final Exception e) {
			Log.e("Exception en Favorito", e.getMessage());
		}
	}

	// ****************************************************************
	// Runnable Overrides
	// ****************************************************************

	public void playSound() {
		// Play the sound
		final Message msg = Message.obtain(mHandler, this);
		mHandler.sendMessage(msg);
	}

	public void run() {

		try {

			Sound sound = sounds.get(actualSound);
			SoundPlayer.getInstance(context).play(sound.getPath());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
