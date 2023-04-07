package com.benatosports.game



import android.content.Context
import android.content.Context.VIBRATOR_MANAGER_SERVICE
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.media.MediaPlayer
import android.os.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


class SecondGameView(ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx,attributeSet) {

    var line = BitmapFactory.decodeResource(ctx.resources,R.drawable.line)
    var ball = BitmapFactory.decodeResource(ctx.resources,R.drawable.vorota)
    var ball1 = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball1)
    var ball2 = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball7)
    var ball3 = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball8)


    public var score = 0
    var health = 3
    var sound = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
    var speed = arrayOf(4,8,16)
    private val random = Random()
    private var millis = 0
    var bx = 0
    private var listener: EndListener? = null
    private var paintB: Paint = Paint(Paint.DITHER_FLAG)
    private var paintT = Paint().apply {
        color = Color.WHITE
        textSize = 120f
        style = Paint.Style.FILL
    }
    private var player = MediaPlayer.create(ctx,R.raw.music)

    init {
        line = Bitmap.createScaledBitmap(line,(line.width/2.5).toInt(),line.height/3,true)
        ball = Bitmap.createScaledBitmap(ball,ball.width/5,ball.height/5,true)
        ball1 = Bitmap.createScaledBitmap(ball1,ball1.width/3,ball1.height/3,true)
        ball2 = Bitmap.createScaledBitmap(ball2,ball2.width/3,ball2.height/3,true)
        ball3 = Bitmap.createScaledBitmap(ball3,ball3.width/3,ball3.height/3,true)
        player.setOnCompletionListener {
            it.start()
        }
        if(sound) player.start()
        holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceCreated(holder: SurfaceHolder) {

            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val canvas = holder.lockCanvas()
                if(canvas!=null) {
                    bx = (canvas.width/2f-ball.width/2f).toInt()
                    draw(canvas)
                    holder.unlockCanvasAndPost(canvas)
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                paused = true
                player.stop()
            }

        })
        val updateThread = Thread {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    if (!paused) {
                        update.run()
                        millis ++
                    }
                }
            }, 500, 16)
        }

        updateThread.start()
    }
    var code = -1f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_UP -> {
                code = -1f
            }
            MotionEvent.ACTION_DOWN -> {
                code = event.x
            }
        }
        postInvalidate()
        return true
    }

    var paused = false
    var list = mutableListOf<Model>()
    var delta = 8
    val update = Runnable{
        var isEnd = false
        var sc = false
        if(paused) return@Runnable
        try {
            val canvas = holder.lockCanvas()
            if(code>=0) {
                if(code>bx) bx+=delta
                else bx-=delta
            }
            if(bx<=-ball.width) bx = canvas.width
            if(bx>=ball.width+canvas.width) bx = 0
            var i = 0
            while(i<list.size) {
                Log.d("TAG","$i")
                list[i].y+=speed[0]
                if(abs(list[i].x-bx)<=ball.width/2 && abs(list[i].y-(canvas.height-200f-line.height/2f))<=ball.height) {
                    score++
                    if(list[i].cur==2) health = min(3,health+1)
                    else if(list[i].cur==1) score+=5
                    list.removeAt(i)
                } else if(list[i].y>canvas.height+ball1.height) {
                    list.removeAt(i)
                    health--
                    if(health<=0) {
                        isEnd = true
                    }
                } else i++
            }
            while(list.size<4) {
                list.add(Model(random.nextInt(canvas.width).toFloat(),-1f*ball1.height,random.nextInt(3)))
            }
            canvas.drawColor(Color.parseColor("#FF6600"))
            canvas.drawText("$score",canvas.width/2f-50,canvas.height/2f-100,paintT)
            canvas.drawBitmap(line,0f,canvas.height-300f,paintB)
            for(i in 1..health) {
                val tmp = Bitmap.createScaledBitmap(ball1,ball1.width/2,ball1.height/2,true)
                canvas.drawBitmap(tmp,canvas.width-150f-(max(i-1,0))*(tmp.width+10f),50f,paintB)
            }
            for(i in list) {
                canvas.drawBitmap(getBall(i.cur),i.x,i.y,paintB)
            }
            canvas.drawBitmap(ball,bx.toFloat(),canvas.height-300f-line.height/2f,paintB)
            holder.unlockCanvasAndPost(canvas)
            if(isEnd) {
                Log.d("TAG","END")
                togglePause()
                if(listener!=null) listener!!.end()
            }
            if(sc) {
                listener?.score(score)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setEndListener(list: EndListener) {
        this.listener = list
    }
    fun togglePause() {
        paused = !paused
    }

    fun getBall(g: Int): Bitmap {
        return when(g) {
            1 -> ball2
            2 -> ball3
            else ->ball1
        }
    }

    companion object {
        interface EndListener {
            fun end();
            fun score(score: Int);
        }
        data class Model(var x: Float, var y: Float, var cur: Int)
    }

}