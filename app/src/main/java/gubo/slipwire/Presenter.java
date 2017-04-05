package gubo.slipwire;

import android.support.annotation.Nullable;

/**
 *
 */
public interface Presenter<D extends Display>
{
    /**
     *
     * @param d
     */
    public void bind( @Nullable D d );

    /**
     *
     */
    public void release();
}
