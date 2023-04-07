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


class FirstGameView(ctx: Context, attributeSet: AttributeSet): SurfaceView(ctx,attributeSet) {

    var line = BitmapFactory.decodeResource(ctx.resources,R.drawable.f1)
    var vorota = BitmapFactory.decodeResource(ctx.resources,R.drawable.vorota)
    var ball = BitmapFactory.decodeResource(ctx.resources,ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getInt("ball",R.drawable.ball1))
    var ball1 = BitmapFactory.decodeResource(ctx.resources,R.drawable.ball1)


    public var score = 0
    var health = 3
    var sound = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("sounds",true)
    var music = ctx.getSharedPreferences("prefs",Context.MODE_PRIVATE).getBoolean("music",true)
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
    private var udar = MediaPlayer.create(ctx,R.raw.sound)

    init {
        player.setOnCompletionListener {
            it.start()
        }
        if(music) player.start()
        line = Bitmap.createScaledBitmap(line, (line.width/3).toInt(),
            (line.height/2.5).toInt(),true)
        ball = Bitmap.createScaledBitmap(ball,ball.width/5,ball.height/5,true)
        ball1 = Bitmap.createScaledBitmap(ball1,ball1.width/5,ball1.height/5,true)
        vorota = Bitmap.createScaledBitmap(vorota,vorota.width/6,vorota.height/6,true)
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
                    bx = (canvas.width/2f-vorota.width/2f).toInt()
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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_DOWN -> {
                if(!play) {
                    if(sound) udar.start()
                    play = true
                }
            }
        }
        postInvalidate()
        return true
    }

    var paused = false
    var front = true
    var play = false;
    var delta = 4
    var by = 0f
    val update = Runnable{
        var isEnd = false
        var sc = false
        if(paused) return@Runnable
        try {
            val canvas = holder.lockCanvas()
            canvas.drawColor(Color.parseColor("#FF6600"))
            canvas.drawText("$score",canvas.width/2f-50,canvas.height/4f,paintT)
            var d = canvas.width-line.width
           if(!play) {
               if(front) {
                   if(bx+vorota.width>=d/2+line.width) front = false
                   else bx += delta
               } else {
                   if(bx<=d/2) front = true
                   else bx-= delta
               }
               by = canvas.height/3f+line.height-ball.height*2
            } else {
                if(by<=canvas.height/3f+100f+ball.height*0.5) {
                    var tmp = bx - canvas.width/2f
                    if(tmp>=0 && tmp<=ball.width/2 || tmp<0 && ball.width/2-tmp<=vorota.width) {
                        play = false
                        score += 3
                    } else {
                        play = false
                        health--
                        if(health<=0) {
                            isEnd = true
                        }
                    }
                } else by -= 4
            }
             for(i in 1..health) {
                val tmp = Bitmap.createScaledBitmap(ball1,ball1.width,ball1.height,true)
                canvas.drawBitmap(tmp,canvas.width-150f-(max(i-1,0))*(tmp.width+10f),50f,paintB)
            }
            canvas.drawBitmap(line,d/2f,canvas.height/3f+100f,paintB)
            canvas.drawBitmap(vorota,bx.toFloat(),canvas.height/3+100f,paintB)
            canvas.drawBitmap(ball,canvas.width/2f-ball.width/2,by,paintB)
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


    companion object {
        interface EndListener {
            fun end();
            fun score(score: Int);
        }
    }

}