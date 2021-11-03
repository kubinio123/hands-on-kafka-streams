package car

import scala.util.Random

object RandomData {
  val carIds = Seq(1, 2)
  val cities = Seq("Wroclaw", "Cracow")
  val streets = Seq("Sezamowa", "Tunelowa")

  def carSpeed: Seq[(CarId, CarSpeed)] =
    for {
      carId <- carIds
      speed = Random.between(5, 10) * 10
    } yield CarId(carId) -> CarSpeed(speed)

  def carEngine: Seq[(CarId, CarEngine)] =
    for {
      carId <- carIds
      rpm = Random.between(25, 35) * 100
    } yield CarId(carId) -> CarEngine(rpm)

  def carLocation: Seq[(CarId, CarLocation)] =
    for {
      carId <- carIds
      city = cities(Random.nextInt(cities.size))
      street = streets(Random.nextInt(streets.size))
    } yield CarId(carId) -> CarLocation(LocationId(city, street))

  def locationData: Seq[(LocationId, LocationData)] =
    for {
      city <- cities
      street <- streets
      speedLimit = Random.between(3, 7) * 10
      trafficVolume = TrafficVolume(Random.nextInt(TrafficVolume.maxId))
    } yield LocationId(city, street) -> LocationData(speedLimit, trafficVolume)
}
