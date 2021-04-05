package games.figureit

import games.figureit.engine.Engine
import games.figureit.engine.first.EngineV1Impl
import games.figureit.engine.first.GameControl
import games.figureit.engine.first.ScoreControl
import games.figureit.engine.first.gamecontrol.GameControlSynchronized
import games.figureit.engine.first.gamecontrol.PlayerControl
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playercontrol.PlayerControlImpl
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.first.scorecontrol.FigureGenerator
import games.figureit.engine.first.scorecontrol.ScoreControlImpl
import games.figureit.engine.first.scorecontrol.ScoreScheduler
import games.figureit.engine.first.scorecontrol.figuregenerator.FigureGeneratorDiagonal
import games.figureit.engine.first.scorecontrol.scorescheduler.ScoreSchedulerTimer
import games.figureit.eventmanager.GameEventManager
import games.figureit.eventmanager.GameEventManagerWebSocket
import games.figureit.eventmanager.GameEventSubscriber
import games.figureit.eventmanager.PlayerEventManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class EngineConfiguration {
    @Bean
    fun gameEngine(
        gameEventManagerAggregator: GameEventManager
    ): Engine {
        val positionGenerator: PositionGenerator = PositionGeneratorFirstFree()
        val playerGenerator: PlayerGenerator = PlayerGeneratorImpl()
        val playerControl: PlayerControl = PlayerControlImpl(
            playerGenerator,
            positionGenerator,
            gameEventManagerAggregator
        )
        val gameControl: GameControl = GameControlSynchronized(playerControl, MAP_SIDE, gameEventManagerAggregator)
        val scoreScheduler: ScoreScheduler = ScoreSchedulerTimer(SCORE_TIME_MILLIS)
        val figureGenerator: FigureGenerator = FigureGeneratorDiagonal()
        val scoreControl: ScoreControl = ScoreControlImpl(
            timerControl = gameControl,
            playerListStore = playerControl,
            figureGenerator = figureGenerator,
            scoreScheduler = scoreScheduler,
            taskUpdateListener = gameEventManagerAggregator
        )

        return EngineV1Impl(playerControl, gameControl, scoreControl)
    }

    @Bean
    fun gameEventManagerWebSocket(gameEventSubscriber: GameEventSubscriber): PlayerEventManager {
        return GameEventManagerWebSocket(
            gameEventSubscriber = gameEventSubscriber,
            executorService = Executors.newFixedThreadPool(100)
        )
    }

    companion object {
        private const val MAP_SIDE = 100
        private const val SCORE_TIME_MILLIS: Long = 20000
    }
}
