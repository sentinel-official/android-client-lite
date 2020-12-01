package co.sentinel.lite.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.constant.Currency;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;

import java.util.Map;

import co.sentinel.lite.BuildConfig;
import co.sentinel.lite.R;
import co.sentinel.lite.ui.activity.GenericActivity;
import co.sentinel.lite.ui.activity.VpnUsageActivity;
import co.sentinel.lite.util.AppConstants;

public class ExtraFragment extends Fragment {

    private ConstraintLayout clUsage, clLanguage,clShare,clAbout,clSocial,clSocialLayout,clLearnMore,clExidio;
    private TextView btnClose,tvAbout,tvVersion;
    private ImageButton telegram,medium,twitter,sentinelco;
    private SwitchCompat scAuto;
    private SharedPreferences mPreferences;
    private View socialDismiss;
    private String SENTContract="0xa44e5137293e855b1b7bc7e2c6f8cd796ffcb037";
    private String platform = "ethereum";

    public ExtraFragment() {
    }

    public static ExtraFragment newInstance() {
        ExtraFragment fragment = new ExtraFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_extra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        clLanguage = view.findViewById(R.id.clLanguage);
        clUsage = view.findViewById(R.id.usageLayout);
        clLearnMore = view.findViewById(R.id.learnMoreLayout);
        clAbout = view.findViewById(R.id.clAbout);
        tvAbout = view.findViewById(R.id.aboutTitle);
        clShare = view.findViewById(R.id.clShare);
        clSocial = view.findViewById(R.id.clSocial);
        clExidio = view.findViewById(R.id.cl_exidio_logo);
        btnClose = view.findViewById(R.id.btnClose);
        sentinelco = view.findViewById(R.id.ib_website);
        twitter = view.findViewById(R.id.ib_twitter);
        telegram = view.findViewById(R.id.ib_telegram);
        medium = view.findViewById(R.id.ib_medium);
        scAuto = view.findViewById(R.id.auto_switch);
        scAuto.setChecked(mPreferences.getBoolean("autoMode",false));
        clSocialLayout = view.findViewById(R.id.clSocialLayout);
        clSocialLayout.setAlpha(0.0f);
        socialDismiss = view.findViewById(R.id.dismissView);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        try {
            CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
            Map<String, Map<String, Double>> sentinel = client.getTokenPrice(platform, SENTContract, Currency.USD);
            String sentinelPrice = String.format("≈$%.4f", sentinel.get(SENTContract).get(Currency.USD));
            tvAbout.setText(sentinelPrice);
        } catch (Exception e){
            tvAbout.setText(R.string.about);
        }

        String aVersionName = getString(R.string.lite, BuildConfig.VERSION_NAME);
        ((TextView) view.findViewById(R.id.tv_app_version)).setText(aVersionName);

        clLanguage.setOnClickListener(v -> startActivityForResult(new Intent(getContext(), GenericActivity.class).putExtra(AppConstants.EXTRA_REQ_CODE, AppConstants.REQ_LANGUAGE), AppConstants.REQ_LANGUAGE));

        clShare.setOnClickListener(v -> ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setChooserTitle("Share Sentinel dVPN")
                .setText("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())
                .startChooser());

        clSocial.setOnClickListener(v -> {
            clSocialLayout.setVisibility(View.VISIBLE);
            clSocialLayout.animate().alpha(1.0f);
            socialDismiss.setVisibility(View.VISIBLE);
        });

        socialDismiss.setOnClickListener(v -> {
            clSocialLayout.setVisibility(View.GONE);
            clSocialLayout.setAlpha(0.0f);
            socialDismiss.setVisibility(View.GONE);
        });

        btnClose.setOnClickListener(v -> {
            clSocialLayout.setVisibility(View.GONE);
            clSocialLayout.setAlpha(0.0f);
        });

        scAuto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(scAuto.isChecked()){
                mPreferences.edit().putBoolean("autoMode",true).apply();
            }else{
                mPreferences.edit().putBoolean("autoMode",false).apply();
            }
        });

        clUsage.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), VpnUsageActivity.class));
        });

        //Social buttons
        Intent socialIntent = new Intent();
        socialIntent.setAction(Intent.ACTION_VIEW);
        socialIntent.addCategory(Intent.CATEGORY_BROWSABLE);

        clAbout.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.website_url)));
            startActivity(socialIntent);
        });

        clExidio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socialIntent.setData(Uri.parse("https://www.exidio.co/"));
                startActivity(socialIntent);
            }
        });

        telegram.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.telegram_url)));
            startActivity(socialIntent);
        });

        medium.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.medium_url)));
            startActivity(socialIntent);
        });

        twitter.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.twitter_url)));
            startActivity(socialIntent);
        });

        sentinelco.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.website_url)));
            startActivity(socialIntent);
        });

        clLearnMore.setOnClickListener(v -> {
            socialIntent.setData(Uri.parse(getString(R.string.website_url)));
            startActivity(socialIntent);
        });
    }
}