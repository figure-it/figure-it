package games.figureit.engine.first.scorecontrol

interface ScoreScheduler {
    fun start(callback: Runnable)
    fun stop()
}
