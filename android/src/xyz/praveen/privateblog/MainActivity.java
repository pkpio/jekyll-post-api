package xyz.praveen.privateblog;

import xyz.praveen.privateblog.Interface.FragmentChanger;
import xyz.praveen.privateblog.fragment.PostFragment;
import xyz.praveen.privateblog.fragment.URLFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements FragmentChanger {
	final int FRAG_URL = 0;
	final int FRAG_POST = 1;
	final int FRAG_COUNT = 2;

	final int ANIM_DISABLE = 0;
	final int ANIM_FORWARD = 1;
	final int ANIM_BACKWARD = 2;
	final int ANIM_DISCARD = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set appropriate fragment
		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (!settings.contains(Param.PARAM_URL))
			changeUrl();
		else
			startPost();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			changeUrl();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void changeUrl() {
		setFragment(FRAG_URL, ANIM_BACKWARD);
	}

	@Override
	public void startPost() {
		setFragment(FRAG_POST, ANIM_FORWARD);
	}

	/**
	 * Replaces current fragment with fragment of choice. This doesn't retrieve
	 * from a fragment history stack so, the new fragment is created fresh
	 * 
	 * @param fragChoice
	 *            Fragment
	 * @param animation
	 *            Animation type
	 */
	public void setFragment(int fragChoice, int animation) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		// Set animation
		switch (animation) {
		case ANIM_FORWARD:
			ft.setCustomAnimations(R.anim.enter, R.anim.exit);
			break;
		case ANIM_BACKWARD:
			ft.setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit);
			break;
		case ANIM_DISCARD:
			ft.setCustomAnimations(R.anim.enter, R.anim.counter_clock);
			break;
		case ANIM_DISABLE:
		default:
			break;
		}

		switch (fragChoice) {
		case FRAG_URL:
			URLFragment urlFrag = URLFragment.newInstance();
			ft.replace(R.id.content_fragment, urlFrag);
			break;
		case FRAG_POST:
			PostFragment postFrag = PostFragment.newInstance();
			ft.replace(R.id.content_fragment, postFrag);
			break;

		default:
			break;
		}

		ft.commit();
	}
}
