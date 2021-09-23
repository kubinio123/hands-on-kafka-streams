package car

import car.CarMetric._

import scala.util.Random

object RandomData {
  val carIds = Seq(1, 2)
  val cities = Seq("Wroclaw", "Cracow")

  def carMetrics: Seq[(CarMetricKey, CarMetricValue)] =
    for {
      carId <- carIds
      (metric, value) <-
        Seq(Speed -> Random.between(100, 130), EngineRpm -> Random.between(2000, 3500), TirePressure -> Random.between(1, 2))
    } yield CarMetricKey(carId, metric) -> CarMetricValue(value)

  def carLocations: Seq[(CarLocationKey, CarLocationValue)] =
    for {
      carId <- carIds
      location = cities(Random.nextInt(cities.size))
    } yield CarLocationKey(carId) -> CarLocationValue(location)

  def weather: Seq[(WeatherKey, WeatherValue)] =
    for {
      city <- cities
      temperature = Random.between(10, 30)
      isStorming = Random.nextBoolean()
    } yield WeatherKey(city) -> WeatherValue(temperature, isStorming)
}
