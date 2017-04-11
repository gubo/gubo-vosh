
package gubo.slipwire

import android.os.*
import android.text.*
import android.view.*
import android.widget.*

import com.squareup.picasso.*

/**
 * Created by GUBO on 4/6/2017.
 */
fun ViewGroup.inflate( layoutId:Int,attach:Boolean=false ): View {
    return LayoutInflater.from( context ).inflate( layoutId,this,attach )
}

fun ImageView.loadImg( url: String? )
{
    if ( TextUtils.isEmpty( url ) ) {
        Picasso.with(context).load( android.R.drawable.btn_plus ).into( this )
    } else {
        Picasso.with( context ).load( url ).into( this )
    }
}

inline fun <reified T : Parcelable> createParcel( crossinline createFromParcel: (Parcel) -> T? ): Parcelable.Creator<T> =
        object : Parcelable.Creator<T> {
            override fun createFromParcel(source: Parcel): T? = createFromParcel(source)
            override fun newArray(size: Int): Array<out T?> = arrayOfNulls(size)
        }
