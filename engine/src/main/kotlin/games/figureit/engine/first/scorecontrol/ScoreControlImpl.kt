package games.figureit.engine.first.scorecontrol

import games.figureit.engine.first.PlayerListStore
import games.figureit.engine.first.ScoreControl
import games.figureit.engine.first.TimerControl
import games.figureit.engine.first.listener.EmptyTaskUpdateListener
import games.figureit.engine.first.listener.TaskUpdateListener
import games.figureit.engine.model.Figure
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import java.lang.IllegalStateException
import java.lang.Thread.sleep
import java.util.ArrayList

class ScoreControlImpl(
    private val timerControl: TimerControl,
    private val playerListStore: PlayerListStore,
    private val figureGenerator: FigureGenerator,
    private val scoreScheduler: ScoreScheduler,
    private val taskUpdateListener: TaskUpdateListener = EmptyTaskUpdateListener()
) : ScoreControl {
    private lateinit var currentFigure: Figure

    override fun start() {
        val players = playerListStore.getActivePlayers() + playerListStore.getPendingAddPlayers()
        val playerCount = players.size
        currentFigure = figureGenerator.generate(playerCount)
        timerControl.startTheWorld()
        scoreScheduler.start(this)
    }

    override fun stop() {
        timerControl.stopTheWorld()
        scoreScheduler.stop()
    }

    override fun getFigure(): Figure {
        if (!this::currentFigure.isInitialized) {
            throw IllegalStateException("Cannot retrieve figure without first start")
        }
        return currentFigure
    }

    override fun run() {
        timerControl.stopTheWorld()
        sleep(100)
        val players = playerListStore.getActivePlayers()
        val checker = FigureChecker(players, currentFigure, taskUpdateListener)
        checker.checkAndReward()
        sleep(100)
        val playersCurrent = players.size
        val playersToAdd = playerListStore.getPendingAddPlayers().size
        val playersToRemove = playerListStore.getPendingRemovePlayers().size
        currentFigure = figureGenerator.generate(playersCurrent + playersToAdd - playersToRemove)
        taskUpdateListener.taskUpdated(currentFigure)
        sleep(100)
        timerControl.startTheWorld()
    }

    private class FigureChecker(
        private val players: Collection<Player>,
        private val figure: Figure,
        private val taskUpdateListener: TaskUpdateListener
    ) {
        private val figurePixels: List<Position> = positionsOfFigurePixels()
        private val positionsWithPlayers = players.map { it.position to it }.toMap()

        fun checkAndReward() {
            if (players.isEmpty()) {
                return
            }
            val minX = players.minOf { it.position.x }
            val minY = players.minOf { it.position.y }
            val maxX = players.maxOf { it.position.x }
            val maxY = players.maxOf { it.position.y }

            val figureHeight = figure.image.size
            val figureWidth = figure.image[0].length

            for (x in minX..maxX - figureWidth + 1) {
                for (y in minY..maxY - figureHeight + 1) {
                    val pos = Position(x, y)
                    if (detectFigureAt(pos)) {
                        addScoresForFigureAt(pos)
                    }
                }
            }
        }

        private fun detectFigureAt(figurePosition: Position): Boolean {
            for (pixelPosition in figurePixels) {
                if (positionsWithPlayers[figurePosition.add(pixelPosition)] == null) {
                    return false
                }
            }
            return true
        }

        private fun addScoresForFigureAt(figurePosition: Position) {
            val awardedPlayers: MutableList<Long> = ArrayList()
            for (pixelPosition in figurePixels) {
                val player = positionsWithPlayers[figurePosition.add(pixelPosition)]
                player ?. let {
                    it.score += figure.points
                    awardedPlayers.add(it.id)
                }
            }
            taskUpdateListener.taskCompleted(figure, figurePosition, awardedPlayers)
        }

        private fun positionsOfFigurePixels(): List<Position> {
            val result = ArrayList<Position>()
            for ((y, row) in figure.image.withIndex()) {
                var x = 0
                for (c in row) {
                    if (c == '1') {
                        result.add(Position(x, y))
                    }
                    x++
                }
            }
            return result
        }
    }
}
