# Лабораторная работа 3: Интерфейсы

## Описание проекта
Проект реализует симуляцию движения объектов с использованием интерфейсов в Kotlin.

#### 1. Movable.kt - Интерфейс движущихся объектов
interface Movable {
    var x: Double
    var y: Double
    var speed: Double
    fun move()
}

Назначение: Определяет контракт для всех движущихся объектов в системе.

Свойства:
    x: Double - координата X в картезианской системе
    y: Double - координата Y в картезианской системе
    speed: Double - скорость движения объекта

Методы:
    move() - основной метод для реализации движения

2. Human.kt - Класс человека
kotlin

open class Human(
    private var fullName: String,
    private var age: Int,
    override var speed: Double
) : Movable

Назначение: Базовый класс для представления человека со случайным движением.

Наследование: Реализует интерфейс Movable

Свойства:
    fullName: String - ФИО человека
    age: Int - возраст
    speed: Double - скорость движения (из интерфейса)
    x: Double, y: Double - координаты (из интерфейса)

Методы:
    move() - реализует модель Random Walk
    Геттеры и сеттеры для свойств
    Конструкторы: первичный и вторичный

3. Driver.kt - Класс водителя
kotlin

class Driver(
    fullName: String,
    age: Int,
    speed: Double,
    private val carModel: String
) : Human(fullName, age, speed)

Назначение: Класс для представления водителя с прямолинейным движением.

Наследование: Наследует от Human, реализует Movable через родителя

Дополнительные свойства:
    carModel: String - модель автомобиля
    directionX: Double - направление по оси X
    directionY: Double - направление по оси Y

Методы:
    move() - переопределен для прямолинейного движения

4. Main.kt - Основная программа
kotlin

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

Назначение: Точка входа в программу, управление симуляцией движения.

Функциональность:
    Создание списка движущихся объектов
    Организация многопоточной симуляции
    Управление временем выполнения
    Вывод результатов движения

Модели движения
Random Walk (Human)

Формула:

x(t+1) = x(t) + Δx × speed
y(t+1) = y(t) + Δy × speed

где Δx, Δy ∈ [-1, 1] - случайные значения
Прямолинейное движение (Driver)

Формула:

x(t+1) = x(t) + directionX × speed
y(t+1) = y(t) + directionY × speed

где directionX, directionY - постоянные коэффициенты
