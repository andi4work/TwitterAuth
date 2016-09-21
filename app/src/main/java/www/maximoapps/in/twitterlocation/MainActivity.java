package www.maximoapps.in.twitterlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import butterknife.InjectView;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private static final String TWITTER_KEY = "LBrEjECz0OFeLKJfGhTaVgoRL";
    private static final String TWITTER_SECRET = "5ep69NZusDMb9lpjV8NjOUrWZJ03wwBnEahpsYdQwwPQnyeptK";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_IMAGE_URL = "image_url";
    @InjectView(R.id.twitter_login_button)
    TwitterLoginButton twitterLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.main_activity);
        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TEST", "Login with Twitter failure", exception);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void login(Result<TwitterSession> result) {
        TwitterSession session = result.data;
        final String username = session.getUserName();
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void failure(TwitterException e) {
                    }

                    @Override
                    public void success(Result<User> userResult) {
                        User user = userResult.data;
                        String profileImage = user.profileImageUrl.replace("_normal", "");
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        intent.putExtra(KEY_USERNAME, username);
                        intent.putExtra(KEY_PROFILE_IMAGE_URL, profileImage);
                        startActivity(intent);
                    }
                });
    }
}
