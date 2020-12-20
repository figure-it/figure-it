package games.figureit.engine.model

import games.figureit.engine.first.gamecontrol.Field

enum class Move {
    LEFT {
        override fun move(field: Field, player: Player): Position {
            return movePlayerToPosition(field, player, field::leftFor)
        }
    },
    RIGHT {
        override fun move(field: Field, player: Player): Position {
            return movePlayerToPosition(field, player, field::rightFor)
        }
    },
    UP {
        override fun move(field: Field, player: Player): Position {
            return movePlayerToPosition(field, player, field::upFor)
        }
    },
    DOWN {
        override fun move(field: Field, player: Player): Position {
            return movePlayerToPosition(field, player, field::downFor)
        }
    };

    abstract fun move(field: Field, player: Player): Position

    companion object {
        private fun movePlayerToPosition(field: Field, player: Player, action: (Position) -> Position): Position {
            val newPosition = action.invoke(player.position)
            if (field.freeAt(newPosition)) {
                field.set(player.position, null)
                field.set(newPosition, player)
                player.position = newPosition
            }
            return player.position
        }
    }
}
