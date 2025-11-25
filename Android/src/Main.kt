fun main() {
    println("=== НАЧАЛО СИМУЛЯЦИИ ДВИЖЕНИЯ ===")

    val movables: List<Movable> = listOf(
        Human("Иванов Иван Иванович", 20, 2.5),
        Human("Петров Петр Петрович", 22, 1.8),
        Human("Сидорова Анна Сергеевна", 19, 3.0),
        Human("Козлов Алексей Дмитриевич", 21, 2.0),
        Driver("Волков Сергей Александрович", 35, 4.0, "Toyota Camry")
    )

    val simulationTime = 10

    for (second in 1..simulationTime) {
        println("\n--- Секунда $second ---")

        val threads = mutableListOf<Thread>()

        movables.forEach { movable ->
            val thread = Thread {
                movable.move()
            }
            threads.add(thread)
            thread.start()
        }

        threads.forEach { it.join() }
        Thread.sleep(1000)
    }

    println("\n=== СИМУЛЯЦИЯ ЗАВЕРШЕНА ===")
}