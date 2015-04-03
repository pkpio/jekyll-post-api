package xyz.praveen.privateblog.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import xyz.praveen.privateblog.Param;
import xyz.praveen.privateblog.R;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class PostFragment extends Fragment implements OnClickListener {
	EditText postTitleET;
	EditText postCatsET;
	EditText postTagsET;
	EditText postExcerptET;
	EditText postContentET;
	Button postBtn;

	public static PostFragment newInstance() {
		PostFragment f = new PostFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_post, container,
				false);
		postTitleET = (EditText) rootView.findViewById(R.id.post_title);
		postCatsET = (EditText) rootView.findViewById(R.id.post_cats);
		postTagsET = (EditText) rootView.findViewById(R.id.post_tags);
		postExcerptET = (EditText) rootView.findViewById(R.id.post_excerpt);
		postContentET = (EditText) rootView.findViewById(R.id.post_content);
		postBtn = (Button) rootView.findViewById(R.id.post_btn);

		postBtn.setOnClickListener(this);
		return rootView;
	}

	/**
	 * Posts in background
	 * 
	 * @author Praveen Kumar Pendyala (m@praveen.xyz)
	 * 
	 */
	private class postSyncerBg extends AsyncTask<String, Integer, Boolean> {
		String url;
		String title;
		String cats;
		String tags;
		String excerpt;
		String content;

		@Override
		protected void onPreExecute() {
			SharedPreferences settings = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			url = settings.getString(Param.PARAM_URL, "");
			title = postTitleET.getText().toString();
			cats = postCatsET.getText().toString();
			tags = postTagsET.getText().toString();
			excerpt = postExcerptET.getText().toString();
			content = postContentET.getText().toString();
			postBtn.setEnabled(false);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// Making HTTP POST request to send data
			try {
				// A client to do a HTTP Post request
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				System.out.println("URL:" + url);

				// Adding nameValuePairs - message as a post variable to the
				// request
				List<NameValuePair> post = new ArrayList<NameValuePair>();
				post.add(new BasicNameValuePair(Param.URL_PARAM_TITLE, title));
				post.add(new BasicNameValuePair(Param.URL_PARAM_CATS, cats));
				post.add(new BasicNameValuePair(Param.URL_PARAM_TAGS, tags));
				post.add(new BasicNameValuePair(Param.URL_PARAM_EXCERPT,
						excerpt));
				post.add(new BasicNameValuePair(Param.URL_PARAM_CONTENT,
						content));
				httpPost.setEntity(new UrlEncodedFormEntity(post));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				String serverResponse = null;
				InputStream is = null;
				if (httpEntity != null) {
					httpEntity.consumeContent();
					is = httpEntity.getContent();
				}

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;

				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				serverResponse = sb.toString();
				System.out.println(serverResponse);

				if (serverResponse.contentEquals(Param.URL_SUCCESS_RESPONSE))
					return true;
				else
					return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			postBtn.setEnabled(true);
			if (result) {
				postTitleET.setText("");
				postCatsET.setText("");
				postTagsET.setText("");
				postExcerptET.setText("");
				postContentET.setText("");
			}
		}
	}

	@Override
	public void onClick(View v) {
		new postSyncerBg().execute("");
	}

}
