package org.plos_clan.cpos.task

data class Process(
    val id: Int,
    val name: String,
)

object ProcessManager {
    private var nextProcessId = 0
    private val processes = mutableListOf<Process>()

    fun initialize() {
        val kernelProcess = Process(
            id = nextProcessId++,
            name = "System",
        )
        processes += kernelProcess
        println("Multi-task initialized ${kernelProcess.name} PID=${kernelProcess.id}")
    }
}
