
package com.gubo.vosh

import android.os.*
import android.view.*
import android.support.v7.app.*
import android.support.v4.util.*
import android.support.v7.widget.*

import io.reactivex.*
import io.reactivex.schedulers.*
import io.reactivex.disposables.*
import io.reactivex.android.schedulers.*

import retrofit2.*
import retrofit2.http.*

import retrofit2.converter.moshi.*

import kotlinx.android.synthetic.main.order.*
import kotlinx.android.synthetic.main.orderpayment.*
import kotlinx.android.synthetic.main.orderitem.view.*

import gubo.slipwire.*

class OrderActivity : AppCompatActivity()
{
    companion object {
        private val ORDERUP = "ORDERUP"
    }
    private val orderManager by lazy { OrderManager() }
    private val recyclerView by lazy {
        orderrecyclerview.setHasFixedSize( true )
        orderrecyclerview
    }

    private var disposable:Disposable? = null
    private var order: Order? = null

    override fun onCreate( savedInstanceState: Bundle? ) {
        super.onCreate( savedInstanceState )
        setContentView( R.layout.order )

        orderrootview?.inflate( R.layout.orderpayment,true )
        ordertitletextview?.text = "ORDER"
        orderpaymenttitletextview?.text = "AMERICAN EXPRESS"

        orderrecyclerview.layoutManager = LinearLayoutManager( this )
        orderrecyclerview.addOnScrollListener( InfiniteScrollListener( { requestOrder() },(orderrecyclerview.layoutManager as LinearLayoutManager) ) )
        orderrecyclerview.adapter = OrderAdapter()

        if ( (savedInstanceState != null) && savedInstanceState.containsKey( ORDERUP ) ) {
            order = savedInstanceState.get( ORDERUP ) as Order
        }
    }

    override fun onSaveInstanceState( outState: Bundle ) {
        super.onSaveInstanceState(outState)
        val currentOrders = ( recyclerView.adapter as OrderAdapter).getOrders()
    }

    override fun onResume() {
        super.onResume()
        requestOrder()
    }

    override fun onPause() {
        super.onPause()

        disposable?.dispose()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun requestOrder() {
        disposable?.dispose()

        disposable = orderManager.getOrder( order?.after ?: "" )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(
                        { retreivedOrder ->
                            ( orderrecyclerview.adapter as OrderAdapter ).addOrderItems( retreivedOrder.items )
                            orderrecyclerview.adapter.notifyDataSetChanged()
                            order = retreivedOrder
                        },
                        { error -> error.printStackTrace() }
                )
    }
}

class RedditNewsResponse( val data:RedditDataResponse )

class RedditDataResponse(
        val children: List<RedditChildrenResponse>,
        val after: String?,
        val before: String?
)

class RedditChildrenResponse( val data:RedditNewsDataResponse )

class RedditNewsDataResponse(
        val id:String,
        val author: String,
        val title: String,
        val num_comments: Int,
        val created: Long,
        val thumbnail: String,
        val url: String
)

interface  RedditApi
{
    @GET( "/top.json" )
    fun getTop( @Query( "after" ) after: String,
                @Query( "limit" ) limit: String)
            : Call<RedditNewsResponse>;
}

class RestAPI
{
    private val redditApi: RedditApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl( "https://www.reddit.com"  )
                .addConverterFactory( MoshiConverterFactory.create() )
                .build()

        redditApi = retrofit.create( RedditApi::class.java )
    }

    fun getNews( after:String,limit:String ): Call<RedditNewsResponse>{
        return redditApi.getTop( after,limit )
    }
}

class OrderManager( private val api:RestAPI = RestAPI() )
{
    fun getOrder(after: String, limit: String = "10"): Observable<Order> {
        return Observable.create {
            subscriber ->
            val callResponse = api.getNews( after,limit )
            val response = callResponse.execute()
            if ( response.isSuccessful) {
                val dataResponse = response.body().data
                val orderItem = dataResponse.children.map {
                    val data = it.data
                    OrderItem( data.id,data.author,data.title,data.thumbnail )
                }
                val order = Order(
                        dataResponse.after ?: "",
                        dataResponse.before ?: "",
                        orderItem)
                subscriber.onNext( order )
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}

/*
 * https://medium.com/@juanchosaravia/keddit-part-4-recyclerview-delegate-adapters-data-classes-with-kotlin-9248f44327f7
 */

data class Order( val after: String,val before: String,val items: List<OrderItem> ) : Parcelable
{
    companion object {
        @JvmField @Suppress( "unused" )
        val CREATOR = createParcel{ Order( it ) }
    }

    protected constructor( parcelIn:Parcel ) : this (
            parcelIn.readString(),
            parcelIn.readString(),
            mutableListOf<OrderItem>().apply {
                parcelIn.readTypedList( this,OrderItem.CREATOR )
            }
    )

    override fun writeToParcel( parcelOut: Parcel,flags: Int ) {
        parcelOut.writeString( after )
        parcelOut.writeString( before )
        parcelOut.writeTypedList( items )
    }

    override fun describeContents() = 0
}

data class OrderItem( val id: String,val author:String,val title:String,val thumbnailUrl:String ) : ViewType,Parcelable
{
    companion object {
        @JvmField @Suppress( "unused" )
        val CREATOR = createParcel{ OrderItem( it ) }
    }

    protected constructor( parcelIn:Parcel ) : this (
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString(),
            parcelIn.readString()
    )

    override fun getViewType() = AdapterConstants.ORDERITEM

    override fun writeToParcel( parcelOut: Parcel, flags: Int ) {
        parcelOut.writeString( id )
        parcelOut.writeString( author )
        parcelOut.writeString( title )
        parcelOut.writeString( thumbnailUrl )
    }

    override fun describeContents() = 0
}

object AdapterConstants
{
    val ORDERITEM       = 1
    val ORDERTOTAL      = 2
}

class OrderAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()
    private val items: ArrayList<ViewType> = ArrayList()

    init {
        delegateAdapters.put( AdapterConstants.ORDERITEM,OrderDelegateAdapter() )
    }

    fun addOrderItems( orderItems:List<OrderItem> ) {
        val atPosition = items.size - 1
        items.addAll( orderItems )
        notifyItemRangeChanged( atPosition,items.size )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType( position:Int ): Int {
        return this.items.get(position).getViewType()
    }
    override fun onCreateViewHolder( parent: ViewGroup,viewType: Int ): RecyclerView.ViewHolder {
        return delegateAdapters.get( viewType ).onCreateViewHolder( parent )
    }

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder,position: Int ) {
        delegateAdapters.get( getItemViewType( position ) ).onBindViewHolder( holder,this.items[position] ) // TODO null check chain needed here
    }
}

class OrderDelegateAdapter : ViewTypeDelegateAdapter
{
    override fun onCreateViewHolder( parent: ViewGroup ) = TurnsViewHolder( parent )

    override fun onBindViewHolder( holder: RecyclerView.ViewHolder,item: ViewType ) {
        holder as TurnsViewHolder
        holder.bind( item as OrderItem )
    }

    class TurnsViewHolder( parent: ViewGroup ) : RecyclerView.ViewHolder( parent.inflate( R.layout.orderitem ) )
    {
        fun bind( item: OrderItem ) = with( itemView ) {
            orderitemthumbnailimageview.loadImg( item.thumbnailUrl )
            orderitemauthortextview.text = item.author
            orderitemtitletextview.text = item.title
            //super.itemView.setOnClickListener { viewActions.onItemSelected( item.url ) }
        }
    }
}

interface ViewType {
    fun getViewType(): Int
}

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder( parent: ViewGroup ): RecyclerView.ViewHolder
    fun onBindViewHolder( holder: RecyclerView.ViewHolder,item: ViewType )
}


