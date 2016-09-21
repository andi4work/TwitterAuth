package www.maximoapps.in.twitterlocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

public class SearchTweets extends ActionBarActivity {

    private boolean flagLoading;
    private boolean endOfSearchResults;
    private static String SEARCH_QUERY = "#Bangalore";
    private TweetViewAdapter adapter;
    private static final String SEARCH_RESULT_TYPE = "recent";
    private static final int SEARCH_COUNT = 200000;
    private long maxId;
    ListView SearchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        setProgressBarIndeterminateVisibility(true);
        adapter = new TweetViewAdapter(SearchTweets.this);
        SearchList = (ListView) findViewById(R.id.search_list);
        SearchList.setAdapter(adapter);
        Intent intent = getIntent();
        SEARCH_QUERY = intent.getExtras().getString("searchString");
        SearchList.setEmptyView(findViewById(R.id.loading));
        final SearchService service = Twitter.getApiClient().getSearchService();
        service.tweets(SEARCH_QUERY, null, null, null, SEARCH_RESULT_TYPE, SEARCH_COUNT, null, null,
                maxId, true, new Callback<Search>() {
                    @Override
                    public void success(Result<Search> searchResult) {
                        setProgressBarIndeterminateVisibility(false);
                        final List<Tweet> tweets = searchResult.data.tweets;
                        adapter.getTweets().addAll(tweets);
                        adapter.notifyDataSetChanged();
                        if (tweets.size() > 0) {
                            maxId = tweets.get(tweets.size() - 1).id - 1;
                        } else {
                            endOfSearchResults = true;
                        }
                        flagLoading = false;
                    }

                    @Override
                    public void failure(TwitterException error) {

                        setProgressBarIndeterminateVisibility(false);
                        Toast.makeText(SearchTweets.this,
                                "Failed",
                                Toast.LENGTH_SHORT).show();

                        flagLoading = false;
                    }
                }
        );
    }
}