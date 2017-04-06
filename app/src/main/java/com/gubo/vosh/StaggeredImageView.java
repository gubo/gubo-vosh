
package com.gubo.vosh;

import android.util.*;
import android.content.*;
import android.support.v7.widget.*;

/**
 * Created by JEFF on 7/17/2016.
 */
public class StaggeredImageView extends AppCompatImageView
{
    private int imageWidth;
    private int imageHeight;

    public StaggeredImageView( Context context ) { super( context ); }
    public StaggeredImageView( Context context,AttributeSet attrs ) { super( context,attrs ); }
    public StaggeredImageView( Context context,AttributeSet attrs,int defStyleAttr ) { super( context,attrs,defStyleAttr ); }

    public int getImageWidth() {
        return imageWidth;
    }
    public void setImageWidth( final int imageWidth ) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }
    public void setImageHeight( final int imageHeight ) {
        this.imageHeight = imageHeight;
    }

    @Override
    protected void onMeasure( final int widthMeasureSpec,final int heightMeasureSpec ) {
        int mw = MeasureSpec.getSize( widthMeasureSpec );
        int mh = MeasureSpec.getSize( heightMeasureSpec );

        if ( (imageWidth > 0) && (imageHeight > 0) ) {
            final float iw = imageWidth;
            final float ih = imageHeight;
            final float iratio = iw / ih;
            if ( iratio > 0 ) {
                mh = ( int )( mw / iratio );
            }
        } else {
            mh = mw;
        }

        mw = MeasureSpec.makeMeasureSpec( mw,MeasureSpec.EXACTLY );
        mh = MeasureSpec.makeMeasureSpec( mh,MeasureSpec.EXACTLY );

        super.onMeasure( mw,mh );
    }
}
