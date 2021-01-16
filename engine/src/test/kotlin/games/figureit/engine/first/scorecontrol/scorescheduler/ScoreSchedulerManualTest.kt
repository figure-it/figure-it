package games.figureit.engine.first.scorecontrol.scorescheduler

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class ScoreSchedulerManualTest {

    private lateinit var checkRun: CheckRun
    private lateinit var scoreScheduler: ScoreSchedulerManual

    @BeforeMethod
    fun setUp() {
        checkRun = CheckRun()
        scoreScheduler = ScoreSchedulerManual()
    }

    @Test
    fun testBeforeStart() {
        assertThat(checkRun.runned, equalTo(false))
    }

    @Test
    fun testAfterStart() {
        scoreScheduler.start(checkRun)
        assertThat(checkRun.runned, equalTo(false))
    }

    @Test
    fun testAfterStartAndRun() {
        scoreScheduler.start(checkRun)
        scoreScheduler.run()
        assertThat(checkRun.runned, equalTo(true))
    }

    @Test
    fun testAfterRun() {
        scoreScheduler.run()
        assertThat(checkRun.runned, equalTo(false))
    }

    class CheckRun : Runnable {
        var runned = false
        override fun run() {
            runned = true
        }
    }
}
