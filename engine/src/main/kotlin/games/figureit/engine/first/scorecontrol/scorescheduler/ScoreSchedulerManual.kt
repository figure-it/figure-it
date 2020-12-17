package games.figureit.engine.first.scorecontrol.scorescheduler

import games.figureit.engine.first.scorecontrol.ScoreScheduler

class ScoreSchedulerManual: ScoreScheduler, Runnable {

    private var callback: Runnable? = null

    override fun start(callback: Runnable) {
        this.callback = callback
    }

    override fun stop() {
        callback = null
    }

    override fun run() {
        callback?.run()
    }
}
