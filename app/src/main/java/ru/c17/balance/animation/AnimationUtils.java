package ru.c17.balance.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class AnimationUtils {

    public interface AnimationFinishedListener {
        void onAnimationFinished();
    }

    public interface Dismissible {
        interface OnDismissedListener {
            void onDismissed();
        }

        void dismiss(OnDismissedListener listener);
    }


    public static void openCircularRevealAnimation(final Context context,
                                                   final View view,
                                                   final RevealAnimationSetting revealSettings,
                                                   final AnimationFinishedListener listener) {


        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int cx = revealSettings.getCenterX();
                int cy = revealSettings.getCenterY();
                int width = revealSettings.getWidth();
                int height = revealSettings.getHeight();
                int duration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);

                //Simply use the diagonal of the view
                float finalRadius = (float) Math.sqrt(width * width + height * height);
                Animator anim = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
                anim.setDuration(duration);
                anim.setInterpolator(new FastOutSlowInInterpolator());
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        listener.onAnimationFinished();
                    }
                });
                anim.start();
            }
        });
    }

    public static void closeCircularRevealAnimation(final View view,
                                                    final RevealAnimationSetting revealSettings,
                                                    final AnimationFinishedListener listener) {

        int cx = revealSettings.getCenterX();
        int cy = revealSettings.getCenterY();
        int width = revealSettings.getWidth();
        int height = revealSettings.getHeight();
        int duration = 400;

        float initRadius = (float) Math.sqrt(width * width + height * height);
        try {
            Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initRadius, 0);
            anim.setDuration(duration);
            anim.setInterpolator(new FastOutSlowInInterpolator());
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //Important: This will prevent the view's flashing (visible between the finished animation and the Fragment remove)
                    view.setVisibility(View.GONE);
                    listener.onAnimationFinished();
                }
            });
            anim.start();
        } catch (NullPointerException npe) {
            return;
        }
    }

}



