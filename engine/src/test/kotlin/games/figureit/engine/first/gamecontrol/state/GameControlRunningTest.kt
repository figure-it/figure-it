package games.figureit.engine.first.gamecontrol.state

import games.figureit.engine.first.gamecontrol.GameControlState
import games.figureit.engine.first.gamecontrol.PlayerGenerator
import games.figureit.engine.first.gamecontrol.PositionGenerator
import games.figureit.engine.first.gamecontrol.playergenerator.PlayerGeneratorImpl
import games.figureit.engine.first.gamecontrol.positiongenerator.PositionGeneratorFirstFree
import games.figureit.engine.model.Field
import games.figureit.engine.model.Move
import games.figureit.engine.model.Move.DOWN
import games.figureit.engine.model.Move.LEFT
import games.figureit.engine.model.Move.RIGHT
import games.figureit.engine.model.Move.UP
import games.figureit.engine.model.Player
import games.figureit.engine.model.Position
import games.figureit.engine.model.PositionState.ON_MAP
import games.figureit.engine.model.PositionState.PENDING
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.nullValue
import org.hamcrest.Matchers.sameInstance
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class GameControlRunningTest {

    private lateinit var playerGenerator: PlayerGenerator

    @BeforeMethod
    fun beforeEach() {
        playerGenerator = PlayerGeneratorImpl()
    }

    @Test
    fun addNoPlayersActive() {
        val state = createDefaultState()
        val players = state.getActivePlayers()
        assertThat(players, Matchers.hasSize(0))
    }

    @Test
    fun addNoPlayersPending() {
        val state = createDefaultState()
        val players = state.getPendingPlayers()
        assertThat(players, Matchers.hasSize(0))
    }

    @Test
    fun newPlayersAreNotActive() {
        val state = createDefaultState()
        val players = state.getActivePlayers()
        assertThat(players, Matchers.hasSize(0))
    }

    @Test
    fun newPlayersArePending() {
        val gameState = createDefaultState()
        gameState.addPlayer()
        val players = gameState.getPendingPlayers()
        assertThat(players, Matchers.hasSize(1))
        val state = players.stream().findAny().get().positionState
        assertThat(state, equalTo(PENDING))
    }

    @Test
    fun movePlayer() {
        val field = Field(5, 5)
        val player = generatePlayerOnMap(2, 2, field)
        val state = createDefaultState(players = mutableMapOf(player.id to player), field = field)

        state.move(1, RIGHT)
        state.move(1, DOWN)
        state.move(1, RIGHT)

        assertThat(player.position, equalTo(Position(4,3)))
        assertThat(field.playerAt(2, 2), nullValue())
        assertThat(field.playerAt(4, 3), sameInstance(player))
    }

    @Test
    fun moveTwoPlayers() {
        val field = Field(5, 5)
        val player1 = generatePlayerOnMap(2, 2, field)
        val player2 = generatePlayerOnMap(3, 2, field)

        val state = createDefaultState(field = field,
            players = mutableMapOf(player1.id to player1, player2.id to player2))
        state.move(1, RIGHT)  //occupied
        state.move(1, DOWN)
        state.move(1, RIGHT)
        state.move(1, UP)     //occupied
        state.move(2, LEFT)
        state.move(1, UP)

        assertThat(player1.position, equalTo(Position(3,2)))
        assertThat(player2.position, equalTo(Position(2,2)))
        assertThat(field.playerAt(3, 2), sameInstance(player1))
        assertThat(field.playerAt(2, 2), sameInstance(player2))
    }

    private fun createDefaultState(
        positionGenerator: PositionGenerator = PositionGeneratorFirstFree(),
        playerGenerator: PlayerGenerator = PlayerGeneratorImpl(),
        field: Field = Field(5, 5),
        players: MutableMap<Long, Player> = HashMap()
    ): GameControlState {
        return GameControlRunning(
            positionGenerator = positionGenerator,
            playerGenerator = playerGenerator,
            field = field,
            players = players
        )
    }

    private fun generatePlayerOnMap(x: Int, y: Int, field: Field): Player {
        val player = playerGenerator.generate()
        player.position = Position(x, y)
        player.positionState = ON_MAP
        field.set(x, y, player)
        return player
    }

}
