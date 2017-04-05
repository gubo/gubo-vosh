package gubo.slipwire;

/**
 *
 */
public interface DataSource<D extends Data>
{
    /**
     *
     * @param position
     * @return
     */
    public D getDataFor( int position );

    /**
     *
     * @param start
     * @param count
     */
    public void getReadyFor( int start,int count );

    /**
     *
     */
    public void requestRefresh();
}
