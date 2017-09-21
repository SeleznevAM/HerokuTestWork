package com.applications.whazzup.flowmortarapplication.flow

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.applications.whazzup.flowmortarapplication.R
import com.applications.whazzup.flowmortarapplication.mortar.ScreenScoper
import flow.*
import java.util.*

class TreeKeyDispetcher(val mActivity : Activity) : KeyChanger(), Dispatcher{

    private var inKey: Any? = null

    private var outKey: Any? = null

    private var mRootFrame: FrameLayout? = null


    override fun dispatch(traversal: Traversal, callback: TraversalCallback) {
        val mContexts: Map<Any, Context>
        val inState = traversal.getState(traversal.destination.top())
        inKey = inState.getKey<Any>()
        val outState = if (traversal.origin == null) null else traversal.getState(traversal.origin!!.top())
        outKey = outState?.getKey<Any>()

        mRootFrame = mActivity.findViewById(R.id.root_frame) as FrameLayout

        if (inKey == outKey) {
            callback.onTraversalCompleted()
            return
        }


        /* if((outKey instanceof AuthorAlbumInfoScreen) && (inKey instanceof PhotoDetailInfoScreen)){
            ScreenScoper.destroyScreenScope(PhotoDetailInfoScreen.class.getName());
        }*/

        // TODO: 27.11.2016 create mortar context for screen
        val flowContext = traversal.createContext(inKey, mActivity)
        val mortarContext = ScreenScoper.getScreenScope(inKey as AbstractScreen<Nothing>).createContext(flowContext)
        mContexts = Collections.singletonMap(inKey, mortarContext)
        changeKey(outState, inState, traversal.direction, mContexts, callback)
    }

    override fun changeKey(outgoingState: State?, incomingState: State, direction: Direction, incomingContexts: Map<Any, Context>, callback: TraversalCallback) {
        val context = incomingContexts[inKey]
        // Сохраняем состояние экрана
        outgoingState?.save(mRootFrame!!.getChildAt(0))

        // Создаем новый экран
        val screen: Screen?
        screen = inKey!!.javaClass.getAnnotation(Screen::class.java)
        if (screen == null) {
            throw IllegalStateException("@Screen annotation is missing in screen " + (inKey as AbstractScreen<Nothing>):: class.java)
        } else {
            val layout = screen.value
            val inflater = LayoutInflater.from(context)
            val newView = inflater.inflate(layout, mRootFrame, false)
            val oldView = mRootFrame!!.getChildAt(0)

            //restore state new view
            incomingState.restore(newView)
            // TODO: 27.11.2016 Unregister screen scope

            //delete old view
            if (outKey != null && inKey !is TreeKey) {
                (outKey as AbstractScreen<*>).unregisterScope()
            }

            if (mRootFrame!!.getChildAt(0) != null) {
                mRootFrame!!.removeView(mRootFrame!!.getChildAt(0))
            }
            mRootFrame!!.addView(newView)
            callback.onTraversalCompleted()
        }
    }
}