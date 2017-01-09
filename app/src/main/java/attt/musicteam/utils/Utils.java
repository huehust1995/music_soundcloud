package attt.musicteam.utils;
/**
 * Created by Hue on 11/8/2016.
 */
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;

public class Utils {


    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}