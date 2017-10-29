package com.voicebot.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.koushik.bot_voice.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Generic base class that contains a container for holding a fragment.
 */
public abstract class BaseSingleFragmentActivity extends BaseAbstractActivity {

    /****************************************************/
    // Instance Variables
    /****************************************************/
   /*@Bind(R.id.img_home)
    ImageView imgHome;
    @Bind(R.id.toolbar_base_linear_layout)
    LinearLayout mLinearLayout;*/
    /****************************************************/
    // Setters & Getters
    /****************************************************/
   /* public LinearLayout getLinearLayout() {
        return mLinearLayout;
    }*/
    /****************************************************/
    // Abstract Method Declaration
    /****************************************************/
    /**
     * Method to return to the base class the fragment that will be added to the fragment container.
     *
     * @return The fragment that needs to be added to the container.
     */
    protected abstract Fragment createFragment();


    /****************************************************/
    // Activity Lifecycle

    /****************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_single_fragment);
        // Bind the views
        ButterKnife.bind(this);
        // Add actions
       // addActions();
        // Call the fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            // Get the fragment that needs to be added.
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    /****************************************************/
    // Actions
    /****************************************************/

    /**
     * Methods to add actions to the various view elements
     */
   /* private void addActions() {
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BaseSingleFragmentActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
            }
        });
    }*/
}
