package xyz.praveen.privateblog.fragment;

import xyz.praveen.privateblog.Interface.FragmentChanger;
import xyz.praveen.privateblog.Param;
import xyz.praveen.privateblog.R;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class URLFragment extends Fragment {
	FragmentChanger fragChanger;

	public static URLFragment newInstance() {
		URLFragment f = new URLFragment();
		return f;
	}

	@Override
	public void onAttach(Activity a) {
		super.onAttach(a);
		fragChanger = (FragmentChanger) a;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_url, container,
				false);

		SharedPreferences settings = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final Editor editor = settings.edit();

		final EditText urlET = (EditText) rootView.findViewById(R.id.url_text);
		rootView.findViewById(R.id.url_save).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						editor.putString(Param.PARAM_URL, urlET.getText()
								.toString());
						editor.commit();
						fragChanger.startPost();
					}
				});
		return rootView;
	}

}
