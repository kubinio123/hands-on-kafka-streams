package car

import scala.util.Random

object RandomData {
  val carIds = Seq(1, 2)
  val cities = Seq("Wroclaw", "Cracow")
  val streets = Seq("Sezamowa", "Tunelowa")

  def carSpeed: Seq[(CarDataKey, CarSpeedData)] =
    for {
      carId <- carIds
      speed = Random.between(5, 10) * 10
    } yield CarDataKey(carId) -> CarSpeedData(speed)

  def carEngine: Seq[(CarDataKey, CarEngineData)] =
    for {
      carId <- carIds
      rpm = Random.between(25, 35) * 100
    } yield CarDataKey(carId) -> CarEngineData(rpm)

  def carLocation: Seq[(CarDataKey, CarLocationData)] =
    for {
      carId <- carIds
      city = cities(Random.nextInt(cities.size))
      street = streets(Random.nextInt(streets.size))
    } yield CarDataKey(carId) -> CarLocationData(city, street)

  def locationData: Seq[(LocationDataKey, LocationData)] =
    for {
      city <- cities
      street <- streets
      speedLimit = Random.between(3, 7) * 10
      trafficVolume = TrafficVolume(Random.nextInt(TrafficVolume.maxId))
    } yield LocationDataKey(city, street) -> LocationData(speedLimit, trafficVolume)
}
