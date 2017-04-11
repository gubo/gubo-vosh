
package com.gubo.vosh

import org.junit.*

/**
 * Created by GUBO on 4/6/2017.
 */
class QuickTest
{
    var VERSION = "1.0.0"

    val version : String? = VERSION

    @Test
    fun run() {
        println( "QuickTest.run $version" )
        logExecution( false ) { println( "<execution>" ) }
        logExecution { s1,s2 -> s1.hashCode() + s2.hashCode() }
    }

    fun logExecution( timed: Boolean = true,func: () -> Unit ) {
        var ms:Long = System.currentTimeMillis()
        println( "executing..." )

        func()

        if ( timed ) {
            ms = (System.currentTimeMillis() - ms)
            println( "done $ms ms" )
        } else {
            println( "done" )
        }
    }

    fun logExecution( func: (String,String) -> Int ) {
        val result = func( "Hello","Friend" )
        println( result )
    }
}