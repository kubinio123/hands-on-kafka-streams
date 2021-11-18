package car.drivernotifier

import car.domain._

object DriverNotifierData {
  case class CarData(speed: Option[CarSpeed], engine: Option[CarEngine], location: Option[CarLocation])

  object CarData {
    val empty: CarData = CarData(None, None, None)
  }

  case class CarAndLocationData(carData: CarData, locationData: LocationData)
}
