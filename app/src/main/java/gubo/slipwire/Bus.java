
package gubo.slipwire;

import io.reactivex.*;
import com.jakewharton.rxrelay2.*;

/**
 *
 * @param <T>
 */
public class Bus<T>
{
    private final Relay<Object> bus = PublishRelay.create().toSerialized();

    public void send( final Object event) {
        bus.accept( event );
    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservers() {
        return bus.hasObservers();
    }
}
