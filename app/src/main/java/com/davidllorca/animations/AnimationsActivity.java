package com.davidllorca.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.w3c.dom.Text;


public class AnimationsActivity extends ActionBarActivity {

    ViewGroup layout;
    ObjectAnimator animIn;
    ObjectAnimator animOut;
    ObjectAnimator animRestOut;
    ObjectAnimator animRestIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animations);

        //References views
        final ToggleButton invGone = (ToggleButton) findViewById(R.id.invGone);
        // Animation period
        final SeekBar animLength = (SeekBar) findViewById(R.id.animLength);
        final TextView duration = (TextView) findViewById(R.id.duration);
        duration.setText(String.format(getResources().getText(R.string.duration).toString(), animLength.getProgress()));
        // Layout for buttons
        layout = new LinearLayout(this);

        final LayoutTransition layoutTrans = new LayoutTransition();
        layout.setLayoutTransition(layoutTrans);

        //Listeners
        animLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                duration.setText(String.format(getResources().getText(R.string.duration).toString(), animLength.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        // Add elements
        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutTrans.setDuration(animLength.getProgress());
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View view = (View) layout.getChildAt(i);
                    view.setVisibility(View.VISIBLE);
                }

                // Add buttons
                Button button;
                for (int i = 0; i < 6; i++) {
                    button = new Button(getApplicationContext());
                    button.setText(String.format((getResources().getText(R.string.click_me).toString()), i));
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layoutTrans.setDuration(animLength.getProgress());
                            v.setVisibility(invGone.isChecked() ? View.GONE : View.INVISIBLE);
                        }
                    });
                    layout.addView(button);
                }
            }
        });

        ViewGroup parent = (ViewGroup) findViewById(R.id.parentLayout);
        parent.addView(layout);

        // Configure animations
        createAnimations();

        ToggleButton animations = (ToggleButton) findViewById(R.id.animation);
        animations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutTrans.setAnimator(LayoutTransition.APPEARING, animIn);
                    layoutTrans.setAnimator(LayoutTransition.DISAPPEARING, animOut);
                    layoutTrans.setAnimator(LayoutTransition.CHANGE_APPEARING, animRestIn);
                    layoutTrans.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, animRestOut);
                    layoutTrans.setStagger(LayoutTransition.CHANGE_APPEARING, 50);
                    layoutTrans.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 50);
                } else {
                    layoutTrans.setAnimator(LayoutTransition.APPEARING, null);
                    layoutTrans.setAnimator(LayoutTransition.DISAPPEARING, null);
                    layoutTrans.setAnimator(LayoutTransition.CHANGE_APPEARING, null);
                    layoutTrans.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, null);
                    layoutTrans.setStagger(LayoutTransition.CHANGE_APPEARING, 0);
                    layoutTrans.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 0);
                }
            }
        });

        // Configure imageviews' animations
        final ImageView image1 = (ImageView)findViewById(R.id.image1);
        final ImageView image2 = (ImageView)findViewById(R.id.image2);
        // Iniciate rotation
        image2.setRotationY(-90f);

        LinearLayout images = (LinearLayout)findViewById(R.id.images);
        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flip(image1, image2, animLength.getProgress());
            }
        });
    }

    private void flip(ImageView image1, ImageView image2, int duration) {
        final ImageView visibleImage;
        final ImageView invisibleImage;
        // Which's visible at the moment?
        if(image1.getVisibility() == View.GONE){
            visibleImage = image2;
            invisibleImage = image1;
        }else{
            visibleImage = image1;
            invisibleImage = image2;
        }

        // Rotation to begin visible
        final ObjectAnimator goneToVisible =ObjectAnimator.ofFloat(visibleImage, "rotationY", -90f, 0f);
        // Adjust duration
        goneToVisible.setDuration(duration);

        // Rotation to begin visible
        ObjectAnimator visibleToGone =ObjectAnimator.ofFloat(visibleImage, "rotationY", 0f, 90f);
        // Adjust duration
        visibleToGone.setDuration(duration);

        // Listener
        visibleToGone.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                visibleImage.setVisibility(View.GONE);
                // When It hide, animation start
                goneToVisible.start();
                invisibleImage.setVisibility(View.VISIBLE);
            }
        });
        // Start hidding
        visibleToGone.start();
    }

    private void createAnimations() {
        /*
            Do when element is added. Own element.
         */
        PropertyValuesHolder inScaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
        PropertyValuesHolder inScaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
        Keyframe kfi0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kfi1 = Keyframe.ofFloat(.9999f, 360f);
        Keyframe kfi2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder intRotation = PropertyValuesHolder.ofKeyframe("rotation", kfi0, kfi1, kfi2);
        animIn = ObjectAnimator.ofPropertyValuesHolder(AnimationsActivity.this, inScaleX, inScaleY, intRotation);
        //Listener
        animIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
                view.setRotation(0f);
            }
        });
        /*
            Do when element is removed. Own element
         */
        PropertyValuesHolder outScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 2f, 1f, 0f);
        PropertyValuesHolder outScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 2f, 1f, 0f);
        animOut = ObjectAnimator.ofPropertyValuesHolder(AnimationsActivity.this, inScaleX, inScaleY);
        // Listener
        animOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(0f);
                view.setScaleY(0f);
            }
        });
        /*
            Do when a element is removed. Remaining elements
         */
        PropertyValuesHolder left = PropertyValuesHolder.ofInt("left", 0, 1);
        PropertyValuesHolder top = PropertyValuesHolder.ofInt("top", 0, 1);
        PropertyValuesHolder right = PropertyValuesHolder.ofInt("right", 0, 1);
        PropertyValuesHolder bottom = PropertyValuesHolder.ofInt("bottom", 0, 1);
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        animRestOut = ObjectAnimator.ofPropertyValuesHolder(AnimationsActivity.this, left, top, right, bottom, scaleX, scaleY);
        // Listener
        animRestOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setScaleX(1f);
                view.setScaleY(1f);
            }
        });
        /*
            Do when a element is adde. Own element
         */
        Keyframe kfri0 = Keyframe.ofFloat(0f, 0f);
        Keyframe kfri1 = Keyframe.ofFloat(.5f, 360f);
        Keyframe kfri2 = Keyframe.ofFloat(1f, 0f);
        PropertyValuesHolder rotation = PropertyValuesHolder.ofKeyframe("rotationX", kfri0, kfri1, kfri2);
        animRestIn = ObjectAnimator.ofPropertyValuesHolder(AnimationsActivity.this, left, top, right, bottom, rotation);
        // Listener
        animRestIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                View view = (View) ((ObjectAnimator)animation).getTarget();
                view.setRotation(0f);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_animations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
