
package gubo.slipwire;

/**
 * Created by GUBO on 4/4/2017.
 */
public class DBG
{
    public static boolean verbose = false;

    public static void m( final Object o ) {
        android.util.Log.d( "DBG",(o != null ? o.toString() : null) );
    }

    public static void m( final Throwable x ) {
        if ( x != null ) {
            android.util.Log.d( "DBG","XXX",x );
        } else {
            m( x );
        }
    }

    public static void v( final Object o ) {
        if ( verbose ) {
            m( o );
        }
    }
}
