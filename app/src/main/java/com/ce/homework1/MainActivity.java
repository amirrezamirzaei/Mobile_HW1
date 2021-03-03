package com.ce.homework1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ce.homework1.model.Coin;
import com.ce.homework1.model.MessageResult;

import java.util.ArrayList;


public class MainActivity extends Activity {
    private static int coinsLoaded = 1;
    private static int loadLimit = 5;
    private static boolean canUpdate = false;

    private static ArrayList<Coin> coinsToBeAdded;

    private Handler handler;
    private LinearLayout mainLayout;
    private Button updateButton;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = findViewById(R.id.mainLayout);
        updateButton = findViewById(R.id.updateButton);
        progressBar = findViewById(R.id.progressBar);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MessageResult.SUCCESSFUL) {
                    progressBar.setProgress(progressBar.getProgress()+loadLimit);
                    addCoinToView();
                } else {

                }
            }
        };

        if (isNetworkAvailable()) {   // connected to internet
            makeViewIntoLoading();
            getCoins();
        } else {
            Toast.makeText(this,"Error: you aren't connected to internet",Toast.LENGTH_SHORT).show();
            /*TODO update coins from database you only need to get coin from database and call setcoinstobeadded*/
        }
    }

    public void addCoinToView() {
        mainLayout.post(new Runnable() {
            @Override
            public void run() {
                for (Coin coin : coinsToBeAdded) {
                    View layoutToBeAdded = LayoutInflater.from(MainActivity.this).inflate(R.layout.fragment_coin, mainLayout, false);
                    RelativeLayout relativeLayout = (RelativeLayout) ((LinearLayout) layoutToBeAdded.getRootView()).getChildAt(0);
                    ImageView logo = (ImageView) relativeLayout.getChildAt(0);
                    TextView name = (TextView) relativeLayout.getChildAt(1);
                    TextView price = (TextView) relativeLayout.getChildAt(2);
                    TextView percentDayHour = (TextView) relativeLayout.getChildAt(3);
                    TextView percentWeek = (TextView) relativeLayout.getChildAt(4);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(getBaseContext()).load(coin.getImageUrl()).placeholder(R.drawable.loading).error(R.drawable.no_connection).into(logo);
                            Log.d("mido",coin.getImageUrl());
                        }
                    });
                    price.setText(String.format("%.03f", coin.getPrice())+"$");
                    name.setText(coin.getName() + " | " + coin.getSymbol());
                    percentDayHour.setText("1h:" + String.format("%.02f", coin.getPercentChangeHour()) + "%  " + "1D:" + String.format("%.02f", coin.getPercentChangeDay()) + "%");
                    percentWeek.setText("7D:" + String.format("%.02f", coin.getPercentChangeWeek()) + "%");
                    layoutToBeAdded.getRootView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CoinActivity.setCoin(coin);
                            Intent i = new Intent(MainActivity.this, CoinActivity.class);
                            startActivity(i);
                        }
                    });
                    mainLayout.addView(layoutToBeAdded);
                }
                coinsLoaded += loadLimit;
                canUpdate = true;
                setCoinsToBeAdded(null);
                makeViewIntoNotLoading();
            }
        });

    }

    public void getCoins() {
        ThreadGenerator.getCoins(coinsLoaded, loadLimit, handler).start();
    }

    public static void setCoinsToBeAdded(ArrayList<Coin> coinsToBeAdded) {
        MainActivity.coinsToBeAdded = coinsToBeAdded;
    }

    public void update(View view) {
        if(canUpdate && isNetworkAvailable()){
            canUpdate = false;
            makeViewIntoLoading();
            getCoins();
        }else if(isNetworkAvailable()==false){
            Toast.makeText(this,"Error: you aren't connected to internet",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Error: please wait before requesting updates",Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void makeViewIntoLoading(){
        updateButton.setBackgroundColor(getResources().getColor(R.color.light_red));
        progressBar.setVisibility(View.VISIBLE);
    }

    private void makeViewIntoNotLoading(){
        updateButton.setBackgroundColor(getResources().getColor(R.color.light_green));
        progressBar.setVisibility(View.INVISIBLE);
    }

    String sample1 = "{\"status\":{\"timestamp\":\"2021-02-27T17:23:44.031Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":16,\"credit_count\":1,\"notice\":null,\"total_count\":4197},\"data\":[{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"slug\":\"bitcoin\",\"num_market_pairs\":9777,\"date_added\":\"2013-04-28T00:00:00.000Z\",\"tags\":[\"mineable\",\"pow\",\"sha-256\",\"store-of-value\",\"state-channels\",\"coinbase-ventures-portfolio\",\"three-arrows-capital-portfolio\",\"polychain-capital-portfolio\"],\"max_supply\":21000000,\"circulating_supply\":18640337,\"total_supply\":18640337,\"platform\":null,\"cmc_rank\":1,\"last_updated\":\"2021-02-27T17:22:02.000Z\",\"quote\":{\"USD\":{\"price\":47142.33836536657,\"volume_24h\":49232496935.616,\"percent_change_1h\":-0.11771834,\"percent_change_24h\":-1.47838687,\"percent_change_7d\":-16.53731833,\"percent_change_30d\":47.38553034,\"market_cap\":878749074098.462,\"last_updated\":\"2021-02-27T17:22:02.000Z\"}}},{\"id\":1027,\"name\":\"Ethereum\",\"symbol\":\"ETH\",\"slug\":\"ethereum\",\"num_market_pairs\":6057,\"date_added\":\"2015-08-07T00:00:00.000Z\",\"tags\":[\"mineable\",\"pow\",\"smart-contracts\",\"coinbase-ventures-portfolio\",\"three-arrows-capital-portfolio\",\"polychain-capital-portfolio\"],\"max_supply\":null,\"circulating_supply\":114845254.249,\"total_supply\":114845254.249,\"platform\":null,\"cmc_rank\":2,\"last_updated\":\"2021-02-27T17:22:02.000Z\",\"quote\":{\"USD\":{\"price\":1481.03273875031,\"volume_24h\":30390418791.21968,\"percent_change_1h\":0.28993843,\"percent_change_24h\":-3.8196045,\"percent_change_7d\":-25.71130276,\"percent_change_30d\":10.75762384,\"market_cap\":170089581432.87216,\"last_updated\":\"2021-02-27T17:22:02.000Z\"}}}]}";
    String sample2 = "{\"status\":{\"timestamp\":\"2021-02-27T18:01:20.831Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":10,\"credit_count\":1,\"notice\":null},\"data\":{\"1\":{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"category\":\"coin\",\"description\":\"Bitcoin (BTC) is a cryptocurrency . Users are able to generate BTC through the process of mining. Bitcoin has a current supply of 18,640,337. The last known price of Bitcoin is 47,071.47927742 USD and is down -1.97 over the last 24 hours. It is currently trading on 9777 active market(s) with $49,071,761,168.08 traded over the last 24 hours. More information can be found at https://bitcoin.org/.\",\"slug\":\"bitcoin\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/1.png\",\"subreddit\":\"bitcoin\",\"notice\":\"\",\"tags\":[\"mineable\",\"pow\",\"sha-256\",\"store-of-value\",\"state-channels\",\"coinbase-ventures-portfolio\",\"three-arrows-capital-portfolio\",\"polychain-capital-portfolio\"],\"tag-names\":[\"Mineable\",\"PoW\",\"SHA-256\",\"Store of Value\",\"State channels\",\"Coinbase Ventures Portfolio\",\"Three Arrows Capital Portfolio\",\"Polychain Capital Portfolio\"],\"tag-groups\":[\"OTHER\",\"CONSENSUS_ALGORITHM\",\"CONSENSUS_ALGORITHM\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\"],\"urls\":{\"website\":[\"https://bitcoin.org/\"],\"twitter\":[],\"message_board\":[\"https://bitcointalk.org\"],\"chat\":[],\"explorer\":[\"https://blockchain.coinmarketcap.com/chain/bitcoin\",\"https://blockchain.info/\",\"https://live.blockcypher.com/btc/\",\"https://blockchair.com/bitcoin\",\"https://explorer.viabtc.com/btc\"],\"reddit\":[\"https://reddit.com/r/bitcoin\"],\"technical_doc\":[\"https://bitcoin.org/bitcoin.pdf\"],\"source_code\":[\"https://github.com/bitcoin/\"],\"announcement\":[]},\"platform\":null,\"date_added\":\"2013-04-28T00:00:00.000Z\",\"twitter_username\":\"\",\"is_hidden\":0},\"2\":{\"id\":2,\"name\":\"Litecoin\",\"symbol\":\"LTC\",\"category\":\"coin\",\"description\":\"Litecoin (LTC) is a cryptocurrency . Users are able to generate LTC through the process of mining. Litecoin has a current supply of 66,557,350.30105277. The last known price of Litecoin is 175.07754056 USD and is down -2.20 over the last 24 hours. It is currently trading on 759 active market(s) with $5,493,061,056.49 traded over the last 24 hours. More information can be found at https://litecoin.org/.\",\"slug\":\"litecoin\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/2.png\",\"subreddit\":\"litecoin\",\"notice\":\"\",\"tags\":[\"mineable\",\"pow\",\"scrypt\",\"medium-of-exchange\",\"binance-chain\"],\"tag-names\":[\"Mineable\",\"PoW\",\"Scrypt\",\"Medium of Exchange\",\"Binance Chain\"],\"tag-groups\":[\"OTHER\",\"CONSENSUS_ALGORITHM\",\"CONSENSUS_ALGORITHM\",\"PROPERTY\",\"PLATFORM\"],\"urls\":{\"website\":[\"https://litecoin.org/\"],\"twitter\":[\"https://twitter.com/LitecoinProject\"],\"message_board\":[\"https://litecointalk.io/\",\"https://litecoin-foundation.org/\"],\"chat\":[\"https://telegram.me/litecoin\"],\"explorer\":[\"https://blockchair.com/litecoin\",\"https://chainz.cryptoid.info/ltc/\",\"http://explorer.litecoin.net/chain/Litecoin\",\"https://ltc.tokenview.com/en\",\"https://bscscan.com/token/0x4338665cbb7b2485a8855a139b75d5e34ab0db94\"],\"reddit\":[\"https://reddit.com/r/litecoin\"],\"technical_doc\":[],\"source_code\":[\"https://github.com/litecoin-project/litecoin\"],\"announcement\":[\"https://bitcointalk.org/index.php?topic=47417.0\"]},\"platform\":null,\"date_added\":\"2013-04-28T00:00:00.000Z\",\"twitter_username\":\"LitecoinProject\",\"is_hidden\":0}}}";
    String sample3 = "{\"status\":{\"timestamp\":\"2021-02-28T15:50:25.255Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":12,\"credit_count\":1,\"notice\":null,\"total_count\":4199},\"data\":[{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"slug\":\"bitcoin\",\"num_market_pairs\":9777,\"date_added\":\"2013-04-28T00:00:00.000Z\",\"tags\":[\"mineable\",\"pow\",\"sha-256\",\"store-of-value\",\"state-channels\",\"coinbase-ventures-portfolio\",\"three-arrows-capital-portfolio\",\"polychain-capital-portfolio\"],\"max_supply\":21000000,\"circulating_supply\":18641056,\"total_supply\":18641056,\"platform\":null,\"cmc_rank\":1,\"last_updated\":\"2021-02-28T15:49:02.000Z\",\"quote\":{\"USD\":{\"price\":43510.29488047352,\"volume_24h\":47782219813.74312,\"percent_change_1h\":-2.61851681,\"percent_change_24h\":-7.51818211,\"percent_change_7d\":-24.05455863,\"percent_change_30d\":18.19786313,\"market_cap\":811077843443.4202,\"last_updated\":\"2021-02-28T15:49:02.000Z\"}}},{\"id\":1027,\"name\":\"Ethereum\",\"symbol\":\"ETH\",\"slug\":\"ethereum\",\"num_market_pairs\":6062,\"date_added\":\"2015-08-07T00:00:00.000Z\",\"tags\":[\"mineable\",\"pow\",\"smart-contracts\",\"coinbase-ventures-portfolio\",\"three-arrows-capital-portfolio\",\"polychain-capital-portfolio\"],\"max_supply\":null,\"circulating_supply\":114857654.374,\"total_supply\":114857654.374,\"platform\":null,\"cmc_rank\":2,\"last_updated\":\"2021-02-28T15:49:02.000Z\",\"quote\":{\"USD\":{\"price\":1305.7250905892593,\"volume_24h\":23871643005.949333,\"percent_change_1h\":-3.97884162,\"percent_change_24h\":-11.08209391,\"percent_change_7d\":-33.36179515,\"percent_change_30d\":-5.62778959,\"market_cap\":149972521162.36096,\"last_updated\":\"2021-02-28T15:49:02.000Z\"}}},{\"id\":2010,\"name\":\"Cardano\",\"symbol\":\"ADA\",\"slug\":\"cardano\",\"num_market_pairs\":236,\"date_added\":\"2017-10-01T00:00:00.000Z\",\"tags\":[\"mineable\",\"dpos\",\"pos\",\"platform\",\"research\",\"smart-contracts\",\"staking\",\"binance-chain\"],\"max_supply\":45000000000,\"circulating_supply\":31112484646,\"total_supply\":45000000000,\"platform\":null,\"cmc_rank\":3,\"last_updated\":\"2021-02-28T15:49:10.000Z\",\"quote\":{\"USD\":{\"price\":1.19825043342246,\"volume_24h\":15820360900.041733,\"percent_change_1h\":-3.79005489,\"percent_change_24h\":-12.24714846,\"percent_change_7d\":6.93623985,\"percent_change_30d\":234.90382015,\"market_cap\":37280548211.91913,\"last_updated\":\"2021-02-28T15:49:10.000Z\"}}},{\"id\":825,\"name\":\"Tether\",\"symbol\":\"USDT\",\"slug\":\"tether\",\"num_market_pairs\":11015,\"date_added\":\"2015-02-25T00:00:00.000Z\",\"tags\":[\"store-of-value\",\"payments\",\"stablecoin\",\"stablecoin-asset-backed\"],\"max_supply\":null,\"circulating_supply\":34977434545.61555,\"total_supply\":35708167440.84519,\"platform\":{\"id\":1027,\"name\":\"Ethereum\",\"symbol\":\"ETH\",\"slug\":\"ethereum\",\"token_address\":\"0xdac17f958d2ee523a2206206994597c13d831ec7\"},\"cmc_rank\":4,\"last_updated\":\"2021-02-28T15:49:09.000Z\",\"quote\":{\"USD\":{\"price\":1.00112401012021,\"volume_24h\":97439263665.51003,\"percent_change_1h\":0.0100598,\"percent_change_24h\":0.09251005,\"percent_change_7d\":0.03576873,\"percent_change_30d\":-0.1317676,\"market_cap\":35016749536.023796,\"last_updated\":\"2021-02-28T15:49:09.000Z\"}}},{\"id\":1839,\"name\":\"Binance Coin\",\"symbol\":\"BNB\",\"slug\":\"binance-coin\",\"num_market_pairs\":478,\"date_added\":\"2017-07-25T00:00:00.000Z\",\"tags\":[\"marketplace\",\"payments\"],\"max_supply\":170532785,\"circulating_supply\":154532785,\"total_supply\":170532785,\"platform\":null,\"cmc_rank\":5,\"last_updated\":\"2021-02-28T15:49:09.000Z\",\"quote\":{\"USD\":{\"price\":198.92208868049204,\"volume_24h\":2727758587.3790355,\"percent_change_1h\":-5.40475507,\"percent_change_24h\":-12.05809988,\"percent_change_7d\":-33.47609171,\"percent_change_30d\":356.89257436,\"market_cap\":30739984361.81341,\"last_updated\":\"2021-02-28T15:49:09.000Z\"}}}]}";
    String sample4 = "{\"status\":{\"timestamp\":\"2021-02-28T17:40:16.053Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":12,\"credit_count\":1,\"notice\":null},\"data\":{\"1\":{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"category\":\"coin\",\"slug\":\"bitcoin\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/1.png\",\"subreddit\":\"bitcoin\",\"tag-names\":[\"Mineable\",\"PoW\",\"SHA-256\",\"Store of Value\",\"State channels\",\"Coinbase Ventures Portfolio\",\"Three Arrows Capital Portfolio\",\"Polychain Capital Portfolio\"],\"tag-groups\":[\"OTHER\",\"CONSENSUS_ALGORITHM\",\"CONSENSUS_ALGORITHM\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\"],\"twitter_username\":\"\",\"is_hidden\":0},\"825\":{\"id\":825,\"name\":\"Tether\",\"symbol\":\"USDT\",\"category\":\"token\",\"slug\":\"tether\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/825.png\",\"subreddit\":\"\",\"tag-names\":[\"Store of Value\",\"Payments\",\"Stablecoin\",\"Stablecoin - Asset-Backed\"],\"tag-groups\":[\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\"],\"twitter_username\":\"tether_to\",\"is_hidden\":0},\"1027\":{\"id\":1027,\"name\":\"Ethereum\",\"symbol\":\"ETH\",\"category\":\"coin\",\"slug\":\"ethereum\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/1027.png\",\"subreddit\":\"ethereum\",\"tag-names\":[\"Mineable\",\"PoW\",\"Smart Contracts\",\"Coinbase Ventures Portfolio\",\"Three Arrows Capital Portfolio\",\"Polychain Capital Portfolio\"],\"tag-groups\":[\"OTHER\",\"CONSENSUS_ALGORITHM\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\"],\"twitter_username\":\"ethereum\",\"is_hidden\":0},\"1839\":{\"id\":1839,\"name\":\"Binance Coin\",\"symbol\":\"BNB\",\"category\":\"coin\",\"slug\":\"binance-coin\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/1839.png\",\"subreddit\":\"binance\",\"tag-names\":[\"Marketplace\",\"Payments\"],\"tag-groups\":[\"PROPERTY\",\"PROPERTY\"],\"twitter_username\":\"binance\",\"is_hidden\":0},\"2010\":{\"id\":2010,\"name\":\"Cardano\",\"symbol\":\"ADA\",\"category\":\"coin\",\"slug\":\"cardano\",\"logo\":\"https://s2.coinmarketcap.com/static/img/coins/64x64/2010.png\",\"subreddit\":\"cardano\",\"tag-names\":[\"Mineable\",\"DPoS\",\"PoS\",\"Platform\",\"Research\",\"Smart Contracts\",\"Staking\",\"Binance Chain\"],\"tag-groups\":[\"OTHER\",\"CONSENSUS_ALGORITHM\",\"CONSENSUS_ALGORITHM\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PROPERTY\",\"PLATFORM\"],\"twitter_username\":\"cardano\",\"is_hidden\":0}}}";

}