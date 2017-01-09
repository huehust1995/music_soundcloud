package attt.musicteam.ui.progress_dialog;

/**
 * Created by Hue on 11/8/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import attt.musicteam.R;


public class ProgressDialogNoTitle extends android.app.Dialog{

    Context context;
    View view;
    View backView;

    int progressColor = -1;

    public ProgressDialogNoTitle(Context context) {
        super(context, android.R.style.Theme_Translucent);
        this.context = context;
    }



    public ProgressDialogNoTitle(Context context, int progressColor) {
        super(context, android.R.style.Theme_Translucent);
        this.progressColor = progressColor;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_dialog_no_title);

        view = (RelativeLayout)findViewById(R.id.contentDialog);
        backView = (RelativeLayout)findViewById(R.id.dialog_rootView);
        backView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft()
                        || event.getX() >view.getRight()
                        || event.getY() > view.getBottom()
                        || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });

        if(progressColor != -1){
            ProgressBarCircularIndeterminate progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) findViewById(R.id.progressBarCircularIndetermininate);
            progressBarCircularIndeterminate.setBackgroundColor(progressColor);
        }
    }

    @Override
    public void show() {
        // TODO 自动生成的方法存根
        super.show();
        // set dialog enter animations
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_main_show_animation));
        backView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.dialog_root_show_animation));
    }


    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.dialog_main_hide_animation);
        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        ProgressDialogNoTitle.super.dismiss();
                    }
                });

            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(context, R.anim.dialog_root_hide_animation);

        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }



}