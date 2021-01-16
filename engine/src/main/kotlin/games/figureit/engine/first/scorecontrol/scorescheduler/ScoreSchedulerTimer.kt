package games.figureit.engine.first.scorecontrol.scorescheduler

import games.figureit.engine.first.scorecontrol.ScoreScheduler
import java.lang.Thread.sleep

class ScoreSchedulerTimer(private val millisPause: Long) : ScoreScheduler {

    private var threadActive: Thread? = null

    @Synchronized
    override fun start(callback: Runnable) {
        if (threadActive == null) {
            threadActive = Thread {
                while (true) {
                    try {
                        sleep(millisPause)
                    } catch (ignored: InterruptedException) {
                        break
                    }
                    if (Thread.interrupted()) {
                        break
                    }
                    callback.run()
                }
            }
            threadActive!!.start()
        }
    }

    @Synchronized
    override fun stop() {
        threadActive?.interrupt()
        threadActive = null
    }
}
